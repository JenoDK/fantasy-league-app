package com.jeno.fantasyleague.backend.data.service.leaguetemplates;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;

import com.google.common.collect.Lists;
import com.jeno.fantasyleague.backend.data.repository.ContestantGroupRepository;
import com.jeno.fantasyleague.backend.data.repository.ContestantRepository;
import com.jeno.fantasyleague.backend.data.repository.GameRepository;
import com.jeno.fantasyleague.backend.data.repository.LeagueRepository;
import com.jeno.fantasyleague.backend.model.Contestant;
import com.jeno.fantasyleague.backend.model.ContestantGroup;
import com.jeno.fantasyleague.backend.model.Game;
import com.jeno.fantasyleague.backend.model.League;
import com.jeno.fantasyleague.backend.model.User;
import com.jeno.fantasyleague.util.DateUtil;

public abstract class FootballInitializer {

	public static final String ID = "Match Number";
	public static final String ROUND_NUMBER = "Round Number";
	public static final String DATE = "Date";
	public static final String LOCATION = "Location";
	public static final String HOME_TEAM = "Home Team";
	public static final String AWAY_TEAM = "Away Team";
	public static final String NEXT_GAME = "Next Game";

	@Autowired
	private FootballSettingsRenderer settingsRenderer;
	@Autowired
	private ContestantRepository contestantRepository;
	@Autowired
	private ContestantGroupRepository contestantGroupRepository;
	@Autowired
	private GameRepository gameRepository;
	@Autowired
	private LeagueRepository leagueRepository;

	public void addNewLeague(League newLeague, User user) throws TemplateException {
		Map<Group, ContestantGroup> groupsMap = Arrays.stream(getGroups())
				.collect(Collectors.toMap(Function.identity(), group -> createContestantGroup(group, newLeague)));
		List<Contestant> contestantList = contestantRepository.saveAll(
				Arrays.stream(getTeams())
						.map(team -> createContestant(team, groupsMap, newLeague))
						.collect(Collectors.toList()));
		List<Game> addedGames = addAllGames(contestantList, newLeague);
		setLeagueStartDate(newLeague, addedGames);
		settingsRenderer.addDefaultLeagueSettings(newLeague);
	}

	private List<Game> addAllGames(List<Contestant> contestants, League league) {
		Map<String, GameDto> gameDtos = readCsv(getCsvPath()).stream()
				.map(record -> createGame(record, contestants, league))
				.collect(Collectors.toMap(dto -> dto.id, Function.identity()));
		gameDtos.values().stream()
				.filter(dto -> dto.nextGameId != null && !dto.nextGameId.isEmpty())
				.forEach(dto -> dto.game.setNext_game(gameDtos.get(dto.nextGameId).game));
		return gameRepository.saveAll(gameDtos.values().stream()
				.map(dto -> dto.game)
				.collect(Collectors.toList()));
	}

	private GameDto createGame(CSVRecord record, List<Contestant> contestants, League league) {
		String id = record.get(ID);
		String round = record.get(ROUND_NUMBER);
		String date = record.get(DATE);
		String location = record.get(LOCATION);
		String home_team = record.get(HOME_TEAM);
		String away_team = record.get(AWAY_TEAM);
		String nextGameId = record.get(NEXT_GAME);

		Game game = new Game();

		setTeams(contestants, home_team, away_team, game);
		game.setRound(round);
		game.setStage(getStage(round).name());
		game.setLocation(location);
		game.setGameDateTime(LocalDateTime.parse(date, DateUtil.DATE_TIME_FORMATTER));
		game.setLeague(league);
		game.setMatchNumber(Integer.valueOf(id));

		return new GameDto(game, id, nextGameId);
	}

	private void setTeams(List<Contestant> contestants, String home_team, String away_team, Game game) {
		Optional<Contestant> homeTeam = contestants.stream()
				.filter(c -> c.getName().equals(home_team))
				.findFirst();

		Optional<Contestant> awayTeam = contestants.stream()
				.filter(c -> c.getName().equals(away_team))
				.findFirst();

		if (homeTeam.isPresent()) {
			game.setHome_team(homeTeam.get());
		} else {
			game.setHome_team_placeholder(home_team);
		}

		if (awayTeam.isPresent()) {
			game.setAway_team(awayTeam.get());
		} else {
			game.setAway_team_placeholder(away_team);
		}
	}

	private void setLeagueStartDate(League newLeague, List<Game> addedGames) {
		Optional<LocalDateTime> earliestGameDate = addedGames.stream()
				.map(Game::getGameDateTime)
				.min(LocalDateTime::compareTo);
		if (earliestGameDate.isPresent()) {
			newLeague.setLeague_starting_date(earliestGameDate.get());
			leagueRepository.saveAndFlush(newLeague);
		}
	}

	private ContestantGroup createContestantGroup(Group group, League league) {
		ContestantGroup contestantGroup = new ContestantGroup();
		contestantGroup.setName(group.getGroupName());
		contestantGroup.setLeague(league);
		return contestantGroupRepository.saveAndFlush(contestantGroup);
	}

	private Contestant createContestant(Team team, Map<Group, ContestantGroup> groupsMap, League league) {
		Contestant contestant = new Contestant();
		contestant.setName(team.getName());
		contestant.setIcon_path(team.getIconPath());
		contestant.setLeague(league);
		contestant.setPower_index(team.getUnibetScoreWinner());
		contestant.setContestant_group(groupsMap.get(team.getGroup()));
		return contestant;
	}

	private SoccerCupStages getStage(String round) {
		if ("Round of 16".equals(round)) {
			return SoccerCupStages.EIGHTH_FINALS;
		} else if ("Quarter Finals".equals(round)) {
			return SoccerCupStages.QUARTER_FINALS;
		} else if ("Semi Finals".equals(round)) {
			return SoccerCupStages.SEMI_FINALS;
		} else if ("Final".equals(round) || "Finals".equals(round)) {
			return SoccerCupStages.FINALS;
		}
		return SoccerCupStages.GROUP_PHASE;
	}

	private List<CSVRecord> readCsv(String path) {
		try {
			Reader in = new InputStreamReader(new ClassPathResource(path).getInputStream());
			return Lists.newArrayList(CSVFormat.RFC4180.withHeader().parse(in));
		} catch (FileNotFoundException e) {
			throw new TemplateException("CSV file not found", e);
		} catch (IOException e) {
			throw new TemplateException("Something went wrong during the parsing of the csv file", e);
		}
	}

	protected abstract String getCsvPath();

	protected abstract Team[] getTeams();

	protected abstract Group[] getGroups();

	public static class Group {

		private final String groupName;
		private final String shortName;

		public Group(String groupName, String shortName) {
			this.groupName = groupName;
			this.shortName = shortName;
		}

		public String getGroupName() {
			return groupName;
		}

		public String getShortName() {
			return shortName;
		}
	}

	public static class Team {

		private final String name;
		private final Group group;
		private final Double unibetScoreWinner;
		private final String iconPath;
		private final Double unibetScoreTop4;
		private final Double oddsToLeaveGroup;

		public Team(String name, Group group, Double unibetScoreWinner, String iconPath) {
			this.name = name;
			this.group = group;
			this.oddsToLeaveGroup = null;
			this.unibetScoreTop4 = null;
			this.unibetScoreWinner = unibetScoreWinner;
			this.iconPath = iconPath;
		}

		public Team(String name, Group group, Double oddsToLeaveGroup, Double unibetScoreTop4, Double unibetScoreWinner, String iconPath) {
			this.name = name;
			this.group = group;
			this.oddsToLeaveGroup = oddsToLeaveGroup;
			this.unibetScoreTop4 = unibetScoreTop4;
			this.unibetScoreWinner = unibetScoreWinner;
			this.iconPath = iconPath;
		}

		public String getName() {
			return name;
		}

		public Group getGroup() {
			return group;
		}

		public Double getOddsToLeaveGroup() {
			return oddsToLeaveGroup;
		}

		public Double getUnibetScoreTop4() {
			return unibetScoreTop4;
		}

		public Double getUnibetScoreWinner() {
			return unibetScoreWinner;
		}

		/**
		 * Uses the unibet scores to calculate a power index ranging from 0 - 100 where 100 is the best.
		 * @return the pwoer index
		 */
		public Double getPowerIndex(Double maxTop4Score, Double maxOutOfGroupsScore) {
			Double maxCombined = maxTop4Score + maxOutOfGroupsScore;
			Double combined = getUnibetScoreTop4() + getOddsToLeaveGroup();
			return ((maxCombined - combined) / maxCombined) * 100;
		}

		public String getIconPath() {
			return iconPath;
		}
	}

	private class GameDto {

		private Game game;
		private String id;
		private String nextGameId;

		public GameDto(Game game, String id, String nextGameId) {
			this.game = game;
			this.id = id;
			this.nextGameId = nextGameId;
		}

	}
}

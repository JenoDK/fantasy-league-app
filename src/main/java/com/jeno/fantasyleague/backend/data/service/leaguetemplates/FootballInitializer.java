package com.jeno.fantasyleague.backend.data.service.leaguetemplates;

import com.google.common.collect.Lists;
import com.jeno.fantasyleague.backend.data.repository.ContestantGroupRepository;
import com.jeno.fantasyleague.backend.data.repository.ContestantRepository;
import com.jeno.fantasyleague.backend.data.repository.GameRepository;
import com.jeno.fantasyleague.backend.data.repository.LeagueRepository;
import com.jeno.fantasyleague.backend.model.*;
import com.jeno.fantasyleague.util.DateUtil;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

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
		contestant.setPower_index(team.getPowerIndex());
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
		private final String iconPath;
		private final Double oddsTop8;
		private final Double oddsTop4;
		private final Double oddsTop3;
		private final Double oddsTop2;
		private final Double oddsTop1;
		private final Double powerIndex;

		public Team(String name, Group group, Double unibetScoreWinner, String iconPath) {
			this.name = name;
			this.group = group;
			this.oddsTop8 = null;
			this.oddsTop4 = null;
			this.oddsTop3 = null;
			this.oddsTop2 = null;
			this.oddsTop1 = unibetScoreWinner;
			this.iconPath = iconPath;
			this.powerIndex = unibetScoreWinner;
		}

		public Team(String name, Group group, Double oddsTop8, Double oddsTop4, Double oddsTop3, Double oddsTop2, Double oddsTop1, String iconPath, Double powerIndex) {
			this.name = name;
			this.group = group;
			this.oddsTop8 = oddsTop8;
			this.oddsTop4 = oddsTop4;
			this.oddsTop3 = oddsTop3;
			this.oddsTop2 = oddsTop2;
			this.oddsTop1 = oddsTop1;
			this.iconPath = iconPath;
			if (powerIndex == null) {
				this.powerIndex = null;
			} else {
				this.powerIndex = powerIndex;
			}
		}

		public String getName() {
			return name;
		}

		public Group getGroup() {
			return group;
		}

		public Double getOddsTop8() {
			return oddsTop8;
		}

		public Double getOddsTop4() {
			return oddsTop4;
		}

		public Double getOddsTop3() {
			return oddsTop3;
		}

		public Double getOddsTop2() {
			return oddsTop2;
		}

		public Double getOddsTop1() {
			return oddsTop1;
		}

		public Double getPowerIndex() {
			return powerIndex;
		}

		/**
		 * Uses the unibet scores to calculate a power index ranging from 0 - 100 where 100 is the best.
		 * @return the pwoer index
		 */
		public Double getPowerIndex(Double maxTop1, Double maxTop2, Double maxTop3, Double maxTop4, Double maxTop8, Double medianTop1, Double medianTop2, Double medianTop3, Double medianTop4, Double medianTop8, Double totalTop1, Double totalTop2, Double totalTop3, Double totalTop4, Double totalTop8) {
//			double odds1Perc = (oddsTop1 / totalTop1) * 100;
//			double odds2Perc = (oddsTop2 / totalTop2) * 100;
//			double odds3Perc = (oddsTop3 / totalTop3) * 100;
//			double odds4Perc = (oddsTop4 / totalTop4) * 100;
//			double odds8Perc = (oddsTop8 / totalTop8) * 100;
//
//			double totalOddsPerc = (odds1Perc + odds2Perc + odds3Perc + odds4Perc + odds8Perc) / 5;

//			Double oddsTop1Percentage = getSqrt(maxTop1, oddsTop1);
//			Double oddsTop2Percentage = getSqrt(maxTop2, oddsTop2);
//			Double oddsTop3Percentage = getSqrt(maxTop3, oddsTop3);
//			Double oddsTop1Percentage = oddsTop1 * 100 / maxTop1;
//			Double oddsTop2Percentage = oddsTop2 * 100 / maxTop2;
//			Double oddsTop3Percentage = oddsTop3 * 100 / maxTop3;
//			Double oddsTop4Percentage = oddsTop4 * 100 / maxTop4;
			Double oddsTop8Percentage = oddsTop8 * 100 / maxTop8;
//			boolean includeWinner = true;
//			double percentageTotal;
//			if (includeWinner) {
//				percentageTotal = 100 - ((oddsTop1Percentage + oddsTop2Percentage + oddsTop3Percentage + oddsTop4Percentage + oddsTop8Percentage) / 5);
//			} else {
//				percentageTotal = 100 - ((oddsTop2Percentage + oddsTop3Percentage + oddsTop4Percentage + oddsTop8Percentage) / 4);
//			}
			BigDecimal wurtel = BigDecimal.valueOf(oddsTop1).sqrt(MathContext.DECIMAL32);
			BigDecimal wurtel2 = BigDecimal.valueOf(oddsTop2).sqrt(MathContext.DECIMAL32);
			BigDecimal wurtel3 = BigDecimal.valueOf(oddsTop3).sqrt(MathContext.DECIMAL32);
			BigDecimal wurtel4 = BigDecimal.valueOf(oddsTop4).sqrt(MathContext.DECIMAL32);
			BigDecimal wurtel8 = BigDecimal.valueOf(oddsTop8).sqrt(MathContext.DECIMAL32);

			BigDecimal medianAvg = BigDecimal.valueOf(medianTop1 + medianTop2 + medianTop3 + medianTop4 + medianTop8).divide(BigDecimal.valueOf(5));

			double logsken1 = Math.log(oddsTop1);
			double logsken2 = Math.log(oddsTop2);
			double logsken3 = Math.log(oddsTop3);
			double logsken4 = Math.log(oddsTop4);
			double logsken8 = Math.log(oddsTop8);
			double logskenTot = logsken1 + logsken2 + logsken3 + logsken4 + logsken8;
			double logskenTotSquared = BigDecimal.valueOf(logskenTot).pow(2).doubleValue();
			double wurtelTot = 100 - wurtel.add(wurtel2).add(wurtel3).add(wurtel4).add(wurtel8).doubleValue();
//			System.err.println(name + " -> " + BigDecimal.valueOf(logskenTot).pow(2));
			return wurtelTot;
		}

		private double getSqrt(Double maxTop1, Double oddsTop1) {
			return BigDecimal.valueOf(oddsTop1).sqrt(MathContext.DECIMAL32).doubleValue() * 100 / maxTop1;
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

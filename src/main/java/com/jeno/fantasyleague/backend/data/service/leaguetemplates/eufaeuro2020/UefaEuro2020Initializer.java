package com.jeno.fantasyleague.backend.data.service.leaguetemplates.eufaeuro2020;

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
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.jeno.fantasyleague.backend.data.repository.ContestantGroupRepository;
import com.jeno.fantasyleague.backend.data.repository.ContestantRepository;
import com.jeno.fantasyleague.backend.data.repository.GameRepository;
import com.jeno.fantasyleague.backend.data.repository.LeagueRepository;
import com.jeno.fantasyleague.backend.data.service.leaguetemplates.TemplateException;
import com.jeno.fantasyleague.backend.data.service.leaguetemplates.SoccerCupStages;
import com.jeno.fantasyleague.backend.model.Contestant;
import com.jeno.fantasyleague.backend.model.ContestantGroup;
import com.jeno.fantasyleague.backend.model.Game;
import com.jeno.fantasyleague.backend.model.League;
import com.jeno.fantasyleague.backend.model.User;
import com.jeno.fantasyleague.util.DateUtil;

@Component
@Transactional(rollbackFor = Exception.class)
public class UefaEuro2020Initializer {

	public static final String ID = "Id";
	public static final String ROUND_NUMBER = "Round Number";
	public static final String DATE = "Date";
	public static final String LOCATION = "Location";
	public static final String HOME_TEAM = "Home Team";
	public static final String AWAY_TEAM = "Away Team";
	public static final String NEXT_GAME = "Next Game";

	@Autowired
	private UefaEuro2020SettingRenderer uefaEuro2020SettingRenderer;
	@Autowired
	private ContestantRepository contestantRepository;
	@Autowired
	private ContestantGroupRepository contestantGroupRepository;
	@Autowired
	private GameRepository gameRepository;
	@Autowired
	private LeagueRepository leagueRepository;

	public void addNewLeague(League newLeague, User user) throws TemplateException {
		Map<Group, ContestantGroup> groupsMap = Arrays.stream(Group.values())
				.collect(Collectors.toMap(Function.identity(), group -> createContestantGroup(group, newLeague)));
		List<Contestant> contestantList = contestantRepository.saveAll(
				Arrays.stream(Team.values())
						.map(team -> createContestant(team, groupsMap, newLeague))
						.collect(Collectors.toList()));
		List<Game> addedGames = addAllGames(contestantList, newLeague);
		setLeagueStartDate(newLeague, addedGames);
		uefaEuro2020SettingRenderer.addDefaultLeagueSettings(newLeague);
	}

	private List<Game> addAllGames(List<Contestant> contestants, League league) {
		Map<String, GameDto> gameDtos = readCsv("csv/uefa-euro-2020.csv").stream()
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

	private List<CSVRecord> readCsv(String path) {
		try {
			Reader in = new InputStreamReader(new ClassPathResource(path).getInputStream());
			return Lists.newArrayList(CSVFormat.RFC4180.withHeader().parse(in));
		} catch (FileNotFoundException e) {
			throw new TemplateException("CSV file for FIFA World Cup 2018 not found", e);
		} catch (IOException e) {
			throw new TemplateException("Something went wrong during the parsing of the csv file", e);
		}
	}

	private SoccerCupStages getStage(String round) {
		if ("Round of 16".equals(round)) {
			return SoccerCupStages.EIGHTH_FINALS;
		} else if ("Quarter Finals".equals(round)) {
			return SoccerCupStages.QUARTER_FINALS;
		} else if ("Semi Finals".equals(round)) {
			return SoccerCupStages.SEMI_FINALS;
		} else if ("Final".equals(round)) {
			return SoccerCupStages.FINALS;
		}
		return SoccerCupStages.GROUP_PHASE;
	}

	private ContestantGroup createContestantGroup(Group group, League league) {
		ContestantGroup contestantGroup = new ContestantGroup();
		contestantGroup.setName(group.groupName);
		contestantGroup.setLeague(league);
		return contestantGroupRepository.saveAndFlush(contestantGroup);
	}

	private Contestant createContestant(Team team, Map<Group, ContestantGroup> groupsMap, League league) {
		Contestant contestant = new Contestant();
		contestant.setName(team.name);
		contestant.setIcon_path(team.iconPath);
		contestant.setLeague(league);
		contestant.setPower_index(team.powerIndex);
		team.group.map(groupsMap::get).ifPresent(contestant::setContestant_group);
		return contestant;
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

	/**
	 * Power index according to https://thepowerrank.com/world-football-soccer/
	 */
	public enum Team {

		AUSTRIA("Austria", Group.GROUP_C, 94, "images/icons/country_icons/austria.png"),
		BELGIUM("Belgium", Group.GROUP_B, 213, "images/icons/country_icons/belgium.png"),
		CROATIA("Croatia", Group.GROUP_D, 144, "images/icons/country_icons/croatia.png"),
		CZECH_REPUBLIC("Czech Republic", Group.GROUP_D, 60, "images/icons/country_icons/czech_republic.png"),
		DENMARK("Denmark", Group.GROUP_B, 157, "images/icons/country_icons/denmark.png"),
		ENGLAND("England", Group.GROUP_D, 163, "images/icons/country_icons/england.png"),
		FINLAND("Finland", Group.GROUP_B, 49, "images/icons/country_icons/finland.png"),
		FRANCE("France", Group.GROUP_F, 219, "images/icons/country_icons/france.png"),
		GERMANY("Germany", Group.GROUP_F, 183, "images/icons/country_icons/germany.png"),
		ITALY("Italy", Group.GROUP_A, 156, "images/icons/country_icons/italy.png"),
		NETHERLANDS("Netherlands", Group.GROUP_C, 156, "images/icons/country_icons/netherlands.png"),
		POLAND("Poland", Group.GROUP_E, 133, "images/icons/country_icons/poland.png"),
		PORTUGAL("Portugal", Group.GROUP_F, 186, "images/icons/country_icons/portugal.png"),
		RUSSIA("Russia", Group.GROUP_B, 115, "images/icons/country_icons/russia.png"),
		SPAIN("Spain", Group.GROUP_E, 207, "images/icons/country_icons/spain.png"),
		SWEDEN("Sweden", Group.GROUP_E, 144, "images/icons/country_icons/sweden.png"),
		SWITZERLAND("Switzerland", Group.GROUP_A, 154, "images/icons/country_icons/switzerland.png"),
		TURKEY("Turkey", Group.GROUP_A, 106, "images/icons/country_icons/switzerland.png"),
		UKRAINE("Ukraine", Group.GROUP_C, 102, "images/icons/country_icons/ukraine.png"),
		WALES("Wales", Group.GROUP_A, 121, "images/icons/country_icons/wales.png");

		private String name;
		private Optional<Group> group = Optional.empty();
		private Integer powerIndex;
		private String iconPath;

		Team(String name, Group group, Integer powerIndex, String iconPath) {
			this.name = name;
			this.group = Optional.of(group);
			this.powerIndex = powerIndex;
			this.iconPath = iconPath;
		}

	}

	public enum Group {
		GROUP_A("Group A"),
		GROUP_B("Group B"),
		GROUP_C("Group C"),
		GROUP_D("Group D"),
		GROUP_E("Group E"),
		GROUP_F("Group F");

		private String groupName;

		Group(String groupName) {
			this.groupName = groupName;
		}

		public String getGroupName() {
			return groupName;
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

package com.jeno.fantasyleague.data.service.leaguetemplates.worldcup2018;

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

import com.google.common.collect.Lists;
import com.jeno.fantasyleague.data.repository.ContestantGroupRepository;
import com.jeno.fantasyleague.data.repository.ContestantRepository;
import com.jeno.fantasyleague.data.repository.GameRepository;
import com.jeno.fantasyleague.data.repository.LeagueRepository;
import com.jeno.fantasyleague.data.service.leaguetemplates.TemplateException;
import com.jeno.fantasyleague.model.Contestant;
import com.jeno.fantasyleague.model.ContestantGroup;
import com.jeno.fantasyleague.model.Game;
import com.jeno.fantasyleague.model.League;
import com.jeno.fantasyleague.model.User;
import com.jeno.fantasyleague.util.DateUtil;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(rollbackFor = Exception.class)
public class FifaWorldCup2018Initializer {

	public static final String ID = "Id";
	public static final String ROUND_NUMBER = "Round Number";
	public static final String DATE = "Date";
	public static final String LOCATION = "Location";
	public static final String HOME_TEAM = "Home Team";
	public static final String AWAY_TEAM = "Away Team";
	public static final String NEXT_GAME = "Next Game";

	@Autowired
	private FifaWorldCup2018SettingRenderer fifaWorldCup2018SettingRenderer;
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
		fifaWorldCup2018SettingRenderer.addDefaultLeagueSettings(newLeague);
	}

	private List<Game> addAllGames(List<Contestant> contestants, League league) {
		Map<String, GameDto> gameDtos = readCsv("csv/fifa-world-cup-2018-games.csv").stream()
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

	private FifaWorldCup2018Stages getStage(String round) {
		if ("Round of 16".equals(round)) {
			return FifaWorldCup2018Stages.EIGHTH_FINALS;
		} else if ("Quarter Finals".equals(round)) {
			return FifaWorldCup2018Stages.QUARTER_FINALS;
		} else if ("Semi Finals".equals(round)) {
			return FifaWorldCup2018Stages.SEMI_FINALS;
		} else if ("Finals".equals(round)) {
			return FifaWorldCup2018Stages.FINALS;
		}
		return FifaWorldCup2018Stages.GROUP_PHASE;
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

	public enum Team {

		ARGENTINA("Argentina", Group.GROUP_D, 69, "images/icons/country_icons/argentina.png"),
		AUSTRALIA("Australia", Group.GROUP_C, 27, "images/icons/country_icons/australia.png"),
		BELGIUM("Belgium", Group.GROUP_G, 72, "images/icons/country_icons/belgium.png"),
		BRAZIL("Brazil", Group.GROUP_E, 89, "images/icons/country_icons/brazil.png"),
		COLOMBIA("Colombia", Group.GROUP_H, 56, "images/icons/country_icons/colombia.png"),
		COSTA_RICA("Costa Rica", Group.GROUP_E, 28, "images/icons/country_icons/costa_rica.png"),
		CROATIA("Croatia", Group.GROUP_D, 64, "images/icons/country_icons/croatia.png"),
		DENMARK("Denmark", Group.GROUP_C, 56, "images/icons/country_icons/denmark.png"),
		EGYPT("Egypt", Group.GROUP_A, 30, "images/icons/country_icons/egypt.png"),
		ENGLAND("England", Group.GROUP_G, 74, "images/icons/country_icons/england.png"),
		FRANCE("France", Group.GROUP_C, 84, "images/icons/country_icons/france.png"),
		GERMANY("Germany", Group.GROUP_F, 82, "images/icons/country_icons/germany.png"),
		ICELAND("Iceland", Group.GROUP_D, 33, "images/icons/country_icons/iceland.png"),
		IRAN("Iran", Group.GROUP_B, 23, "images/icons/country_icons/iran.png"),
		JAPAN("Japan", Group.GROUP_H, 37, "images/icons/country_icons/japan.png"),
		KOREA_REPUBLIC("Korea Republic", Group.GROUP_F, 30, "images/icons/country_icons/korea_republic.png"),
		MOROCCO("Morocco", Group.GROUP_B, 42, "images/icons/country_icons/morocco.png"),
		MEXICO("Mexico", Group.GROUP_F, 45, "images/icons/country_icons/mexico.png"),
		NIGERIA("Nigeria", Group.GROUP_D, 36, "images/icons/country_icons/nigeria.png"),
		PANAMA("Panama", Group.GROUP_G, 12, "images/icons/country_icons/panama.png"),
		PERU("Peru", Group.GROUP_C, 26, "images/icons/country_icons/peru.png"),
		POLAND("Poland", Group.GROUP_H, 47, "images/icons/country_icons/poland.png"),
		PORTUGAL("Portugal", Group.GROUP_B, 63, "images/icons/country_icons/portugal.png"),
		RUSSIA("Russia", Group.GROUP_A, 51, "images/icons/country_icons/russia.png"),
		SAUDI_ARABIA("Saudi Arabia", Group.GROUP_A, 21, "images/icons/country_icons/saudi_arabia.png"),
		SENEGAL("Senegal", Group.GROUP_H, 43, "images/icons/country_icons/senegal.png"),
		SERBIA("Serbia", Group.GROUP_E, 46, "images/icons/country_icons/serbia.png"),
		SPAIN("Spain", Group.GROUP_B, 100, "images/icons/country_icons/spain.png"),
		SWEDEN("Sweden", Group.GROUP_F, 40, "images/icons/country_icons/sweden.png"),
		SWITZERLAND("Switzerland", Group.GROUP_E, 60, "images/icons/country_icons/switzerland.png"),
		TUNISIA("Tunisia", Group.GROUP_G, 31, "images/icons/country_icons/tunisia.png"),
		URUGUAY("Uruguay", Group.GROUP_A, 51, "images/icons/country_icons/uruguay.png");

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
		GROUP_F("Group F"),
		GROUP_G("Group G"),
		GROUP_H("Group H");

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

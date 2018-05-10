package com.jeno.fantasyleague.data.service.leaguetemplates.worldcup2018;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
		List<Game> addedGames = addGames(contestantList, newLeague);
		Optional<LocalDateTime> earliestGameDate = addedGames.stream()
				.map(Game::getGame_date_time)
				.min(LocalDateTime::compareTo);
		if (earliestGameDate.isPresent()) {
			newLeague.setLeague_starting_date(earliestGameDate.get());
			leagueRepository.saveAndFlush(newLeague);
		}
		fifaWorldCup2018SettingRenderer.addDefaultLeagueSettings(newLeague);
	}

	private List<Game> addGames(List<Contestant> contestants, League league) {
		try {
			Reader in = new FileReader(new ClassPathResource("csv/fifa-world-cup-2018-RomanceStandardTime.csv").getFile());
			List<Game> games = Lists.newArrayList(CSVFormat.RFC4180.withHeader().parse(in)).stream()
					.map(record -> createGame(record, contestants, league))
					.collect(Collectors.toList());
			return gameRepository.saveAll(games);
		} catch (FileNotFoundException e) {
			throw new TemplateException("CSV file for FIFA World Cup 2018 not found", e);
		} catch (IOException e) {
			throw new TemplateException("Something went wron during the parsing of the csv file", e);
		}
	}

	private Game createGame(CSVRecord record, List<Contestant> contestants, League league) {
		String round = record.get("Round Number");
		String date = record.get("Date");
		String location = record.get("Location");
		String home_team = record.get("Home Team");
		String away_team = record.get("Away Team");

		Contestant homeTeam = contestants.stream()
				.filter(c -> c.getName().equals(home_team))
				.findFirst()
				.orElseThrow(() -> new TemplateException("Wrong team name for " + home_team));

		Contestant awayTeam = contestants.stream()
				.filter(c -> c.getName().equals(away_team))
				.findFirst()
				.orElseThrow(() -> new TemplateException("Wrong team name for " + away_team));

		Game game = new Game();
		game.setRound(round);
		game.setStage(FifaWorldCup2018Stages.GROUP_PHASE.name());
		game.setLocation(location);
		game.setGame_date_time(LocalDateTime.parse(date, DateUtil.DATE_TIME_FORMATTER));
		game.setHome_team(homeTeam);
		game.setAway_team(awayTeam);
		game.setLeague(league);
		return game;
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
//		WINNER_GROUP_A("Winner Group A", "images/icons/unknown.png"),
//		RUNNER_UP_GROUP_A("Runner-up Group A", "images/icons/unknown.png"),
//		WINNER_GROUP_B("Winner Group B", "images/icons/unknown.png"),
//		RUNNER_UP_GROUP_B("Runner-up Group B", "images/icons/unknown.png"),
//		WINNER_GROUP_C("Winner Group C", "images/icons/unknown.png"),
//		RUNNER_UP_GROUP_C("Runner-up Group C", "images/icons/unknown.png"),
//		WINNER_GROUP_D("Winner Group D", "images/icons/unknown.png"),
//		RUNNER_UP_GROUP_D("Runner-up Group D", "images/icons/unknown.png"),
//		WINNER_GROUP_E("Winner Group E", "images/icons/unknown.png"),
//		RUNNER_UP_GROUP_E("Runner-up Group E", "images/icons/unknown.png"),
//		WINNER_GROUP_F("Winner Group F", "images/icons/unknown.png"),
//		RUNNER_UP_GROUP_F("Runner-up Group F", "images/icons/unknown.png"),
//		WINNER_GROUP_G("Winner Group G", "images/icons/unknown.png"),
//		RUNNER_UP_GROUP_G("Runner-up Group G", "images/icons/unknown.png"),
//		WINNER_GROUP_H("Winner Group H", "images/icons/unknown.png"),
//		RUNNER_UP_GROUP_H("Runner-up Group H", "images/icons/unknown.png"),
//		TO_BE_ANNOUNCED("To be announced", "images/icons/unknown.png");

		private String name;
		private Optional<Group> group = Optional.empty();
		private Integer powerIndex;
		private String iconPath;

		Team(String name, String iconPath) {
			this.name = name;
			this.iconPath = iconPath;
		}

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
	}
}

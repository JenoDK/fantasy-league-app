package com.jeno.fantasyleague.data.service.leaguetemplates;

import com.google.common.collect.Lists;
import com.jeno.fantasyleague.data.repository.ContestantGroupRepository;
import com.jeno.fantasyleague.data.repository.ContestantRepository;
import com.jeno.fantasyleague.data.repository.GameRepository;
import com.jeno.fantasyleague.model.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FifaWorldCup2018Service implements LeagueTemplateService {

	private static final DateTimeFormatter CSV_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

	@Autowired
	private ContestantRepository contestantRepository;
	@Autowired
	private ContestantGroupRepository contestantGroupRepository;
	@Autowired
	private GameRepository gameRepository;

	@Override
	public void run(League newLeague, User user) throws TemplateException {
		Map<Group, ContestantGroup> groupsMap = Arrays.stream(Group.values())
				.collect(Collectors.toMap(Function.identity(), group -> createContestantGroup(group, newLeague)));
		List<Contestant> contestantList = contestantRepository.saveAll(
			Arrays.stream(Team.values())
				.map(team -> createContestant(team, groupsMap, newLeague))
				.collect(Collectors.toList()));
		addGames(contestantList, newLeague);
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
		game.setLocation(location);
		game.setGame_date_time(LocalDateTime.parse(date, CSV_DATE_TIME_FORMATTER));
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
		team.group.map(groupsMap::get).ifPresent(contestant::setContestant_group);
		return contestant;
	}

	public enum Team {

		ARGENTINA("Argentina", Group.GROUP_D, "images/icons/country_icons/argentina.png"),
		AUSTRALIA("Australia", Group.GROUP_C, "images/icons/country_icons/australia.png"),
		BELGIUM("Belgium", Group.GROUP_G, "images/icons/country_icons/belgium.png"),
		BRAZIL("Brazil", Group.GROUP_E, "images/icons/country_icons/brazil.png"),
		COLOMBIA("Colombia", Group.GROUP_H, "images/icons/country_icons/colombia.png"),
		COSTA_RICA("Costa Rica", Group.GROUP_E, "images/icons/country_icons/costa_rica.png"),
		CROATIA("Croatia", Group.GROUP_D, "images/icons/country_icons/croatia.png"),
		DENMARK("Denmark", Group.GROUP_C, "images/icons/country_icons/denmark.png"),
		EGYPT("Egypt", Group.GROUP_A, "images/icons/country_icons/egypt.png"),
		ENGLAND("England", Group.GROUP_G, "images/icons/country_icons/england.png"),
		FRANCE("France", Group.GROUP_C, "images/icons/country_icons/france.png"),
		GERMANY("Germany", Group.GROUP_F, "images/icons/country_icons/germany.png"),
		ICELAND("Iceland", Group.GROUP_D, "images/icons/country_icons/iceland.png"),
		IRAN("Iran", Group.GROUP_B, "images/icons/country_icons/iran.png"),
		JAPAN("Japan", Group.GROUP_H, "images/icons/country_icons/japan.png"),
		KOREA_REPUBLIC("Korea Republic", Group.GROUP_F, "images/icons/country_icons/korea_republic.png"),
		MOROCCO("Morocco", Group.GROUP_B, "images/icons/country_icons/morocco.png"),
		MEXICO("Mexico", Group.GROUP_F, "images/icons/country_icons/mexico.png"),
		NIGERIA("Nigeria", Group.GROUP_D, "images/icons/country_icons/nigeria.png"),
		PANAMA("Panama", Group.GROUP_G, "images/icons/country_icons/panama.png"),
		PERU("Peru", Group.GROUP_C, "images/icons/country_icons/peru.png"),
		POLAND("Poland", Group.GROUP_H, "images/icons/country_icons/poland.png"),
		PORTUGAL("Portugal", Group.GROUP_B, "images/icons/country_icons/portugal.png"),
		RUSSIA("Russia", Group.GROUP_A, "images/icons/country_icons/russia.png"),
		SAUDI_ARABIA("Saudi Arabia", Group.GROUP_A, "images/icons/country_icons/saudi_arabia.png"),
		SENEGAL("Senegal", Group.GROUP_H, "images/icons/country_icons/senegal.png"),
		SERBIA("Serbia", Group.GROUP_E, "images/icons/country_icons/serbia.png"),
		SPAIN("Spain", Group.GROUP_B, "images/icons/country_icons/spain.png"),
		SWEDEN("Sweden", Group.GROUP_F, "images/icons/country_icons/sweden.png"),
		SWITZERLAND("Switzerland", Group.GROUP_E, "images/icons/country_icons/switzerland.png"),
		TUNISIA("Tunisia", Group.GROUP_G, "images/icons/country_icons/tunisia.png"),
		URUGUAY("Uruguay", Group.GROUP_A, "images/icons/country_icons/uruguay.png");
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
		private String iconPath;

		Team(String name, String iconPath) {
			this.name = name;
			this.iconPath = iconPath;
		}

		Team(String name, Group group, String iconPath) {
			this.name = name;
			this.group = Optional.of(group);
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

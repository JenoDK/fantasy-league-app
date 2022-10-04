package com.jeno.fantasyleague.backend.data.service.leaguetemplates.fifaworld2022;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.math.Quantiles;
import com.jeno.fantasyleague.backend.data.service.leaguetemplates.FootballInitializer;

@Component
@Transactional(rollbackFor = Exception.class)
public class FifaWorldCup2022Initializer extends FootballInitializer {

	public static final FootballInitializer.Group GROUP_A = new FootballInitializer.Group("Group A", "A");
	public static final FootballInitializer.Group GROUP_B = new FootballInitializer.Group("Group B", "B");
	public static final FootballInitializer.Group GROUP_C = new FootballInitializer.Group("Group C", "C");
	public static final FootballInitializer.Group GROUP_D = new FootballInitializer.Group("Group D", "D");
	public static final FootballInitializer.Group GROUP_E = new FootballInitializer.Group("Group E", "E");
	public static final FootballInitializer.Group GROUP_F = new FootballInitializer.Group("Group F", "F");
	public static final FootballInitializer.Group GROUP_G = new FootballInitializer.Group("Group G", "G");
	public static final FootballInitializer.Group GROUP_H = new FootballInitializer.Group("Group H", "H");
	public static final Team[] TEAMS = {
			new Team("Qatar", GROUP_A, 14.0, 81.0, 401.0, "images/icons/country_icons/qatar.png"),
			new Team("Ecuador", GROUP_A, 7.0, 51.0, 251.0, "images/icons/country_icons/ecuador.png"),
			new Team("Senegal", GROUP_A, 5.75, 14.0, 101.0, "images/icons/country_icons/senegal.png"),
			new Team("Netherlands", GROUP_A, 1.42, 3.25, 14.0, "images/icons/country_icons/netherlands.png"),
			new Team("England", GROUP_B, 1.3, 2.4, 9.5, "images/icons/country_icons/england.png"),
			new Team("Iran", GROUP_B, 21.0, 151.0, 751.0, "images/icons/country_icons/iran.png"),
			new Team("USA", GROUP_B, 8.0, 41.0, 201.0, "images/icons/country_icons/usa.png"),
			new Team("Wales", GROUP_B, 6.5, 51.0, 251.0, "images/icons/country_icons/wales.png"),
			new Team("Argentina", GROUP_C, 1.4, 2.20, 7.5, "images/icons/country_icons/argentina.png"),
			new Team("Saudi Arabia", GROUP_C, 22.0, 201.0, 1001.0, "images/icons/country_icons/saudi_arabia.png"),
			new Team("Mexico", GROUP_C, 5.5, 31.0, 151.0, "images/icons/country_icons/mexico.png"),
			new Team("Poland", GROUP_C, 6.5, 41.0, 201.0, "images/icons/country_icons/poland.png"),
			new Team("France", GROUP_D, 1.47, 2.10, 7.5, "images/icons/country_icons/france.png"),
			new Team("Australia", GROUP_D, 19.0, 126.0, 751.0, "images/icons/country_icons/australia.png"),
			new Team("Denmark", GROUP_D, 3.5, 6.0, 34.0, "images/icons/country_icons/denmark.png"),
			new Team("Tunisia", GROUP_D, 13.0, 126.0, 751.0, "images/icons/country_icons/tunisia.png"),
			new Team("Spain", GROUP_E, 1.9, 2.40, 10.0, "images/icons/country_icons/spain.png"),
			new Team("Costa Rica", GROUP_E, 71.0, 151.0, 751.0, "images/icons/country_icons/costa_rica.png"),
			new Team("Germany", GROUP_E, 2.15, 2.75, 12.0, "images/icons/country_icons/germany.png"),
			new Team("Japan", GROUP_E, 12.0, 41.0, 201.0, "images/icons/country_icons/japan.png"),
			new Team("Belgium", GROUP_F, 1.72, 4.0, 18.0, "images/icons/country_icons/belgium.png"),
			new Team("Canada", GROUP_F, 13.0, 61.0, 301.0, "images/icons/country_icons/canada.png"),
			new Team("Morocco", GROUP_F, 10.0, 61.0, 301.0, "images/icons/country_icons/morocco.png"),
			new Team("Croatia", GROUP_F, 3.0, 7.0, 41.0, "images/icons/country_icons/croatia.png"),
			new Team("Brazil", GROUP_G, 1.25, 1.8, 5.0, "images/icons/country_icons/brazil.png"),
			new Team("Serbia", GROUP_G, 7.5, 10.0, 71.0, "images/icons/country_icons/serbia.png"),
			new Team("Switzerland", GROUP_G, 7.5, 14.0, 101.0, "images/icons/country_icons/switzerland.png"),
			new Team("Cameroon", GROUP_G, 31.0, 81.0, 401.0, "images/icons/country_icons/cameroon.png"),
			new Team("Portugal", GROUP_H,1.6,  3.5, 16.0, "images/icons/country_icons/portugal.png"),
			new Team("Ghana", GROUP_H, 14.0, 81.0, 401.0, "images/icons/country_icons/ghana.png"),
			new Team("Uruguay", GROUP_H, 3.1, 9.0, 51.0, "images/icons/country_icons/uruguay.png"),
			new Team("Korea Republic", GROUP_H, 14.0, 71.0, 351.0, "images/icons/country_icons/korea_republic.png"),
	};


	public static FootballInitializer.Group[] groups() {
		return new FootballInitializer.Group[] {
				GROUP_A,
				GROUP_B,
				GROUP_C,
				GROUP_D,
				GROUP_E,
				GROUP_F,
				GROUP_G,
				GROUP_H
		};
	}


	@Override
	protected String getCsvPath() {
		return "csv/fifa-world-cup-2022-UTC.csv";
	}

	@Override
	protected Team[] getTeams() {
		return TEAMS;
	}

	@Override
	protected Group[] getGroups() {
		return groups();
	}

	public static void main(String[] args) {
		List<Double> top4List = Arrays.stream(TEAMS)
				.map(Team::getUnibetScoreTop4)
				.collect(Collectors.toList());
		Double maxTop4 = Arrays.stream(TEAMS)
				.mapToDouble(Team::getUnibetScoreTop4)
				.max().getAsDouble();
		Double maxOutOfGroups = Arrays.stream(TEAMS)
				.mapToDouble(Team::getOddsToLeaveGroup)
				.max().getAsDouble();
		double median = Quantiles.median().compute(top4List);
		System.err.println("MEDIAN " + median);
		Arrays.stream(TEAMS)
				.sorted(Comparator.comparing(Team::getUnibetScoreWinner))
				.forEach(t -> {
					BigDecimal powerIndex = new BigDecimal(t.getPowerIndex(maxTop4,  maxOutOfGroups)).setScale(2, RoundingMode.HALF_UP);
					BigDecimal pricePerShare = powerIndex.divide(BigDecimal.valueOf(10)).multiply(BigDecimal.valueOf(100000)).setScale(0);
					System.err.println(t.getName() + " power index: " + powerIndex + " price per share: €" + pricePerShare);
				});
	}
}

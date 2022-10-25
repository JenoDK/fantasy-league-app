package com.jeno.fantasyleague.backend.data.service.leaguetemplates.fifaworld2022;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.ToDoubleFunction;
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
			new Team("Brazil", GROUP_G, 1.35, 1.80, 2.50, 3.25, 5.0, "images/icons/country_icons/brazil.png", 100.00),
			new Team("Argentina", GROUP_C, 1.55, 2.20, 2.85, 3.75, 7.5, "images/icons/country_icons/argentina.png", 95.25),
			new Team("France", GROUP_D, 1.55, 2.10, 2.85, 3.75, 7.5, "images/icons/country_icons/france.png", 93.15),
			new Team("England", GROUP_B, 1.45, 2.40, 3.00, 4.00, 9.5, "images/icons/country_icons/england.png", 92.45),
			new Team("Spain", GROUP_E, 1.65, 2.40, 3.00, 5.00, 10.0, "images/icons/country_icons/spain.png", 85.50),
			new Team("Germany", GROUP_E, 1.75, 2.75, 3.50, 5.00, 12.0, "images/icons/country_icons/germany.png", 82.10),
			new Team("Netherlands", GROUP_A, 1.65, 3.25, 4.00, 6.00, 14.0, "images/icons/country_icons/netherlands.png", 70.30),
			new Team("Portugal", GROUP_H,1.80, 3.50, 5.00,  7.00, 16.0, "images/icons/country_icons/portugal.png", 68.15),
			new Team("Belgium", GROUP_F, 1.90, 4.00, 4.50, 7.50, 18.0, "images/icons/country_icons/belgium.png", 64.80),
			new Team("Denmark", GROUP_D, 2.75, 6.00, 9.00, 12.00, 34.0, "images/icons/country_icons/denmark.png", 50.50),
			new Team("Croatia", GROUP_F, 3.25, 7.00,  9.00, 18.00, 41.0, "images/icons/country_icons/croatia.png", 42.25),
			new Team("Uruguay", GROUP_H, 3.00, 9.00, 12.00, 21.00, 51.0, "images/icons/country_icons/uruguay.png", 38.15),
			new Team("Serbia", GROUP_G, 4.50, 10.00, 26.00, 31.00, 71.0, "images/icons/country_icons/serbia.png", 30.45),
			new Team("Senegal", GROUP_A, 4.50, 14.00,  26.00, 36.00, 101.0, "images/icons/country_icons/senegal.png", 27.15),
			new Team("Switzerland", GROUP_G, 4.00, 14.00, 26.00, 36.00, 101.0, "images/icons/country_icons/switzerland.png", 25.35),
			new Team("Mexico", GROUP_C, 4.00, 31.00, 41.00, 51.00, 151.0, "images/icons/country_icons/mexico.png", 24.15),
			new Team("USA", GROUP_B, 4.75, 41.00, 31.00, 61.00, 201.0, "images/icons/country_icons/usa.png", 21.15),
			new Team("Japan", GROUP_E, 12.00, 41.00, 61.00, 71.00, 201.0, "images/icons/country_icons/japan.png", 18.95),
			new Team("Morocco", GROUP_F, 10.00, 41.00, 51.00, 71.00, 301.0, "images/icons/country_icons/morocco.png", 15.00),
			new Team("Poland", GROUP_C, 4.75, 41.00, 51.00, 71.00, 201.0, "images/icons/country_icons/poland.png", 17.65),
			new Team("Canada", GROUP_F, 18.00, 61.00, 101.00, 126.00, 301.0, "images/icons/country_icons/canada.png", 5.25),
			new Team("Ecuador", GROUP_A, 6.50, 51.00, 61.00, 101.00, 251.0, "images/icons/country_icons/ecuador.png", 12.15),
			new Team("Wales", GROUP_B, 4.50, 51.00, 61.00, 101.00, 251.0, "images/icons/country_icons/wales.png", 14.10),
			new Team("Korea Republic", GROUP_H, 14.0, 71.00, 101.00, 151.00, 351.0, "images/icons/country_icons/korea_republic.png", 7.05),
			new Team("Cameroon", GROUP_G, 26.00, 81.00, 126.00, 201.00, 401.0, "images/icons/country_icons/cameroon.png", 6.85),
			new Team("Ghana", GROUP_H, 14.00, 81.00,  101.00, 201.00, 401.0, "images/icons/country_icons/ghana.png", 5.75),
			new Team("Qatar", GROUP_A, 16.00, 81.00, 101.00, 201.00, 401.0, "images/icons/country_icons/qatar.png", 4.25),
			new Team("Australia", GROUP_D, 21.0, 126.00, 251.00, 301.00, 751.0, "images/icons/country_icons/australia.png", 7.45),
			new Team("Costa Rica", GROUP_E, 31.0, 151.00, 251.00, 301.00, 751.0, "images/icons/country_icons/costa_rica.png", 3.45),
			new Team("Iran", GROUP_B, 17.50, 151.00, 201.00, 301.00, 751.0, "images/icons/country_icons/iran.png", 5.00),
			new Team("Tunisia", GROUP_D, 18.00, 126.00, 151.00, 301.00, 751.0, "images/icons/country_icons/tunisia.png", 7.45),
			new Team("Saudi Arabia", GROUP_C, 41.00, 201.00, 301.00, 401.00, 1001.0, "images/icons/country_icons/saudi_arabia.png", 1.15),
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
		Double maxTop1 = getMax(Team::getOddsTop1, true);
		Double maxTop2 = getMax(Team::getOddsTop2, true);
		Double maxTop3 = getMax(Team::getOddsTop3, false);
		Double maxTop4 = getMax(Team::getOddsTop4, false);
		Double maxTop8 = getMax(Team::getOddsTop8, false);

		Double totalTop1 = getTotal(Team::getOddsTop1, false);
		Double totalTop2 = getTotal(Team::getOddsTop2, false);
		Double totalTop3 = getTotal(Team::getOddsTop3, false);
		Double totalTop4 = getTotal(Team::getOddsTop4, false);
		Double totalTop8 = getTotal(Team::getOddsTop8, false);

		Double medianTop1 = getMedian(Team::getOddsTop1, false);
		Double medianTop2 = getMedian(Team::getOddsTop2, false);
		Double medianTop3 = getMedian(Team::getOddsTop3, false);
		Double medianTop4 = getMedian(Team::getOddsTop4, false);
		Double medianTop8 = getMedian(Team::getOddsTop8, false);

		Arrays.stream(TEAMS)
				.sorted(Comparator.comparing(t -> t.getPowerIndex(maxTop1, maxTop2, maxTop3, maxTop4, maxTop8, medianTop1, medianTop2, medianTop3, medianTop4, medianTop8, totalTop1, totalTop2, totalTop3, totalTop4, totalTop8), Comparator.reverseOrder()))
				.forEach(t -> {
//					BigDecimal powerIndex = new BigDecimal(t.getPowerIndex(maxTop1, maxTop2, maxTop3, maxTop4, maxTop8, medianTop1, medianTop2, medianTop3, medianTop4, medianTop8, totalTop1, totalTop2, totalTop3, totalTop4, totalTop8));
//					BigDecimal powerIndexRounded = powerIndex.setScale(0, RoundingMode.DOWN);
					BigDecimal powerIndexRounded = BigDecimal.valueOf(t.getPowerIndex());
					BigDecimal pricePerShare = powerIndexRounded.multiply(BigDecimal.valueOf(10000));
					System.err.println(t.getName() + " power index: " + powerIndexRounded + " price per share: €" + pricePerShare);
				});
	}

	private static Double getTotal(ToDoubleFunction<Team> function, boolean applySqrt) {
		return Arrays.stream(TEAMS)
				.mapToDouble(t -> {
					if (applySqrt) {
						return BigDecimal.valueOf(function.applyAsDouble(t)).sqrt(MathContext.DECIMAL32).doubleValue();
					} else {
						return function.applyAsDouble(t);
					}
				})
				.sum();
	}

	private static Double getMedian(ToDoubleFunction<Team> function, boolean applySqrt) {
		List<Double> indexList = Arrays.stream(TEAMS)
				.mapToDouble(t -> {
					if (applySqrt) {
						return BigDecimal.valueOf(function.applyAsDouble(t)).sqrt(MathContext.DECIMAL32).doubleValue();
					} else {
						return function.applyAsDouble(t);
					}
				})
				.boxed()
				.collect(Collectors.toList());
		return Quantiles.median().compute(indexList);
	}

	private static Double getMax(ToDoubleFunction<Team> function, boolean applySqrt) {
		return Arrays.stream(TEAMS)
				.mapToDouble(t -> {
					if (applySqrt) {
						return BigDecimal.valueOf(function.applyAsDouble(t)).sqrt(MathContext.DECIMAL32).doubleValue();
					} else {
						return function.applyAsDouble(t);
					}
				})
				.max().getAsDouble();
	}
}

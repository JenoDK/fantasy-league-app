package com.jeno.fantasyleague.backend.data.service.leaguetemplates.fifaworld2026;

import com.jeno.fantasyleague.backend.data.service.leaguetemplates.FootballInitializer;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(rollbackFor = Exception.class)
public class FifaWorldCup2026Initializer extends FootballInitializer {

	public static final Group GROUP_A = new Group("Group A", "A");
	public static final Group GROUP_B = new Group("Group B", "B");
	public static final Group GROUP_C = new Group("Group C", "C");
	public static final Group GROUP_D = new Group("Group D", "D");
	public static final Group GROUP_E = new Group("Group E", "E");
	public static final Group GROUP_F = new Group("Group F", "F");
	public static final Group GROUP_G = new Group("Group G", "G");
	public static final Group GROUP_H = new Group("Group H", "H");
	public static final Group GROUP_I = new Group("Group I", "I");
	public static final Group GROUP_J = new Group("Group J", "J");
	public static final Group GROUP_K = new Group("Group K", "K");
	public static final Group GROUP_L = new Group("Group L", "L");

	public static Group[] groups() {
		return new Group[] {
				GROUP_A, GROUP_B, GROUP_C, GROUP_D, GROUP_E, GROUP_F,
				GROUP_G, GROUP_H, GROUP_I, GROUP_J, GROUP_K, GROUP_L
		};
	}

	@Override
	protected String getCsvPath() {
		return "csv/fifa-world-cup-2026-UTC.csv";
	}

	/**
	 * Power index (0-100, higher = stronger) drives the Stocks tab share price. Each value is a 50/50
	 * blend of two absolute sub-scores (fixed anchors, NOT min/max-stretched across this field):
	 *   - Elo:  (elo - 1450) / (2100 - 1450) * 100, clamped 0-100
	 *           Source: World Football Elo Ratings (eloratings.net / Wikipedia), ~1 June 2026.
	 *   - Odds: (ln(1/decimalOdds) - ln(0.001)) / (ln(0.18) - ln(0.001)) * 100, clamped 0-100
	 *           Source: outright-winner odds for all 48 teams (Yahoo Sports), ~June 2026.
	 * Final value clamped to [1, 100] (so no team's share is free). A few mid/low Elo values
	 * (Czechia, Panama, Congo DR) are estimates on the same scale. Recompute via /tmp/power_index.py.
	 */
	@Override
	protected Team[] getTeams() {
		return new Team[]{
				// Group A
				new Team("Mexico", GROUP_A, 58.2, "images/icons/country_icons/mexico.png"),
				new Team("South Africa", GROUP_A, 5.2, "images/icons/country_icons/south_africa.png"),
				new Team("Korea Republic", GROUP_A, 36.8, "images/icons/country_icons/korea_republic.png"),
				new Team("Czechia", GROUP_A, 37.8, "images/icons/country_icons/czech_republic.png"),
				// Group B
				new Team("Canada", GROUP_B, 44.6, "images/icons/country_icons/canada.png"),
				new Team("Bosnia and Herzegovina", GROUP_B, 24.2, "images/icons/country_icons/bosnia_and_herzegovina.png"),
				new Team("Qatar", GROUP_B, 1.0, "images/icons/country_icons/qatar.png"),
				new Team("Switzerland", GROUP_B, 60.2, "images/icons/country_icons/switzerland.png"),
				// Group C
				new Team("Brazil", GROUP_C, 86.7, "images/icons/country_icons/brazil.png"),
				new Team("Morocco", GROUP_C, 59.4, "images/icons/country_icons/morocco.png"),
				new Team("Haiti", GROUP_C, 6.3, "images/icons/country_icons/haiti.png"),
				new Team("Scotland", GROUP_C, 37.9, "images/icons/country_icons/scotland.png"),
				// Group D
				new Team("USA", GROUP_D, 52.5, "images/icons/country_icons/usa.png"),
				new Team("Paraguay", GROUP_D, 47.7, "images/icons/country_icons/paraguay.png"),
				new Team("Australia", GROUP_D, 31.7, "images/icons/country_icons/australia.png"),
				new Team("Türkiye", GROUP_D, 61.1, "images/icons/country_icons/turkey.png"),
				// Group E
				new Team("Germany", GROUP_E, 77.0, "images/icons/country_icons/germany.png"),
				new Team("Curaçao", GROUP_E, 1.0, "images/icons/country_icons/curacao.png"),
				new Team("Côte d'Ivoire", GROUP_E, 32.8, "images/icons/country_icons/cote_divoire.png"),
				new Team("Ecuador", GROUP_E, 63.3, "images/icons/country_icons/ecuador.png"),
				// Group F
				new Team("Netherlands", GROUP_F, 76.5, "images/icons/country_icons/netherlands.png"),
				new Team("Japan", GROUP_F, 63.7, "images/icons/country_icons/japan.png"),
				new Team("Sweden", GROUP_F, 46.3, "images/icons/country_icons/sweden.png"),
				new Team("Tunisia", GROUP_F, 20.7, "images/icons/country_icons/tunisia.png"),
				// Group G
				new Team("Belgium", GROUP_G, 64.6, "images/icons/country_icons/belgium.png"),
				new Team("Egypt", GROUP_G, 32.5, "images/icons/country_icons/egypt.png"),
				new Team("IR Iran", GROUP_G, 30.8, "images/icons/country_icons/iran.png"),
				new Team("New Zealand", GROUP_G, 10.4, "images/icons/country_icons/new_zealand.png"),
				// Group H
				new Team("Spain", GROUP_H, 100.0, "images/icons/country_icons/spain.png"),
				new Team("Cabo Verde", GROUP_H, 9.7, "images/icons/country_icons/cabo_verde.png"),
				new Team("Saudi Arabia", GROUP_H, 8.9, "images/icons/country_icons/saudi_arabia.png"),
				new Team("Uruguay", GROUP_H, 62.7, "images/icons/country_icons/uruguay.png"),
				// Group I
				new Team("France", GROUP_I, 98.5, "images/icons/country_icons/france.png"),
				new Team("Senegal", GROUP_I, 58.0, "images/icons/country_icons/senegal.png"),
				new Team("Iraq", GROUP_I, 12.2, "images/icons/country_icons/iraq.png"),
				new Team("Norway", GROUP_I, 71.1, "images/icons/country_icons/norway.png"),
				// Group J
				new Team("Argentina", GROUP_J, 95.4, "images/icons/country_icons/argentina.png"),
				new Team("Algeria", GROUP_J, 35.8, "images/icons/country_icons/algeria.png"),
				new Team("Austria", GROUP_J, 51.3, "images/icons/country_icons/austria.png"),
				new Team("Jordan", GROUP_J, 18.1, "images/icons/country_icons/jordan.png"),
				// Group K
				new Team("Portugal", GROUP_K, 85.4, "images/icons/country_icons/portugal.png"),
				new Team("Congo DR", GROUP_K, 12.0, "images/icons/country_icons/congo_dr.png"),
				new Team("Uzbekistan", GROUP_K, 20.6, "images/icons/country_icons/uzbekistan.png"),
				new Team("Colombia", GROUP_K, 72.5, "images/icons/country_icons/colombia.png"),
				// Group L
				new Team("England", GROUP_L, 91.0, "images/icons/country_icons/england.png"),
				new Team("Croatia", GROUP_L, 62.9, "images/icons/country_icons/croatia.png"),
				new Team("Ghana", GROUP_L, 17.4, "images/icons/country_icons/ghana.png"),
				new Team("Panama", GROUP_L, 16.9, "images/icons/country_icons/panama.png"),
		};
	}

	@Override
	protected Group[] getGroups() {
		return groups();
	}

}

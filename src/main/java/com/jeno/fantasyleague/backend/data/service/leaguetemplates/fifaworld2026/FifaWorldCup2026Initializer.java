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

	// TODO power index: 50.0 is a neutral placeholder for every team. Replace with real
	// values (higher = stronger; range used elsewhere is 0-100). The Team name MUST stay
	// exactly equal to the CSV spelling, otherwise games won't link to the contestant.
	private static final double PI = 50.0;

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

	@Override
	protected Team[] getTeams() {
		return new Team[]{
				// Group A
				new Team("Mexico", GROUP_A, PI, "images/icons/country_icons/mexico.png"),
				new Team("South Africa", GROUP_A, PI, "images/icons/country_icons/south_africa.png"),
				new Team("Korea Republic", GROUP_A, PI, "images/icons/country_icons/korea_republic.png"),
				new Team("Czechia", GROUP_A, PI, "images/icons/country_icons/czech_republic.png"),
				// Group B
				new Team("Canada", GROUP_B, PI, "images/icons/country_icons/canada.png"),
				new Team("Bosnia and Herzegovina", GROUP_B, PI, "images/icons/country_icons/bosnia_and_herzegovina.png"),
				new Team("Qatar", GROUP_B, PI, "images/icons/country_icons/qatar.png"),
				new Team("Switzerland", GROUP_B, PI, "images/icons/country_icons/switzerland.png"),
				// Group C
				new Team("Brazil", GROUP_C, PI, "images/icons/country_icons/brazil.png"),
				new Team("Morocco", GROUP_C, PI, "images/icons/country_icons/morocco.png"),
				new Team("Haiti", GROUP_C, PI, "images/icons/country_icons/haiti.png"),
				new Team("Scotland", GROUP_C, PI, "images/icons/country_icons/scotland.png"),
				// Group D
				new Team("USA", GROUP_D, PI, "images/icons/country_icons/usa.png"),
				new Team("Paraguay", GROUP_D, PI, "images/icons/country_icons/paraguay.png"),
				new Team("Australia", GROUP_D, PI, "images/icons/country_icons/australia.png"),
				new Team("Türkiye", GROUP_D, PI, "images/icons/country_icons/turkey.png"),
				// Group E
				new Team("Germany", GROUP_E, PI, "images/icons/country_icons/germany.png"),
				new Team("Curaçao", GROUP_E, PI, "images/icons/country_icons/curacao.png"),
				new Team("Côte d'Ivoire", GROUP_E, PI, "images/icons/country_icons/cote_divoire.png"),
				new Team("Ecuador", GROUP_E, PI, "images/icons/country_icons/ecuador.png"),
				// Group F
				new Team("Netherlands", GROUP_F, PI, "images/icons/country_icons/netherlands.png"),
				new Team("Japan", GROUP_F, PI, "images/icons/country_icons/japan.png"),
				new Team("Sweden", GROUP_F, PI, "images/icons/country_icons/sweden.png"),
				new Team("Tunisia", GROUP_F, PI, "images/icons/country_icons/tunisia.png"),
				// Group G
				new Team("Belgium", GROUP_G, PI, "images/icons/country_icons/belgium.png"),
				new Team("Egypt", GROUP_G, PI, "images/icons/country_icons/egypt.png"),
				new Team("IR Iran", GROUP_G, PI, "images/icons/country_icons/iran.png"),
				new Team("New Zealand", GROUP_G, PI, "images/icons/country_icons/new_zealand.png"),
				// Group H
				new Team("Spain", GROUP_H, PI, "images/icons/country_icons/spain.png"),
				new Team("Cabo Verde", GROUP_H, PI, "images/icons/country_icons/cabo_verde.png"),
				new Team("Saudi Arabia", GROUP_H, PI, "images/icons/country_icons/saudi_arabia.png"),
				new Team("Uruguay", GROUP_H, PI, "images/icons/country_icons/uruguay.png"),
				// Group I
				new Team("France", GROUP_I, PI, "images/icons/country_icons/france.png"),
				new Team("Senegal", GROUP_I, PI, "images/icons/country_icons/senegal.png"),
				new Team("Iraq", GROUP_I, PI, "images/icons/country_icons/iraq.png"),
				new Team("Norway", GROUP_I, PI, "images/icons/country_icons/norway.png"),
				// Group J
				new Team("Argentina", GROUP_J, PI, "images/icons/country_icons/argentina.png"),
				new Team("Algeria", GROUP_J, PI, "images/icons/country_icons/algeria.png"),
				new Team("Austria", GROUP_J, PI, "images/icons/country_icons/austria.png"),
				new Team("Jordan", GROUP_J, PI, "images/icons/country_icons/jordan.png"),
				// Group K
				new Team("Portugal", GROUP_K, PI, "images/icons/country_icons/portugal.png"),
				new Team("Congo DR", GROUP_K, PI, "images/icons/country_icons/congo_dr.png"),
				new Team("Uzbekistan", GROUP_K, PI, "images/icons/country_icons/uzbekistan.png"),
				new Team("Colombia", GROUP_K, PI, "images/icons/country_icons/colombia.png"),
				// Group L
				new Team("England", GROUP_L, PI, "images/icons/country_icons/england.png"),
				new Team("Croatia", GROUP_L, PI, "images/icons/country_icons/croatia.png"),
				new Team("Ghana", GROUP_L, PI, "images/icons/country_icons/ghana.png"),
				new Team("Panama", GROUP_L, PI, "images/icons/country_icons/panama.png"),
		};
	}

	@Override
	protected Group[] getGroups() {
		return groups();
	}

}

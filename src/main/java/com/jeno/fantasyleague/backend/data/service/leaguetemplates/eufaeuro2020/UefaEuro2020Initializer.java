package com.jeno.fantasyleague.backend.data.service.leaguetemplates.eufaeuro2020;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.jeno.fantasyleague.backend.data.service.leaguetemplates.FootballInitializer;

@Component
@Transactional(rollbackFor = Exception.class)
public class UefaEuro2020Initializer extends FootballInitializer {

	public static final FootballInitializer.Group GROUP_A = new FootballInitializer.Group("Group A", "A");
	public static final FootballInitializer.Group GROUP_B = new FootballInitializer.Group("Group B", "B");
	public static final FootballInitializer.Group GROUP_C = new FootballInitializer.Group("Group C", "C");
	public static final FootballInitializer.Group GROUP_D = new FootballInitializer.Group("Group D", "D");
	public static final FootballInitializer.Group GROUP_E = new FootballInitializer.Group("Group E", "E");
	public static final FootballInitializer.Group GROUP_F = new FootballInitializer.Group("Group F", "F");


	@Override
	protected String getCsvPath() {
		return "csv/uefa-euro-2020.csv";
	}

	@Override
	protected FootballInitializer.Team[] getTeams() {
		return new FootballInitializer.Team[]{
				new FootballInitializer.Team("France", GROUP_D, 100.0, "images/icons/country_icons/france.png"),
				new FootballInitializer.Team("England", GROUP_D, 92.31, "images/icons/country_icons/england.png"),
				new FootballInitializer.Team("Belgium", GROUP_B, 85.71, "images/icons/country_icons/belgium.png"),
				new FootballInitializer.Team("Portugal", GROUP_F, 70.59, "images/icons/country_icons/portugal.png"),
				new FootballInitializer.Team("Germany", GROUP_F, 60.0, "images/icons/country_icons/germany.png"),
				new FootballInitializer.Team("Spain", GROUP_E, 60.0, "images/icons/country_icons/spain.png"),
				new FootballInitializer.Team("Italy", GROUP_A, 50.0, "images/icons/country_icons/italy.png"),
				new FootballInitializer.Team("Netherlands", GROUP_C, 42.86, "images/icons/country_icons/netherlands.png"),
				new FootballInitializer.Team("Denmark", GROUP_B, 23.08, "images/icons/country_icons/denmark.png"),
				new FootballInitializer.Team("Croatia", GROUP_D, 14.63, "images/icons/country_icons/croatia.png"),
				new FootballInitializer.Team("Turkey", GROUP_A, 10.71, "images/icons/country_icons/turkey.png"),
				new FootballInitializer.Team("Switzerland", GROUP_A, 8.96, "images/icons/country_icons/switzerland.png"),
				new FootballInitializer.Team("Poland", GROUP_E, 8.45, "images/icons/country_icons/poland.png"),
				new FootballInitializer.Team("Russia", GROUP_B, 6.59, "images/icons/country_icons/russia.png"),
				new FootballInitializer.Team("Sweden", GROUP_E, 6.59, "images/icons/country_icons/sweden.png"),
				new FootballInitializer.Team("Ukraine", GROUP_C, 6.59, "images/icons/country_icons/ukraine.png"),
				new FootballInitializer.Team("Czech Republic", GROUP_D, 5.41, "images/icons/country_icons/czech_republic.png"),
				new FootballInitializer.Team("Austria", GROUP_C, 4.26, "images/icons/country_icons/austria.png"),
				new FootballInitializer.Team("Scotland", GROUP_D, 3.31, "images/icons/country_icons/scotland.png"),
				new FootballInitializer.Team("Wales", GROUP_A, 2.99, "images/icons/country_icons/wales.png"),
				new FootballInitializer.Team("Slovakia", GROUP_E, 1.71, "images/icons/country_icons/slovakia.png"),
				new FootballInitializer.Team("Finland", GROUP_B, 1.33, "images/icons/country_icons/finland.png"),
				new FootballInitializer.Team("North Macedonia", GROUP_C, 1.33, "images/icons/country_icons/north_macedonia.png"),
				new FootballInitializer.Team("Hungary", GROUP_F, 0.86, "images/icons/country_icons/hungary.png")
		};
	}

	@Override
	protected FootballInitializer.Group[] getGroups() {
		return new FootballInitializer.Group[] {
				GROUP_A,
				GROUP_B,
				GROUP_C,
				GROUP_D,
				GROUP_E,
				GROUP_F
		};
	}

}

package com.jeno.fantasyleague.backend.data.service.leaguetemplates.eufaeuro2024;

import com.jeno.fantasyleague.backend.data.service.leaguetemplates.FootballInitializer;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(rollbackFor = Exception.class)
public class UefaEuro2024Initializer extends FootballInitializer {

	public static final Group GROUP_A = new Group("Group A", "A");
	public static final Group GROUP_B = new Group("Group B", "B");
	public static final Group GROUP_C = new Group("Group C", "C");
	public static final Group GROUP_D = new Group("Group D", "D");
	public static final Group GROUP_E = new Group("Group E", "E");
	public static final Group GROUP_F = new Group("Group F", "F");


	@Override
	protected String getCsvPath() {
		return "csv/uefa-euro-2024-UTC.csv";
	}

	@Override
	protected Team[] getTeams() {
		return new Team[]{
				new Team("England", GROUP_C, 100.0, "images/icons/country_icons/england.png"),
				new Team("Spain", GROUP_B, 85.14962612926846, "images/icons/country_icons/spain.png"),
				new Team("Italy", GROUP_B, 84.28827257015591, "images/icons/country_icons/italy.png"),
				new Team("Germany", GROUP_A, 81.0708894874649, "images/icons/country_icons/germany.png"),
				new Team("France", GROUP_D, 63.43760434275959, "images/icons/country_icons/france.png"),
				new Team("Netherlands", GROUP_D, 59.43972306211583, "images/icons/country_icons/netherlands.png"),
				new Team("Portugal", GROUP_F, 54.26214336813275, "images/icons/country_icons/portugal.png"),
				new Team("Belgium", GROUP_E, 46.19434173297851, "images/icons/country_icons/belgium.png"),
				new Team("Turkey", GROUP_F, 36.95628912431959, "images/icons/country_icons/turkey.png"),
				new Team("Scotland", GROUP_A, 34.97012448293567, "images/icons/country_icons/scotland.png"),
				new Team("Czech Republic", GROUP_F, 34.773889309769185, "images/icons/country_icons/czech_republic.png"),
				new Team("Switzerland", GROUP_A, 31.91914856542855, "images/icons/country_icons/switzerland.png"),
				new Team("Austria", GROUP_D, 31.618874366131498, "images/icons/country_icons/austria.png"),
				new Team("Denmark", GROUP_C, 30.54010250774111, "images/icons/country_icons/denmark.png"),
				new Team("Serbia", GROUP_C, 26.907041981802263, "images/icons/country_icons/serbia.png"),
				new Team("Croatia", GROUP_B, 24.740229479201854, "images/icons/country_icons/croatia.png"),
				new Team("Hungary", GROUP_A, 21.220504273638458, "images/icons/country_icons/hungary.png"),
				new Team("Romania", GROUP_E, 20.756575755057408, "images/icons/country_icons/romania.png"),
				new Team("Slovakia", GROUP_E, 19.032777315806147, "images/icons/country_icons/slovakia.png"),
				new Team("Slovenia", GROUP_C, 12.850973412076513, "images/icons/country_icons/slovenia.png"),
				new Team("Albania", GROUP_B, 7.164637513812147, "images/icons/country_icons/albania.png"),
				new Team("Play-off Winner A", GROUP_D, 0.0, "images/icons/country_icons/unknown.png"),
				new Team("Play-off Winner B", GROUP_E, 0.0, "images/icons/country_icons/unknown.png"),
				new Team("Play-off Winner C", GROUP_F, 0.0, "images/icons/country_icons/unknown.png"),
		};
	}

	@Override
	protected Group[] getGroups() {
		return new Group[] {
				GROUP_A,
				GROUP_B,
				GROUP_C,
				GROUP_D,
				GROUP_E,
				GROUP_F
		};
	}

}

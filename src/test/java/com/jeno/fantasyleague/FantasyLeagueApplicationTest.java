package com.jeno.fantasyleague;

import com.jeno.fantasyleague.data.service.leaguetemplates.worldcup2018.FifaWorldCup2018Service;
import org.junit.Test;
import org.springframework.util.StringUtils;

import java.util.Arrays;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class FantasyLeagueApplicationTest {

	@Test
	public void contextLoads() {
		Arrays.stream(FifaWorldCup2018Service.Team.values())
				.forEach(lala ->
						System.err.println(
								lala.toString() + "(\"" +
								StringUtils.capitalize(lala.toString().toLowerCase())
										.replaceAll("_", " ") +
								"\", \"images/icons/country_icons/" +
								lala.toString().toLowerCase() + ".png\"),"));
		System.err.println("LENGTH " + FifaWorldCup2018Service.Team.values().length);
	}

}

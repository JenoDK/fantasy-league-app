package com.jeno.fantasyleague.backend.data.service.leaguetemplates;

import com.jeno.fantasyleague.backend.data.service.leaguetemplates.custom.CustomTemplateService;
import com.jeno.fantasyleague.backend.data.service.leaguetemplates.eufaeuro2020.UefaEuro2020Service;
import com.jeno.fantasyleague.backend.data.service.leaguetemplates.eufaeuro2024.UefaEuro2024Service;
import com.jeno.fantasyleague.backend.data.service.leaguetemplates.fifaworld2022.FifaWorldCup2022Service;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LeagueTemplateConfig {

	public static final String FIFA_WORLD_CUP_2018 = "FifaWorldCup2018Service";
	public static final String UEFA_EURO_2020 = "UefaEuro2020Service";
	public static final String FIFA_WORLD_CUP_2022 = "FifaWorldCup2022Service";
	public static final String UEFA_EURO_2024 = "UefaEuro2024Service";
	public static final String CUSTOM = "CustomTemplateService";

	@Bean(name = UEFA_EURO_2024)
	public UefaEuro2024Service getUefaEuro2024Service() {
		return new UefaEuro2024Service();
	}

	@Bean(name = FIFA_WORLD_CUP_2022)
	public FifaWorldCup2022Service getFifaWorldCup2022Service() {
		return new FifaWorldCup2022Service();
	}

	@Bean(name = UEFA_EURO_2020)
	public UefaEuro2020Service getUefaEuro2020Service() {
		return new UefaEuro2020Service();
	}

	@Bean(name = FIFA_WORLD_CUP_2018)
	public UefaEuro2020Service getFifaWorldCup2018Service() {
		return new UefaEuro2020Service();
	}

	@Bean(name = CUSTOM)
	public CustomTemplateService getCustomTemplateService() {
		return new CustomTemplateService();
	}

}

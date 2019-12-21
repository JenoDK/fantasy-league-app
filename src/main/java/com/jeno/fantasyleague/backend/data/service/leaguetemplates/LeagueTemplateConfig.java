package com.jeno.fantasyleague.backend.data.service.leaguetemplates;

import com.jeno.fantasyleague.backend.data.service.leaguetemplates.custom.CustomTemplateService;
import com.jeno.fantasyleague.backend.data.service.leaguetemplates.worldcup2018.FifaWorldCup2018Service;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LeagueTemplateConfig {

	public static final String FIFA_WORLD_CUP_2018 = "FifaWorldCup2018Service";
	public static final String CUSTOM = "CustomTemplateService";

	@Bean(name = FIFA_WORLD_CUP_2018)
	public FifaWorldCup2018Service getFifaWorldCup2018Service() {
		return new FifaWorldCup2018Service();
	}

	@Bean(name = CUSTOM)
	public CustomTemplateService getCustomTemplateService() {
		return new CustomTemplateService();
	}

}

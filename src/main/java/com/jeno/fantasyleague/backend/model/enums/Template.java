package com.jeno.fantasyleague.backend.model.enums;

import com.jeno.fantasyleague.backend.data.service.leaguetemplates.LeagueTemplateConfig;
import com.jeno.fantasyleague.util.Images;

public enum Template {

	UEFA_EURO_2024("UEFA EURO 2024", Images.Icons.UEFA_EURO_2024, LeagueTemplateConfig.UEFA_EURO_2024),
	FIFA_WORLD_CUP_2022("FIFA WORLD CUP QATAR 2022", Images.Icons.FIFA_WORLD_CUP_2022, LeagueTemplateConfig.FIFA_WORLD_CUP_2022),
	UEFA_EURO_2020("UEFA EURO 2020", Images.Icons.UEFA_EURO_2020, LeagueTemplateConfig.UEFA_EURO_2020),
	FIFA_WORLD_CUP_2018("FIFA World Cup 2018", Images.Icons.FIFA_WORLD_CUP_2018, LeagueTemplateConfig.FIFA_WORLD_CUP_2018),
	CUSTOM("Custom", Images.Icons.CUSTOM_LEAGUE, LeagueTemplateConfig.CUSTOM),
	;
	// When adding a template please also check AdminModel#updateGameScoresGlobally and only fetch games for a given matchNumber per template. In FIFA World Cup was the first template where we filled in matchNumber

	private String name;
	private String iconPath;
	private String templateServiceBeanName;

	Template(String name, String iconPath, String templateServiceBeanName) {
		this.name = name;
		this.iconPath = iconPath;
		this.templateServiceBeanName = templateServiceBeanName;
	}

	public String getName() {
		return name;
	}

	public String getTemplateServiceBeanName() {
		return templateServiceBeanName;
	}

	public String getIconPath() {
		return iconPath;
	}
}

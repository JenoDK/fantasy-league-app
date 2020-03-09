package com.jeno.fantasyleague.backend.model.enums;

import com.jeno.fantasyleague.backend.data.service.leaguetemplates.LeagueTemplateConfig;
import com.jeno.fantasyleague.util.Images;

public enum Template {

	UEFA_EURO_2020("UEFA EURO 2020", Images.Icons.UEFA_EURO_2020, LeagueTemplateConfig.UEFA_EURO_2020),
	FIFA_WORLD_CUP_2018("FIFA World Cup 2018", Images.Icons.FIFA_WORLD_CUP_2018, LeagueTemplateConfig.FIFA_WORLD_CUP_2018),
	CUSTOM("Custom", Images.Icons.CUSTOM_LEAGUE, LeagueTemplateConfig.CUSTOM),
	;

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

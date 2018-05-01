package com.jeno.fantasyleague.model.enums;

import com.jeno.fantasyleague.data.service.leaguetemplates.LeagueTemplateConfig;
import com.jeno.fantasyleague.util.Images;

public enum Template {

	FIFA_WORLD_CUP_2018("FIFA World Cup 2018", Images.Icons.FIFA_WORLD_CUP_2018, LeagueTemplateConfig.FIFA_WORLD_CUP_2018),
	CUSTOM("Custom", Images.Icons.CUSTOM_LEAGUE, LeagueTemplateConfig.CUSTOM);

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

package com.jeno.fantasyleague.ui.main.views.league.singleleague.overview.chart.model;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class IconColor {

	private final String color;
	private final String iconPath;

	public IconColor(String iconPath, String color) {
		this.color = color;
		this.iconPath = iconPath;
	}

	public static List<IconColor> createList(Map<String, String> colorsPerCountry) {
		return colorsPerCountry.entrySet().stream()
				.map(e -> new IconColor(e.getKey(), e.getValue()))
				.collect(Collectors.toList());
	}

	public String getColor() {
		return color;
	}

	public String getIconPath() {
		return iconPath;
	}
}

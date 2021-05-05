package com.jeno.fantasyleague.ui.main.views.league.singleleague.overview.chart.model;

public class ScoreChartSerieData {

	private final Integer serieId;
	private final String color;
	private final String iconPath;

	public ScoreChartSerieData(Integer serieId, String color, String iconPath) {
		this.serieId = serieId;
		this.color = color;
		this.iconPath = iconPath;
	}

	public Integer getSerieId() {
		return serieId;
	}

	public String getColor() {
		return color;
	}

	public String getIconPath() {
		return iconPath;
	}
}

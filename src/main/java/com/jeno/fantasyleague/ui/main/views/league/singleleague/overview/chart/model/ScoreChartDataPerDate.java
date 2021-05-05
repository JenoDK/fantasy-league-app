package com.jeno.fantasyleague.ui.main.views.league.singleleague.overview.chart.model;

import java.util.List;

public class ScoreChartDataPerDate {

	public final String date;
	public final List<Double> scores;

	public ScoreChartDataPerDate(String date, List<Double> scores) {
		this.date = date;
		this.scores = scores;
	}

	public String getDate() {
		return date;
	}

	public List<Double> getScores() {
		return scores;
	}
}

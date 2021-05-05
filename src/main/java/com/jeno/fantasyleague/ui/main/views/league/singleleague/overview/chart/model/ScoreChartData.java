package com.jeno.fantasyleague.ui.main.views.league.singleleague.overview.chart.model;

import java.util.List;

public class ScoreChartData {

	private final String name;
	private final Integer userId;
	private final boolean hasUserProfilePicture;
	private final List<Double> scores;
	private final int position;
	private final List<String> dataRoles;

	public ScoreChartData(String name, Integer userId, boolean hasUserProfilePicture, List<Double> scores, int position, List<String> dataRoles) {
		this.name = name;
		this.userId = userId;
		this.hasUserProfilePicture = hasUserProfilePicture;
		this.scores = scores;
		this.position = position;
		this.dataRoles = dataRoles;
	}

	public String getName() {
		return name;
	}

	public Integer getUserId() {
		return userId;
	}

	public boolean isHasUserProfilePicture() {
		return hasUserProfilePicture;
	}

	public List<Double> getScores() {
		return scores;
	}

	public int getPosition() {
		return position;
	}

	public List<String> getDataRoles() {
		return dataRoles;
	}
}

package com.jeno.fantasyleague.ui.main.views.league.singleleague.matches.single;

import java.util.Optional;

import com.jeno.fantasyleague.backend.model.Prediction;
import com.jeno.fantasyleague.backend.model.User;

public class UserScoresForGameBean {

	private final User user;
	private Prediction prediction;
	private Integer homePrediction;
	private Integer awayPrediction;
	private final double score;
	private final Optional<Boolean> homeTeamWon;
	private final boolean predictionIsHiddenForUser;
	private final String predictionHiddenUntil;
	private final Integer homeTeamWeight;
	private final Integer awayTeamWeight;

	public UserScoresForGameBean(
			User user,
			Prediction prediction,
			double score,
			Integer homeTeamWeight,
			Integer awayTeamWeight,
			Optional<Boolean> homeTeamWon,
			boolean predictionIsHiddenForUser,
			String predictionHiddenUntil) {
		this.user = user;
		setPrediction(prediction);
		this.score = score;
		this.homeTeamWeight = homeTeamWeight;
		this.awayTeamWeight = awayTeamWeight;
		this.homeTeamWon = homeTeamWon;
		this.predictionIsHiddenForUser = predictionIsHiddenForUser;
		this.predictionHiddenUntil = predictionHiddenUntil;
	}

	public void setPrediction(Prediction prediction) {
		this.prediction = prediction;
		this.homePrediction = prediction.getHome_team_score();
		this.awayPrediction = prediction.getAway_team_score();
	}

	public User getUser() {
		return user;
	}

	public Prediction getPrediction() {
		return prediction;
	}

	public Integer getHomePrediction() {
		return homePrediction;
	}

	public Integer getAwayPrediction() {
		return awayPrediction;
	}

	public double getScore() {
		return score;
	}

	public Integer getHomeTeamWeight() {
		return homeTeamWeight;
	}

	public Integer getAwayTeamWeight() {
		return awayTeamWeight;
	}

	public Optional<Boolean> getHomeTeamWon() {
		return homeTeamWon;
	}

	public boolean isPredictionIsHiddenForUser() {
		return predictionIsHiddenForUser;
	}

	public String getPredictionHiddenUntil() {
		return predictionHiddenUntil;
	}
}

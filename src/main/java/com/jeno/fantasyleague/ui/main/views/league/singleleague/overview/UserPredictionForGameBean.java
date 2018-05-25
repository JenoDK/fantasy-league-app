package com.jeno.fantasyleague.ui.main.views.league.singleleague.overview;

import java.util.Optional;

import com.jeno.fantasyleague.model.Prediction;
import com.jeno.fantasyleague.model.User;

public class UserPredictionForGameBean {

	private final User user;
	private final Prediction prediction;
	private final Integer homePrediction;
	private final Integer awayPrediction;
	private final double score;
	private final Optional<Boolean> homeTeamWon;
	private final boolean predictionIsHiddenForUser;
	private final String predictionHiddenUntil;

	public UserPredictionForGameBean(User user, Prediction prediction, double score, Optional<Boolean> homeTeamWon, boolean predictionIsHiddenForUser, String predictionHiddenUntil) {
		this.user = user;
		this.prediction = prediction;
		this.homePrediction = prediction.getHome_team_score();
		this.awayPrediction = prediction.getAway_team_score();
		this.score = score;
		this.homeTeamWon = homeTeamWon;
		this.predictionIsHiddenForUser = predictionIsHiddenForUser;
		this.predictionHiddenUntil = predictionHiddenUntil;
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

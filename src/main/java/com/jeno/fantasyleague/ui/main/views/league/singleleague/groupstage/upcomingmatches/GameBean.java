package com.jeno.fantasyleague.ui.main.views.league.singleleague.groupstage.upcomingmatches;

import java.time.LocalDateTime;
import java.util.Objects;

import com.jeno.fantasyleague.model.Contestant;
import com.jeno.fantasyleague.model.Game;
import com.jeno.fantasyleague.model.Prediction;

public class GameBean {

	private final Game game;
	private final Prediction prediction;

	private Integer homeTeamScore;
	private Integer awayTeamScore;

	private Integer homeTeamPrediction;
	private Integer awayTeamPrediction;

	public GameBean(Game game, Prediction prediction) {
		this.game = game;
		this.prediction = prediction;

		homeTeamScore = game.getHome_team_score();
		awayTeamScore = game.getAway_team_score();

		homeTeamPrediction = prediction.getHome_team_score();
		awayTeamPrediction = prediction.getAway_team_score();
	}

	public Contestant getHome_team() {
		return game.getHome_team();
	}

	public Contestant getAway_team() {
		return game.getAway_team();
	}

	public String getRound() {
		return game.getRound();
	}

	public String getLocation() {
		return game.getLocation();
	}

	public LocalDateTime getGame_date_time() {
		return game.getGame_date_time();
	}

	public Integer getHome_team_score() {
		return homeTeamScore;
	}

	public Integer getAway_team_score() {
		return awayTeamScore;
	}

	public void setHomeTeamScore(Integer homeTeamScore) {
		this.homeTeamScore = homeTeamScore;
	}

	public void setAwayTeamScore(Integer awayTeamScore) {
		this.awayTeamScore = awayTeamScore;
	}

	public Integer getHomeTeamPrediction() {
		return homeTeamPrediction;
	}

	public void setHomeTeamPrediction(Integer homeTeamPrediction) {
		this.homeTeamPrediction = homeTeamPrediction;
	}

	public Integer getAwayTeamPrediction() {
		return awayTeamPrediction;
	}

	public void setAwayTeamPrediction(Integer awayTeamPrediction) {
		this.awayTeamPrediction = awayTeamPrediction;
	}

	public boolean scoresAreValid() {
		return Objects.nonNull(homeTeamScore) && Objects.nonNull(awayTeamScore);
	}

	public Game setTeamScoresAndGetModelItem() {
		game.setHome_team_score(homeTeamScore);
		game.setAway_team_score(awayTeamScore);
		return game;
	}

	public Prediction setPredictionsAndGetModelItem() {
		prediction.setHome_team_score(homeTeamPrediction);
		prediction.setAway_team_score(awayTeamPrediction);
		if (homeTeamPrediction > awayTeamPrediction) {
			prediction.setWinner(game.getHome_team());
		} else if (homeTeamPrediction < awayTeamPrediction) {
			prediction.setWinner(game.getAway_team());
		} else {
			prediction.setWinner(null);
		}
		return prediction;
	}

}

package com.jeno.fantasyleague.ui.main.views.league.singleleague.groupstage.prediction;

import com.jeno.fantasyleague.model.Contestant;
import com.jeno.fantasyleague.model.Game;
import com.jeno.fantasyleague.model.Prediction;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.groupstage.upcomingmatches.GameBean;

import java.util.Objects;

public class PredictionBean {

	private final Game game;
	private final Prediction prediction;

	private Integer homeTeamScore = 0;
	private Integer awayTeamScore = 0;

	public PredictionBean(Game game, Prediction prediction) {
		this.game = game;
		this.prediction = prediction;
		homeTeamScore = GameBean.getTeamScore(prediction.getHome_team_score());
		awayTeamScore = GameBean.getTeamScore(prediction.getAway_team_score());
	}

	public Contestant getHomeTeam() {
		return game.getHome_team();
	}

	public Game getGame() {
		return game;
	}

	public Contestant getAwayTeam() {
		return game.getAway_team();
	}

	public Integer getHomeTeamScore() {
		return GameBean.getTeamScore(homeTeamScore);
	}

	public void setHomeTeamScore(Integer homeTeamScore) {
		this.homeTeamScore = homeTeamScore;
	}

	public Integer getAwayTeamScore() {
		return GameBean.getTeamScore(awayTeamScore);
	}

	public void setAwayTeamScore(Integer awayTeamScore) {
		this.awayTeamScore = awayTeamScore;
	}

	public boolean scoreChanged() {
		return !Objects.equals(homeTeamScore, GameBean.getTeamScore(prediction.getHome_team_score()))
				|| !Objects.equals(awayTeamScore, GameBean.getTeamScore(prediction.getAway_team_score()));
	}

	public Prediction setPredictionsAndGetModelItem() {
		prediction.setHome_team_score(homeTeamScore);
		prediction.setAway_team_score(awayTeamScore);
		return prediction;
	}

}

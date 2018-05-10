package com.jeno.fantasyleague.ui.main.views.league.singleleague.groupstage.prediction;

import java.util.Objects;

import com.jeno.fantasyleague.model.Contestant;
import com.jeno.fantasyleague.model.Game;
import com.jeno.fantasyleague.model.Prediction;

public class PredictionBean {

	private final Game game;
	private final Prediction prediction;

	private Integer homeTeamScore;
	private Integer awayTeamScore;

	public PredictionBean(Game game, Prediction prediction) {
		this.game = game;
		this.prediction = prediction;
		homeTeamScore = prediction.getHome_team_score();
		awayTeamScore = prediction.getAway_team_score();
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
		return homeTeamScore;
	}

	public void setHomeTeamScore(Integer homeTeamScore) {
		this.homeTeamScore = homeTeamScore;
	}

	public Integer getAwayTeamScore() {
		return awayTeamScore;
	}

	public void setAwayTeamScore(Integer awayTeamScore) {
		this.awayTeamScore = awayTeamScore;
	}

	public boolean scoreChangedAndIsValid() {
		boolean scoreChanged = !Objects.equals(homeTeamScore, prediction.getHome_team_score())
				|| !Objects.equals(awayTeamScore, prediction.getAway_team_score());
		boolean nonNullValues = Objects.nonNull(homeTeamScore) && Objects.nonNull(awayTeamScore);
		return scoreChanged && nonNullValues;
	}

	public Prediction setPredictionsAndGetModelItem() {
		prediction.setHome_team_score(homeTeamScore);
		prediction.setAway_team_score(awayTeamScore);
		if (homeTeamScore > awayTeamScore) {
			prediction.setWinner(game.getHome_team());
		} else if (homeTeamScore < awayTeamScore) {
			prediction.setWinner(game.getAway_team());
		}
		return prediction;
	}

}

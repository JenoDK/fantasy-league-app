package com.jeno.fantasyleague.ui.main.views.league.singleleague.overview;

import java.util.Optional;

import com.jeno.fantasyleague.data.service.leaguetemplates.worldcup2018.FifaWorldCup2018Stages;
import com.jeno.fantasyleague.model.Contestant;
import com.jeno.fantasyleague.model.Game;
import com.jeno.fantasyleague.model.League;
import com.jeno.fantasyleague.model.Prediction;
import com.jeno.fantasyleague.util.DateUtil;

public class UserPredictionScoreBean {

	private final Prediction prediction;
	private final Contestant homeTeam;
	private final Contestant awayTeam;
	private final double predictionScore;
	private final boolean predictionIsHidden;
	private final League league;

	public UserPredictionScoreBean(Prediction prediction, Contestant homeTeam, Contestant awayTeam, double predictionScore, boolean predictionIsHidden, League league) {
		this.prediction = prediction;
		this.homeTeam = homeTeam;
		this.awayTeam = awayTeam;
		this.predictionScore = predictionScore;
		this.predictionIsHidden = predictionIsHidden;
		this.league = league;
	}

	public Game getGame() {
		return prediction.getGame();
	}

	public Contestant getHome_team() {
		return homeTeam;
	}

	public Contestant getAway_team() {
		return awayTeam;
	}

	public Integer getGameHome_team_score() {
		return prediction.getGame().getHome_team_score();
	}

	public Integer getGameAway_team_score() {
		return prediction.getGame().getAway_team_score();
	}

	public Optional<Boolean> getGameHomeTeamWon() {
		return Optional.ofNullable(getHome_team())
				.map(Contestant::getId)
				.map(id -> id.equals(prediction.getGame().getWinner_fk()));
	}

	public Integer getPredictionHome_team_score() {
		return prediction.getHome_team_score();
	}

	public Integer getPredictionAway_team_score() {
		return prediction.getAway_team_score();
	}

	public Optional<Boolean> getPredictionHomeTeamWon() {
		return Optional.ofNullable(getHome_team())
				.map(Contestant::getId)
				.map(id -> id.equals(prediction.getWinner_fk()));
	}

	public double getPredictionScore() {
		return predictionScore;
	}

	public boolean predictionIsHidden() {
		return predictionIsHidden;
	}

	public String getPredictionHiddenUntil() {
		if (FifaWorldCup2018Stages.GROUP_PHASE.toString().equals(prediction.getGame().getStage())) {
			return DateUtil.DATE_TIME_FORMATTER.format(league.getLeague_starting_date());
		} else {
			return DateUtil.DATE_TIME_FORMATTER.format(prediction.getGame().getGame_date_time());
		}
	}
}

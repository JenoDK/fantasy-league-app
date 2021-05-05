package com.jeno.fantasyleague.ui.main.views.league.singleleague.matches;

import java.util.Objects;
import java.util.Optional;

import com.jeno.fantasyleague.backend.data.service.leaguetemplates.SoccerCupStages;
import com.jeno.fantasyleague.backend.model.Contestant;
import com.jeno.fantasyleague.backend.model.Game;
import com.jeno.fantasyleague.backend.model.League;
import com.jeno.fantasyleague.backend.model.Prediction;
import com.jeno.fantasyleague.util.DateUtil;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

public class MatchBean {

	private Prediction prediction;
	private BehaviorSubject<MatchBean> predictionChanged = BehaviorSubject.create();
	private BehaviorSubject<Contestant> homeContestantChanged = BehaviorSubject.create();
	private BehaviorSubject<Contestant> awayContestantChanged = BehaviorSubject.create();

	private final Contestant homeTeam;
	private final Contestant awayTeam;
	private final Integer homeTeamWeight;
	private final Integer awayTeamWeight;
	private final double predictionScore;
	private final boolean predictionIsHidden;
	private final League league;

	public MatchBean(
			Prediction prediction,
			Contestant homeTeam,
			Contestant awayTeam,
			Integer homeTeamWeight,
			Integer awayTeamWeight,
			double predictionScore,
			boolean predictionIsHidden,
			League league) {
		this.prediction = prediction;
		this.homeTeam = homeTeam;
		this.awayTeam = awayTeam;
		this.homeTeamWeight = homeTeamWeight;
		this.awayTeamWeight = awayTeamWeight;
		this.predictionScore = predictionScore;
		this.predictionIsHidden = predictionIsHidden;
		this.league = league;
	}

	public Observable<MatchBean> predictionCHanged() {
		return predictionChanged;
	}

	public void setPrediction(Prediction prediction) {
		this.prediction = prediction;
		this.predictionChanged.onNext(this);
	}

	public Prediction getPrediction() {
		return prediction;
	}

	public Contestant getHomeTeam() {
		return homeTeam;
	}

	public Contestant getAwayTeam() {
		return awayTeam;
	}

	public League getLeague() {
		return league;
	}

	public Game getGame() {
		return prediction.getGame();
	}

	public Integer getHomeTeamWeight() {
		return homeTeamWeight;
	}

	public Integer getAwayTeamWeight() {
		return awayTeamWeight;
	}

	public Integer getGameHomeTeamScore() {
		return prediction.getGame().getHome_team_score();
	}

	public Integer getGameAwayTeamScore() {
		return prediction.getGame().getAway_team_score();
	}

	public Optional<Boolean> getGameHomeTeamWon() {
		return Optional.ofNullable(getHomeTeam())
				.filter(ignored -> Objects.nonNull(prediction.getGame().getWinner()))
				.map(Contestant::getId)
				.map(id -> id.equals(prediction.getGame().getWinner_fk()));
	}

	public Integer getHomeTeamPrediction() {
		return prediction.getHome_team_score();
	}

	public Integer getAwayTeamPrediction() {
		return prediction.getAway_team_score();
	}

	public Optional<Boolean> getPredictionHomeTeamWon() {
		return Optional.ofNullable(getHomeTeam())
				.filter(ignored -> Objects.nonNull(prediction.getWinner()))
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
		if (SoccerCupStages.GROUP_PHASE.toString().equals(prediction.getGame().getStage())) {
			return DateUtil.DATE_TIME_FORMATTER.format(league.getLeague_starting_date());
		} else {
			return DateUtil.DATE_TIME_FORMATTER.format(prediction.getGame().getGameDateTime());
		}
	}

	public BehaviorSubject<Contestant> getHomeContestantChanged() {
		return homeContestantChanged;
	}

	public BehaviorSubject<Contestant> getAwayContestantChanged() {
		return awayContestantChanged;
	}
}

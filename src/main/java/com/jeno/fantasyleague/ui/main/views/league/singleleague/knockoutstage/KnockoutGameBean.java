package com.jeno.fantasyleague.ui.main.views.league.singleleague.knockoutstage;

import java.util.Optional;

import com.jeno.fantasyleague.model.Contestant;
import com.jeno.fantasyleague.model.Game;
import com.jeno.fantasyleague.model.Prediction;

public class KnockoutGameBean {

	private Game game;
	private Prediction prediction;

	private Contestant awayTeam;
	private Contestant homeTeam;

	private Integer homeTeamScore;
	private Integer awayTeamScore;
	private Optional<Boolean> homeTeamIsWinner = Optional.empty();

	private Integer homeTeamPrediction;
	private Integer awayTeamPrediction;
	private Optional<Boolean> homeTeamPredictionIsWinner = Optional.empty();

	public KnockoutGameBean(Game game, Contestant homeTeam, Contestant awayTeam, Prediction prediction) {
		this.game = game;
		this.prediction = prediction;

		this.homeTeam = homeTeam;
		this.awayTeam = awayTeam;

		homeTeamScore = game.getHome_team_score();
		awayTeamScore = game.getAway_team_score();
		homeTeamIsWinner = Optional.ofNullable(game.getWinner())
				.flatMap(winner -> Optional.ofNullable(homeTeam))
				.map(home -> home.getId().equals(game.getWinner().getId()));

		homeTeamPrediction = prediction.getHome_team_score();
		awayTeamPrediction = prediction.getAway_team_score();
		homeTeamPredictionIsWinner = Optional.ofNullable(prediction.getWinner())
				.flatMap(winner -> Optional.ofNullable(homeTeam))
				.map(home -> home.getId().equals(prediction.getWinner().getId()));
	}

	public Contestant getAwayTeam() {
		return awayTeam;
	}

	public void setAwayTeam(Contestant awayTeam) {
		this.awayTeam = awayTeam;
		game.setAway_team(awayTeam);
	}

	public Contestant getHomeTeam() {
		return homeTeam;
	}

	public void setHomeTeam(Contestant homeTeam) {
		this.homeTeam = homeTeam;
		game.setHome_team(homeTeam);
	}

	public Game getGame() {
		return game;
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

	// In case of equal score this boolean decides the winner
	public void setHomeTeamIsWinner(boolean homeTeamIsWinner) {
		this.homeTeamIsWinner = Optional.of(homeTeamIsWinner);
	}

	public Optional<Boolean> getHomeTeamIsWinner() {
		return homeTeamIsWinner;
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

	// In case of equal score this boolean decides the winner
	public void setHomeTeamPredictionIsWinner(boolean homeTeamPredictionIsWinner) {
		this.homeTeamPredictionIsWinner = Optional.of(homeTeamPredictionIsWinner);
	}

	public Optional<Boolean> getHomeTeamPredictionIsWinner() {
		return homeTeamPredictionIsWinner;
	}

	public Game setScoresAndGetGameModelItem() {
		game.setHome_team_score(homeTeamScore);
		game.setAway_team_score(awayTeamScore);
		if (homeTeamScore > awayTeamScore) {
			game.setWinner(game.getHome_team());
		} else if (homeTeamScore < awayTeamScore) {
			game.setWinner(game.getAway_team());
		} else {
			if (homeTeamIsWinner.isPresent() && homeTeamIsWinner.get()) {
				game.setWinner(game.getHome_team());
			} else {
				game.setWinner(game.getAway_team());
			}
		}
		return game;
	}

	public Prediction setScoresAndGetPredictionModelItem() {
		prediction.setHome_team_score(homeTeamPrediction);
		prediction.setAway_team_score(awayTeamPrediction);
		if (homeTeamPrediction > awayTeamPrediction) {
			prediction.setWinner(homeTeam);
		} else if (homeTeamPrediction < awayTeamPrediction) {
			prediction.setWinner(awayTeam);
		} else {
			if (homeTeamPredictionIsWinner.isPresent() && homeTeamPredictionIsWinner.get()) {
				prediction.setWinner(homeTeam);
			} else {
				prediction.setWinner(awayTeam);
			}
		}
		return prediction;
	}
}

package com.jeno.fantasyleague.ui.main.views.league.singleleague.matches;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

import com.jeno.fantasyleague.backend.data.service.leaguetemplates.SoccerCupStages;
import com.jeno.fantasyleague.backend.model.Contestant;
import com.jeno.fantasyleague.backend.model.Game;
import com.jeno.fantasyleague.backend.model.League;
import com.jeno.fantasyleague.backend.model.Prediction;

public class MatchPredictionBean {

	private League league;
	private Game game;
	private Prediction prediction;

	private Contestant awayTeam;
	private Contestant homeTeam;

	private Integer homeTeamScore;
	private Integer awayTeamScore;
	private Optional<Boolean> homeTeamIsWinner;

	private Integer homeTeamPrediction;
	private Integer awayTeamPrediction;
	private Optional<Boolean> homeTeamPredictionIsWinner;

	public MatchPredictionBean(League league, Prediction prediction) {
		this.league = league;
		this.game = prediction.getGame();
		this.prediction = prediction;

		this.homeTeam = game.getHome_team();
		this.awayTeam = game.getAway_team();

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

	public League getLeague() {
		return league;
	}

	public Contestant getAwayTeam() {
		return awayTeam;
	}

	public Contestant getHomeTeam() {
		return homeTeam;
	}

	public String getRound() {
		return game.getRound();
	}

	public String getLocation() {
		return game.getLocation();
	}

	public LocalDateTime getGame_date_time() {
		return game.getGameDateTime();
	}

	public Integer getHomeTeamScore() {
		return homeTeamScore;
	}

	public Integer getAwayTeamScore() {
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

	public Optional<Boolean> getHomeTeamIsWinner() {
		return homeTeamIsWinner;
	}

	public Optional<Boolean> getHomeTeamPredictionIsWinner() {
		return homeTeamPredictionIsWinner;
	}

	public void setHomeTeamIsWinner(Boolean homeTeamIsWinner) {
		this.homeTeamIsWinner = Optional.of(homeTeamIsWinner);
	}

	public void setHomeTeamIsWinnerOptional(Optional<Boolean> homeTeamIsWinner) {
		this.homeTeamIsWinner = homeTeamIsWinner;
	}

	public void setHomeTeamPredictionIsWinner(Boolean homeTeamPredictionIsWinner) {
		this.homeTeamPredictionIsWinner = Optional.of(homeTeamPredictionIsWinner);
	}

	public Game setGameScoresAndGetGameModelItem() {
		game.setHome_team_score(homeTeamScore);
		game.setAway_team_score(awayTeamScore);
		if (homeTeamScore != null && awayTeamScore != null) {
			if (homeTeamScore > awayTeamScore) {
				game.setWinner(game.getHome_team());
			} else if (homeTeamScore < awayTeamScore) {
				game.setWinner(game.getAway_team());
			} else {
				if (homeTeamIsWinner.isPresent() && !SoccerCupStages.GROUP_PHASE.toString().equals(game.getStage())) {
					if (homeTeamIsWinner.get()) {
						game.setWinner(game.getHome_team());
					} else {
						game.setWinner(game.getAway_team());
					}
				} else {
					game.setWinner(null);
				}
			}
		}
		return game;
	}

	public Prediction setPredictionScoresAndGetModelItem() {
		prediction.setHome_team_score(homeTeamPrediction);
		prediction.setAway_team_score(awayTeamPrediction);
		if (homeTeamPrediction > awayTeamPrediction) {
			prediction.setWinner(homeTeam);
		} else if (homeTeamPrediction < awayTeamPrediction) {
			prediction.setWinner(awayTeam);
		} else {
			if (homeTeamPredictionIsWinner.isPresent() && !SoccerCupStages.GROUP_PHASE.toString().equals(game.getStage())) {
				if (homeTeamPredictionIsWinner.get()) {
					prediction.setWinner(homeTeam);
				} else {
					prediction.setWinner(awayTeam);
				}
			} else {
				prediction.setWinner(null);
			}
		}
		return prediction;
	}

}

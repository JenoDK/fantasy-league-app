package com.jeno.fantasyleague.ui.main.views.league.singleleague.groupstage.upcomingmatches;

import com.jeno.fantasyleague.model.Contestant;
import com.jeno.fantasyleague.model.Game;

import java.time.LocalDateTime;
import java.util.Objects;

public class GameBean {

	private final Game game;

	private Integer homeTeamScore;
	private Integer awayTeamScore;

	public GameBean(Game game) {
		this.game = game;
		homeTeamScore = game.getHome_team_score();
		awayTeamScore = game.getAway_team_score();
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

	public boolean scoreChangedAndIsValid() {
		boolean scoreChanged = !Objects.equals(homeTeamScore, game.getHome_team_score())
				|| !Objects.equals(awayTeamScore, game.getAway_team_score());
		boolean nonNullValues = Objects.nonNull(homeTeamScore) && Objects.nonNull(awayTeamScore);
		return scoreChanged && nonNullValues;
	}

	public Game setTeamScoresAndGetModelItem() {
		game.setHome_team_score(homeTeamScore);
		game.setAway_team_score(awayTeamScore);
		return game;
	}

	public static int getTeamScore(Integer score) {
		return score != null ? score : 0;
	}

}

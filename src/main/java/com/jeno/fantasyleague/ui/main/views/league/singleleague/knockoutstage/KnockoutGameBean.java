package com.jeno.fantasyleague.ui.main.views.league.singleleague.knockoutstage;

import java.util.Objects;
import java.util.Optional;

import com.jeno.fantasyleague.model.Contestant;
import com.jeno.fantasyleague.model.Game;

public class KnockoutGameBean {

	private Game game;
	private Contestant awayTeam;
	private Contestant homeTeam;
	private Integer homeTeamScore;
	private Integer awayTeamScore;
	private Optional<Boolean> homeTeamIsWinner = Optional.empty();

	public KnockoutGameBean(Game game, Contestant homeTeam, Contestant awayTeam) {
		this.game = game;
		this.homeTeam = homeTeam;
		this.awayTeam = awayTeam;
		homeTeamScore = game.getHome_team_score();
		awayTeamScore = game.getAway_team_score();
		homeTeamIsWinner = Optional.ofNullable(game.getWinner())
				.flatMap(winner -> Optional.ofNullable(homeTeam))
				.map(home -> home.getId().equals(game.getWinner().getId()));
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

	public boolean scoresAreValid() {
		if (scoreNotNullAndEqual()) {
			return homeTeamIsWinner.isPresent();
		} else {
			return Objects.nonNull(homeTeamScore) && Objects.nonNull(awayTeamScore);
		}
	}

	public Game setScoresAndGetModelItem() {
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

	public boolean scoreNotNullAndEqual() {
		return Objects.nonNull(homeTeamScore) && Objects.nonNull(awayTeamScore) && homeTeamScore.equals(awayTeamScore);
	}

	// In case of equal score this boolean decides the winner
	public void setHomeTeamIsWinner(boolean homeTeamIsWinner) {
		this.homeTeamIsWinner = Optional.of(homeTeamIsWinner);
	}

	public Optional<Boolean> getHomeTeamIsWinner() {
		return homeTeamIsWinner;
	}
}

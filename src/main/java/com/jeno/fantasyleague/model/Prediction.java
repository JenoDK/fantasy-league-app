package com.jeno.fantasyleague.model;

import com.jeno.fantasyleague.model.audit.UserAudit;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "prediction")
public class Prediction extends UserAudit {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(targetEntity = Game.class, fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "game_id")
	private Game game;

	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "user_id")
	private User user;

	// In case of equal scores sometimes you still want to assign a winner (penalties f.e.)
	@ManyToOne(targetEntity = Contestant.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "winner_id")
	private Contestant winner;

	private Integer home_team_score;

	private Integer away_team_score;

	public Prediction() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Contestant getWinner() {
		return winner;
	}

	public void setWinner(Contestant winner) {
		this.winner = winner;
	}

	public Integer getHome_team_score() {
		return home_team_score;
	}

	public void setHome_team_score(Integer home_team_score) {
		this.home_team_score = home_team_score;
	}

	public Integer getAway_team_score() {
		return away_team_score;
	}

	public void setAway_team_score(Integer away_team_score) {
		this.away_team_score = away_team_score;
	}
}

package com.jeno.fantasyleague.backend.model;

import com.jeno.fantasyleague.backend.model.audit.UserAudit;

import javax.persistence.*;

@Entity
@Table(name = "prediction")
public class Prediction extends UserAudit {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(targetEntity = Game.class, fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "game_id")
	private Game game;

	@Column(name = "game_id", insertable = false, updatable = false)
	private Long game_fk;

	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "user_id")
	private User user;

	@Column(name = "user_id", insertable = false, updatable = false)
	private Long user_fk;

	// In case of equal scores sometimes you still want to assign a winner (penalties f.e.)
	@ManyToOne(targetEntity = Contestant.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "winner_id")
	private Contestant winner;

	@Column(name = "winner_id", insertable = false, updatable = false)
	private Long winner_fk;

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

	public Long getGame_fk() {
		return game_fk;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Long getUser_fk() {
		return user_fk;
	}

	public Contestant getWinner() {
		return winner;
	}

	public void setWinner(Contestant winner) {
		this.winner = winner;
	}

	public Long getWinner_fk() {
		return winner_fk;
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

package com.jeno.fantasyleague.model;

import com.jeno.fantasyleague.model.audit.UserAudit;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "game")
public class Game extends UserAudit {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(targetEntity = League.class, fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "league_id")
	private League league;

	@ManyToOne(targetEntity = Contestant.class, fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "home_team_id")
	private Contestant home_team;

	@ManyToOne(targetEntity = Contestant.class, fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "away_team_id")
	private Contestant away_team;

	@ManyToOne(targetEntity = Contestant.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "winner_id")
	private Contestant winner;

	@Size(max = 128)
	private String location;

	private LocalDateTime game_date_time;

	private String round;

	private String stage;

	private Integer home_team_score;

	private Integer away_team_score;

	public Game() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public League getLeague() {
		return league;
	}

	public void setLeague(League league) {
		this.league = league;
	}

	public Contestant getHome_team() {
		return home_team;
	}

	public void setHome_team(Contestant home_team) {
		this.home_team = home_team;
	}

	public Contestant getAway_team() {
		return away_team;
	}

	public void setAway_team(Contestant away_team) {
		this.away_team = away_team;
	}

	public Contestant getWinner() {
		return winner;
	}

	public void setWinner(Contestant winner) {
		this.winner = winner;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public LocalDateTime getGame_date_time() {
		return game_date_time;
	}

	public void setGame_date_time(LocalDateTime game_date_time) {
		this.game_date_time = game_date_time;
	}

	public String getRound() {
		return round;
	}

	public void setRound(String round) {
		this.round = round;
	}

	public String getStage() {
		return stage;
	}

	public void setStage(String stage) {
		this.stage = stage;
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

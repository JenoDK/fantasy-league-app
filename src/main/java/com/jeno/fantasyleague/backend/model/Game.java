package com.jeno.fantasyleague.backend.model;

import com.jeno.fantasyleague.backend.model.audit.UserAudit;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
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

	@Column(name = "home_team_id", insertable = false, updatable = false)
	private Long home_team_fk;

	private String home_team_placeholder;

	@ManyToOne(targetEntity = Contestant.class, fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "away_team_id")
	private Contestant away_team;

	@Column(name = "away_team_id", insertable = false, updatable = false)
	private Long away_team_fk;

	private String away_team_placeholder;

	@ManyToOne(targetEntity = Contestant.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "winner_id")
	private Contestant winner;

	@Column(name = "winner_id", insertable = false, updatable = false)
	private Long winner_fk;

	@ManyToOne(targetEntity = Game.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "next_game_id")
	private Game next_game;

	@Column(name = "next_game_id", insertable = false, updatable = false)
	private Long next_game_fk;

	@Length(max = 128)
	private String location;

	@Column(name = "game_date_time")
	private LocalDateTime gameDateTime;

	private String round;

	private String stage;

	private Integer home_team_score;

	private Integer away_team_score;

	@Column(name = "match_number")
	private Integer matchNumber;

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

	public Long getHome_team_fk() {
		return home_team_fk;
	}

	public String getHome_team_placeholder() {
		return home_team_placeholder;
	}

	public void setHome_team_placeholder(String home_team_placeholder) {
		this.home_team_placeholder = home_team_placeholder;
	}

	public Contestant getAway_team() {
		return away_team;
	}

	public void setAway_team(Contestant away_team) {
		this.away_team = away_team;
	}

	public Long getAway_team_fk() {
		return away_team_fk;
	}

	public String getAway_team_placeholder() {
		return away_team_placeholder;
	}

	public void setAway_team_placeholder(String away_team_placeholder) {
		this.away_team_placeholder = away_team_placeholder;
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

	public Game getNext_game() {
		return next_game;
	}

	public void setNext_game(Game next_game) {
		this.next_game = next_game;
	}

	public Long getNext_game_fk() {
		return next_game_fk;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public LocalDateTime getGameDateTime() {
		return gameDateTime;
	}

	public void setGameDateTime(LocalDateTime gameDateTime) {
		this.gameDateTime = gameDateTime;
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

	public Integer getMatchNumber() {
		return matchNumber;
	}

	public void setMatchNumber(Integer matchNumber) {
		this.matchNumber = matchNumber;
	}
}

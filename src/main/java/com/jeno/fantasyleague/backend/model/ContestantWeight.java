package com.jeno.fantasyleague.backend.model;

import javax.persistence.*;

@Entity
@Table(name = "contestantweight")
public class ContestantWeight {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(targetEntity = Contestant.class, fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "contestant_id")
	private Contestant contestant;

	@Column(name = "contestant_id", insertable = false, updatable = false)
	private Long contestant_fk;

	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "user_id")
	private User user;

	@Column(name = "user_id", insertable = false, updatable = false)
	private Long user_fk;

	@ManyToOne(targetEntity = League.class, fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "league_id")
	private League league;

	private Integer weight;

	public ContestantWeight() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Contestant getContestant() {
		return contestant;
	}

	public void setContestant(Contestant contestant) {
		this.contestant = contestant;
	}

	public Long getContestant_fk() {
		return contestant_fk;
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

	public League getLeague() {
		return league;
	}

	public void setLeague(League league) {
		this.league = league;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}
}

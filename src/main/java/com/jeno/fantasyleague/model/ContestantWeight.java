package com.jeno.fantasyleague.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "contestantweight")
public class ContestantWeight {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(targetEntity = Contestant.class, fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "contestant_id")
	private Contestant contestant;

	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "user_id")
	private User user;

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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
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

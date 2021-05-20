package com.jeno.fantasyleague.backend.model;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

@Embeddable
public class LeagueUserId implements java.io.Serializable {

	private User user;
	private League league;

	@ManyToOne
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@ManyToOne
	public League getLeague() {
		return league;
	}

	public void setLeague(League league) {
		this.league = league;
	}
}

package com.jeno.fantasyleague.ui.main.views.league.gridlayout;

import com.jeno.fantasyleague.backend.model.League;
import com.jeno.fantasyleague.backend.model.LeagueUser;
import com.jeno.fantasyleague.backend.model.User;

import java.util.List;
import java.util.Objects;

public class LeagueBean {

	private final League league;
	private final User loggedInUser;
	private final List<LeagueUser> leagueUsers;
	private final List<User> leagueOwners;

	public LeagueBean(League league, User loggedInUser, List<LeagueUser> leagueUsers, List<User> leagueOwners) {
		this.league = league;
		this.loggedInUser = loggedInUser;
		this.leagueUsers = leagueUsers;
		this.leagueOwners = leagueOwners;
	}

	public LeagueUser getLoggedInLeagueUser() {
		return leagueUsers.stream()
				.filter(lu -> Objects.equals(loggedInUser.getId(), lu.getUser().getId()))
				.findFirst()
				.orElseThrow(() -> new RuntimeException("Impossible for a logged in user " + loggedInUser.getUsername() + " not to be part of the leagueUser list"));
	}

	public League getLeague() {
		return league;
	}

	public User getLoggedInUser() {
		return loggedInUser;
	}

	public List<LeagueUser> getLeagueUsers() {
		return leagueUsers;
	}

	public List<User> getLeagueOwners() {
		return leagueOwners;
	}

	public static class Builder {

		private League league;
		private User loggedInUser;
		private List<LeagueUser> leagueUsers;
		private List<User> leagueOwners;

		public LeagueBean createLeagueBean() {
			return new LeagueBean(league, loggedInUser, leagueUsers, leagueOwners);
		}

		public Builder setLeague(League league) {
			this.league = league;
			return this;
		}

		public Builder setLoggedInUser(User loggedInUser) {
			this.loggedInUser = loggedInUser;
			return this;
		}

		public Builder setLeagueUsers(List<LeagueUser> leagueUsers) {
			this.leagueUsers = leagueUsers;
			return this;
		}

		public Builder setLeagueOwners(List<User> leagueOwners) {
			this.leagueOwners = leagueOwners;
			return this;
		}
	}
}

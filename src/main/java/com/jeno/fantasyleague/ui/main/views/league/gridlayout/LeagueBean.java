package com.jeno.fantasyleague.ui.main.views.league.gridlayout;

import java.util.List;

import com.jeno.fantasyleague.backend.model.League;
import com.jeno.fantasyleague.backend.model.User;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.overview.UserTotalScoreBean;

public class LeagueBean {

	private final League league;
	private final List<UserTotalScoreBean> scores;
	private final User loggedInUser;
	private final List<User> leagueUsers;
	private final List<User> leagueOwners;

	public LeagueBean(League league, List<UserTotalScoreBean> scores, User loggedInUser, List<User> leagueUsers, List<User> leagueOwners) {
		this.league = league;
		this.scores = scores;
		this.loggedInUser = loggedInUser;
		this.leagueUsers = leagueUsers;
		this.leagueOwners = leagueOwners;
	}

	public League getLeague() {
		return league;
	}

	public List<UserTotalScoreBean> getScores() {
		return scores;
	}

	public User getLoggedInUser() {
		return loggedInUser;
	}

	public List<User> getLeagueUsers() {
		return leagueUsers;
	}

	public List<User> getLeagueOwners() {
		return leagueOwners;
	}

	public static class Builder {

		private League league;
		private List<UserTotalScoreBean> scores;
		private User loggedInUser;
		private List<User> leagueUsers;
		private List<User> leagueOwners;

		public LeagueBean createLeagueBean() {
			return new LeagueBean(league, scores, loggedInUser, leagueUsers, leagueOwners);
		}

		public Builder setLeague(League league) {
			this.league = league;
			return this;
		}

		public Builder setScores(List<UserTotalScoreBean> scores) {
			this.scores = scores;
			return this;
		}

		public Builder setLoggedInUser(User loggedInUser) {
			this.loggedInUser = loggedInUser;
			return this;
		}

		public Builder setLeagueUsers(List<User> leagueUsers) {
			this.leagueUsers = leagueUsers;
			return this;
		}

		public Builder setLeagueOwners(List<User> leagueOwners) {
			this.leagueOwners = leagueOwners;
			return this;
		}
	}
}

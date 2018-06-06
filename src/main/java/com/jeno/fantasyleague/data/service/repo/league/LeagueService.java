package com.jeno.fantasyleague.data.service.repo.league;

import com.jeno.fantasyleague.model.League;
import com.jeno.fantasyleague.model.Prediction;
import com.jeno.fantasyleague.model.User;

public interface LeagueService {

	League addLeague(League league, User user);

	void addUserToLeague(League league, User user);

	UserLeagueScore getTotalLeagueScoreForUser(League league, User user);

	double getPredictionScoreForUser(League league, Prediction prediction, User user);
}

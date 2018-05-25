package com.jeno.fantasyleague.data.service.repo.league;

import java.util.Map;

import com.jeno.fantasyleague.data.service.leaguetemplates.worldcup2018.FifaWorldCup2018Stages;
import com.jeno.fantasyleague.model.League;
import com.jeno.fantasyleague.model.Prediction;
import com.jeno.fantasyleague.model.User;

public interface LeagueService {

	League addLeague(League league, User user);

	void addUserToLeague(League league, User user);

	Map<FifaWorldCup2018Stages, Double> getTotalLeagueScoreForUser(League league, User user);

	double getPredictionScoreForUser(League league, Prediction prediction, User user);
}

package com.jeno.fantasyleague.backend.data.service.repo.league;

import com.jeno.fantasyleague.backend.model.ContestantWeight;
import com.jeno.fantasyleague.backend.model.League;
import com.jeno.fantasyleague.backend.model.Prediction;
import com.jeno.fantasyleague.backend.model.User;

import java.util.List;
import java.util.Map;

public interface LeagueService {

	League addLeague(League league, User user);

	void addUserToLeague(League league, User user);

	List<UserLeagueScore> getTotalLeagueScores(League league);

	double getPredictionScoreForUser(League league, Prediction prediction, User user);

	Map<Long,Double> getPredictionScoresForUser(League league, List<Prediction> predictionsWithJoinedGames, List<ContestantWeight> contestantWeights);

	void addUserToDefaultLeague(User user);
}

package com.jeno.fantasyleague.data.service.repo.league;

import java.util.List;
import java.util.Map;

import com.jeno.fantasyleague.model.ContestantWeight;
import com.jeno.fantasyleague.model.League;
import com.jeno.fantasyleague.model.Prediction;
import com.jeno.fantasyleague.model.User;

public interface LeagueService {

	League addLeague(League league, User user);

	void addUserToLeague(League league, User user);

	List<UserLeagueScore> getTotalLeagueScores(League league);

	double getPredictionScoreForUser(League league, Prediction prediction, User user);

	Map<Long,Double> getPredictionScoresForUser(League league, List<Prediction> predictionsWithJoinedGames, List<ContestantWeight> contestantWeights, User user);
}

package com.jeno.fantasyleague.data.service.leaguetemplates;

import java.util.List;
import java.util.Map;

import com.jeno.fantasyleague.data.service.repo.league.UserLeagueScore;
import com.jeno.fantasyleague.model.ContestantWeight;
import com.jeno.fantasyleague.model.League;
import com.jeno.fantasyleague.model.Prediction;
import com.jeno.fantasyleague.model.User;

public interface LeagueTemplateService {

	void run(League newLeague, User user) throws TemplateException;

	LeagueSettingRenderer getLeagueSettingRenderer();

	List<UserLeagueScore> calculateTotalUserScores(League league);

	double calculateScoreOfPrediction(League league, Prediction prediction, User user);

	Map<Long,Double> calculateScoresForUser(League league, List<Prediction> predictionsWithJoinedGames, List<ContestantWeight> contestantWeights, User user);

}

package com.jeno.fantasyleague.data.service.leaguetemplates;

import com.jeno.fantasyleague.data.service.repo.league.UserLeagueScore;
import com.jeno.fantasyleague.model.League;
import com.jeno.fantasyleague.model.Prediction;
import com.jeno.fantasyleague.model.User;

public interface LeagueTemplateService {

	void run(League newLeague, User user) throws TemplateException;

	LeagueSettingRenderer getLeagueSettingRenderer();

	UserLeagueScore calculateTotalUserScore(League league, User user);

	double calculateScoreOfPrediction(League league, Prediction prediction, User user);

}

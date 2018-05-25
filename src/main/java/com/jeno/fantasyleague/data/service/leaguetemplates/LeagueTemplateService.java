package com.jeno.fantasyleague.data.service.leaguetemplates;

import java.util.Map;

import com.jeno.fantasyleague.data.service.leaguetemplates.worldcup2018.FifaWorldCup2018Stages;
import com.jeno.fantasyleague.model.League;
import com.jeno.fantasyleague.model.Prediction;
import com.jeno.fantasyleague.model.User;

public interface LeagueTemplateService {

	void run(League newLeague, User user) throws TemplateException;

	LeagueSettingRenderer getLeagueSettingRenderer();

	Map<FifaWorldCup2018Stages, Double> calculateTotalUserScore(League league, User user);

	double calculateScoreOfPrediction(League league, Prediction prediction, User user);

}

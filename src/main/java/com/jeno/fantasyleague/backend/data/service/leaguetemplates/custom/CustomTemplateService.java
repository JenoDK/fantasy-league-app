package com.jeno.fantasyleague.backend.data.service.leaguetemplates.custom;

import com.google.common.collect.Lists;
import com.jeno.fantasyleague.backend.data.service.leaguetemplates.LeagueSettingRenderer;
import com.jeno.fantasyleague.backend.data.service.leaguetemplates.LeagueTemplateService;
import com.jeno.fantasyleague.backend.data.service.leaguetemplates.TemplateException;
import com.jeno.fantasyleague.backend.data.service.repo.league.UserLeagueScore;
import com.jeno.fantasyleague.backend.model.ContestantWeight;
import com.jeno.fantasyleague.backend.model.League;
import com.jeno.fantasyleague.backend.model.Prediction;
import com.jeno.fantasyleague.backend.model.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

public class CustomTemplateService implements LeagueTemplateService {

	@Autowired
	private CustomSettingRenderer customSettingRenderer;

	@Override
	public void run(League newLeague, User user) throws TemplateException {

	}

	@Override
	public LeagueSettingRenderer getLeagueSettingRenderer() {
		return customSettingRenderer;
	}

	@Override
	public List<UserLeagueScore> calculateTotalUserScores(League league) {
		return Lists.newArrayList();
	}

	@Override
	public double calculateScoreOfPrediction(League league, Prediction prediction, User user) {
		return 0;
	}

	@Override
	public Map<Long, Double> calculateScoresForUser(League league, List<Prediction> predictionsWithJoinedGames, List<ContestantWeight> contestantWeights) {
		return null;
	}

}

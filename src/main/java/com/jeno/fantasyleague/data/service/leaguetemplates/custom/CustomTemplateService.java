package com.jeno.fantasyleague.data.service.leaguetemplates.custom;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jeno.fantasyleague.data.service.leaguetemplates.LeagueSettingRenderer;
import com.jeno.fantasyleague.data.service.leaguetemplates.LeagueTemplateService;
import com.jeno.fantasyleague.data.service.leaguetemplates.TemplateException;
import com.jeno.fantasyleague.data.service.repo.league.UserLeagueScore;
import com.jeno.fantasyleague.model.ContestantWeight;
import com.jeno.fantasyleague.model.League;
import com.jeno.fantasyleague.model.Prediction;
import com.jeno.fantasyleague.model.User;
import org.springframework.beans.factory.annotation.Autowired;

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
	public Map<Long, Double> calculateScoresForUser(League league, List<Prediction> predictionsWithJoinedGames, List<ContestantWeight> contestantWeights, User user) {
		return Maps.newHashMap();
	}

}

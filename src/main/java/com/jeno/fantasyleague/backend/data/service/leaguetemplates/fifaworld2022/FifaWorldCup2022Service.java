package com.jeno.fantasyleague.backend.data.service.leaguetemplates.fifaworld2022;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.jeno.fantasyleague.backend.data.service.leaguetemplates.FootballLeagueScoreHelper;
import com.jeno.fantasyleague.backend.data.service.leaguetemplates.FootballSettingsRenderer;
import com.jeno.fantasyleague.backend.data.service.leaguetemplates.LeagueSettingRenderer;
import com.jeno.fantasyleague.backend.data.service.leaguetemplates.LeagueTemplateService;
import com.jeno.fantasyleague.backend.data.service.leaguetemplates.TemplateException;
import com.jeno.fantasyleague.backend.data.service.repo.league.UserLeagueScore;
import com.jeno.fantasyleague.backend.model.ContestantWeight;
import com.jeno.fantasyleague.backend.model.League;
import com.jeno.fantasyleague.backend.model.Prediction;
import com.jeno.fantasyleague.backend.model.User;

@Component
@Transactional(rollbackFor = Exception.class)
public class FifaWorldCup2022Service implements LeagueTemplateService {

	@Autowired
	private FifaWorldCup2022Initializer fifaWorldCup2022Initializer;
	@Autowired
	private FootballSettingsRenderer settingRenderer;
	@Autowired
	private FootballLeagueScoreHelper scoreHelper;

	@Override
	public void run(League newLeague, User user) throws TemplateException {
		fifaWorldCup2022Initializer.addNewLeague(newLeague, user);
	}

	@Override
	public LeagueSettingRenderer getLeagueSettingRenderer() {
		return settingRenderer;
	}

	@Override
	public List<UserLeagueScore> calculateTotalUserScores(League league) {
		return scoreHelper.calculateTotalUserScores(league);
	}

	@Override
	public double calculateScoreOfPrediction(League league, Prediction prediction, User user) {
		return scoreHelper.calculateScoreOfPrediction(league, prediction, user);
	}

	@Override
	public Map<Long, Double> calculateScoresForUser(League league, List<Prediction> predictionsWithJoinedGames, List<ContestantWeight> contestantWeights) {
		Map<Long, Integer> contestantWeightsMap = contestantWeights.stream()
				.collect(Collectors.toMap(ContestantWeight::getContestant_fk, ContestantWeight::getWeight));
		return scoreHelper.calculateScoresForUser(league, predictionsWithJoinedGames, contestantWeightsMap);
	}
}

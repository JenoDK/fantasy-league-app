package com.jeno.fantasyleague.backend.data.service.leaguetemplates.eufaeuro2024;

import com.jeno.fantasyleague.backend.data.service.leaguetemplates.*;
import com.jeno.fantasyleague.backend.data.service.repo.league.UserLeagueScore;
import com.jeno.fantasyleague.backend.model.ContestantWeight;
import com.jeno.fantasyleague.backend.model.League;
import com.jeno.fantasyleague.backend.model.Prediction;
import com.jeno.fantasyleague.backend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Transactional(rollbackFor = Exception.class)
public class UefaEuro2024Service implements LeagueTemplateService {

	@Autowired
	private FootballSettingsRenderer uefaEuro2024SettingRenderer;
	@Autowired
	private UefaEuro2024Initializer uefaEuro2024Initializer;
	@Autowired
	private FootballLeagueScoreHelper footballLeagueScoreHelper;

	@Override
	public LeagueSettingRenderer getLeagueSettingRenderer() {
		return uefaEuro2024SettingRenderer;
	}

	@Override
	public List<UserLeagueScore> calculateTotalUserScores(League league) {
		return footballLeagueScoreHelper.calculateTotalUserScores(league);
	}

	@Override
	public double calculateScoreOfPrediction(League league, Prediction prediction, User user) {
		return footballLeagueScoreHelper.calculateScoreOfPrediction(league, prediction, user);
	}

	@Override
	public Map<Long, Double> calculateScoresForUser(
			League league,
			List<Prediction> predictionsWithJoinedGames,
			List<ContestantWeight> contestantWeights) {
		Map<Long, Integer> contestantWeightsMap = contestantWeights.stream()
				.collect(Collectors.toMap(ContestantWeight::getContestant_fk, ContestantWeight::getWeight));
		return footballLeagueScoreHelper.calculateScoresForUser(league, predictionsWithJoinedGames, contestantWeightsMap);
	}

	@Override
	public void run(League newLeague, User user) throws TemplateException {
		uefaEuro2024Initializer.addNewLeague(newLeague, user);
	}

}

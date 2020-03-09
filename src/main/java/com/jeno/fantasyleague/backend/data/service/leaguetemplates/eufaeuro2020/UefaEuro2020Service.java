package com.jeno.fantasyleague.backend.data.service.leaguetemplates.eufaeuro2020;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.jeno.fantasyleague.backend.data.service.leaguetemplates.LeagueSettingRenderer;
import com.jeno.fantasyleague.backend.data.service.leaguetemplates.LeagueTemplateService;
import com.jeno.fantasyleague.backend.data.service.leaguetemplates.TemplateException;
import com.jeno.fantasyleague.backend.data.service.repo.league.UserLeagueScore;
import com.jeno.fantasyleague.backend.model.ContestantWeight;
import com.jeno.fantasyleague.backend.model.League;
import com.jeno.fantasyleague.backend.model.Prediction;
import com.jeno.fantasyleague.backend.model.User;

@Transactional(rollbackFor = Exception.class)
public class UefaEuro2020Service implements LeagueTemplateService {

	@Autowired
	private UefaEuro2020SettingRenderer uefaEuro2020SettingRenderer;
	@Autowired
	private UefaEuro2020Initializer uefaEuro2020Initializer;
	@Autowired
	private UefaEuro2020ScoreHelper uefaEuro2020ScoreHelper;

	@Override
	public LeagueSettingRenderer getLeagueSettingRenderer() {
		return uefaEuro2020SettingRenderer;
	}

	@Override
	public List<UserLeagueScore> calculateTotalUserScores(League league) {
		return uefaEuro2020ScoreHelper.calculateTotalUserScores(league);
	}

	@Override
	public double calculateScoreOfPrediction(League league, Prediction prediction, User user) {
		return uefaEuro2020ScoreHelper.calculateScoreOfPrediction(league, prediction, user);
	}

	@Override
	public Map<Long, Double> calculateScoresForUser(
			League league,
			List<Prediction> predictionsWithJoinedGames,
			List<ContestantWeight> contestantWeights) {
		Map<Long, Integer> contestantWeightsMap = contestantWeights.stream()
				.collect(Collectors.toMap(ContestantWeight::getContestant_fk, ContestantWeight::getWeight));
		return uefaEuro2020ScoreHelper.calculateScoresForUser(league, predictionsWithJoinedGames, contestantWeightsMap);
	}

	@Override
	public void run(League newLeague, User user) throws TemplateException {
		uefaEuro2020Initializer.addNewLeague(newLeague, user);
	}

}

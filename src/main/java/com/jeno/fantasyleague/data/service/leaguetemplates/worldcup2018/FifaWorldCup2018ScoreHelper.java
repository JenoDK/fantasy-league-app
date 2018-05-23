package com.jeno.fantasyleague.data.service.leaguetemplates.worldcup2018;

import java.util.List;
import java.util.Objects;

import com.jeno.fantasyleague.data.repository.ContestantWeightRepository;
import com.jeno.fantasyleague.data.repository.LeagueSettingRepository;
import com.jeno.fantasyleague.data.repository.PredictionRepository;
import com.jeno.fantasyleague.model.ContestantWeight;
import com.jeno.fantasyleague.model.League;
import com.jeno.fantasyleague.model.LeagueSetting;
import com.jeno.fantasyleague.model.Prediction;
import com.jeno.fantasyleague.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FifaWorldCup2018ScoreHelper {

	@Autowired
	private ContestantWeightRepository contestantWeightRepository;
	@Autowired
	private PredictionRepository predictionRepository;
	@Autowired
	private LeagueSettingRepository leagueSettingRepository;

	public double calculateUserScore(League league, User user) {
		List<Prediction> predictionsWithJoinedGames = predictionRepository.findByLeagueAndUserAndJoinGames(league, user);
		List<ContestantWeight> weights = contestantWeightRepository.findByUserAndLeagueAndJoinContestant(user, league);
		return predictionsWithJoinedGames.stream()
				.mapToDouble(prediction -> calculateScoreOfPrediction(prediction, league, weights))
				.sum();
	}

	private double calculateScoreOfPrediction(Prediction prediction, League league, List<ContestantWeight> weights) {
		Integer gameHomeScore = prediction.getGame().getHome_team_score();
		Integer gameAwayScore = prediction.getGame().getAway_team_score();
		Integer predictionHomeScore = prediction.getHome_team_score();
		Integer predictionAwayScore = prediction.getAway_team_score();

		// If everything is non null we have a valid prediction & game combination
		if (Objects.nonNull(gameHomeScore) && Objects.nonNull(gameAwayScore) && Objects.nonNull(predictionHomeScore) && Objects.nonNull(predictionAwayScore)) {
			FifaWorldCup2018Stages stage = FifaWorldCup2018Stages.valueOf(prediction.getGame().getStage());
			Integer gameScore;
			// Correct score
			if (Objects.equals(gameHomeScore, predictionHomeScore) && Objects.equals(gameAwayScore, predictionAwayScore)) {
				gameScore = findAllCorrectSetting(league, stage);
			// Wrong score, correct result
			} else if (Objects.nonNull(prediction.getWinner()) &&
					Objects.nonNull(prediction.getGame().getWinner()) &&
					Objects.equals(prediction.getWinner().getId(), prediction.getGame().getWinner().getId())) {
				gameScore = findWrongScoreSetting(league, stage);
			// All wrong
			} else {
				gameScore = findAllWrongSetting(league, stage);
			}

			float totalScore = gameScore;

			if (Objects.nonNull(prediction.getGame().getWinner())) {
				Integer userWeightForWinner = weights.stream()
						.filter(weight -> weight.getContestant().getId().equals(prediction.getGame().getWinner().getId()))
						.findFirst()
						.map(ContestantWeight::getWeight)
						.get();

				float powerIndexCoef = 1f / (prediction.getGame().getWinner().getPower_index() / 100f);
				float userWeightCoef = 1f + (userWeightForWinner / 100f);
				totalScore = totalScore * powerIndexCoef * userWeightCoef;
			}

			return totalScore;
		} else {
			return 0f;
		}
	}

	public Integer findAllCorrectSetting(League league, FifaWorldCup2018Stages stage) {
		return findSetting(league, stage, FifaWorldCup2018SettingRenderer.ALL_CORRECT);
	}

	public Integer findWrongScoreSetting(League league, FifaWorldCup2018Stages stage) {
		return findSetting(league, stage, FifaWorldCup2018SettingRenderer.WRONG_SCORE);
	}

	public Integer findAllWrongSetting(League league, FifaWorldCup2018Stages stage) {
		return findSetting(league, stage, FifaWorldCup2018SettingRenderer.ALL_WRONG);
	}

	public Integer findSetting(League league, FifaWorldCup2018Stages stage, String type) {
		return leagueSettingRepository.findByLeagueAndName(league, stage.getName() + type)
				.map(LeagueSetting::getValue)
				.map(value -> Integer.valueOf(value))
				.get();
	}

}
package com.jeno.fantasyleague.backend.data.service.leaguetemplates;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Maps;
import com.jeno.fantasyleague.backend.data.repository.ContestantWeightRepository;
import com.jeno.fantasyleague.backend.data.repository.LeagueSettingRepository;
import com.jeno.fantasyleague.backend.data.repository.LeagueUserRepository;
import com.jeno.fantasyleague.backend.data.repository.PredictionRepository;
import com.jeno.fantasyleague.backend.data.service.repo.league.UserLeagueScore;
import com.jeno.fantasyleague.backend.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class FootballLeagueScoreHelper {

	@Autowired
	private ContestantWeightRepository contestantWeightRepository;
	@Autowired
	private PredictionRepository predictionRepository;
	@Autowired
	private LeagueUserRepository leagueUserRepository;
	@Autowired
	private LeagueSettingRepository leagueSettingRepository;

	public List<UserLeagueScore> calculateTotalUserScores(League league) {
		ArrayListMultimap<Long, Prediction> predictionsPerUser = ArrayListMultimap.create();
		ArrayListMultimap<Long, ContestantWeight> contestantWeightsPerUser = ArrayListMultimap.create();
		List<User> leagueUsers = leagueUserRepository.findByLeague(league).stream().map(LeagueUser::getUser).collect(Collectors.toList());
		Map<String, LeagueSetting> settingMap = leagueSettingRepository.findByLeague(league).stream()
				.collect(Collectors.toMap(LeagueSetting::getName, Function.identity()));
		predictionRepository.findByLeagueAndJoinGames(league).stream()
				.forEach(prediction -> predictionsPerUser.put(prediction.getUser_fk(), prediction));
		contestantWeightRepository.findByLeagueAndJoinContestant(league).stream()
				.forEach(contestantWeight -> contestantWeightsPerUser.put(contestantWeight.getUser_fk(), contestantWeight));
		return leagueUsers.stream()
				.map(user -> createUserLeagueScore(user, settingMap, predictionsPerUser.get(user.getId()), contestantWeightsPerUser.get(user.getId())))
				.collect(Collectors.toList());
	}

	private UserLeagueScore createUserLeagueScore(
			User user,
			Map<String, LeagueSetting> settingMap,
			List<Prediction> predictions,
			List<ContestantWeight> contestantWeights) {
		Map<SoccerCupStages, Double> scorePerStage = Maps.newHashMap();
		Map<Long, Double> scoresPerGame = Maps.newHashMap();
		Map<Long, Integer> contestantWeightsMap = contestantWeights.stream()
				.collect(Collectors.toMap(ContestantWeight::getContestant_fk, ContestantWeight::getWeight));
		predictions.forEach(prediction -> {
			SoccerCupStages stage = SoccerCupStages.valueOf(prediction.getGame().getStage());
			double scoreForPrediction = calculateScoreOfPrediction(prediction, settingMap, contestantWeightsMap);
			scoresPerGame.put(prediction.getGame_fk(), scoreForPrediction);
			if (scorePerStage.containsKey(stage)) {
				scorePerStage.put(stage, scorePerStage.get(stage) + scoreForPrediction);
			} else {
				scorePerStage.put(stage, scoreForPrediction);
			}
		});
		return new UserLeagueScore(user, scoresPerGame, scorePerStage, predictions);
	}

	public double calculateScoreOfPrediction(League league, Prediction prediction, User user) {
		Map<String, LeagueSetting> settingMap = leagueSettingRepository.findByLeague(league).stream()
				.collect(Collectors.toMap(LeagueSetting::getName, Function.identity()));
		Map<Long, Integer> contestantWeightsMap = contestantWeightRepository.findByUserAndLeague(user, league).stream()
				.collect(Collectors.toMap(ContestantWeight::getContestant_fk, ContestantWeight::getWeight));
		return calculateScoreOfPrediction(prediction, settingMap, contestantWeightsMap);
	}

	public Map<Long, Double> calculateScoresForUser(League league, List<Prediction> predictions, Map<Long, Integer> weightsForUserPerContestant) {
		Map<String, LeagueSetting> settingMap = leagueSettingRepository.findByLeague(league).stream()
				.collect(Collectors.toMap(LeagueSetting::getName, Function.identity()));
		return predictions.stream()
				.collect(Collectors.toMap(Prediction::getGame_fk, p -> calculateScoreOfPrediction(p, settingMap, weightsForUserPerContestant)));
	}

	private double calculateScoreOfPrediction(
			Prediction prediction,
			Map<String, LeagueSetting> settingMap,
			Map<Long, Integer> weightsPerContestant) {
		Integer gameHomeScore = prediction.getGame().getHome_team_score();
		Integer gameAwayScore = prediction.getGame().getAway_team_score();
		Integer predictionHomeScore = prediction.getHome_team_score();
		Integer predictionAwayScore = prediction.getAway_team_score();

		// If everything is non null we have a valid prediction & game combination
		if (Objects.nonNull(gameHomeScore) && Objects.nonNull(gameAwayScore) && Objects.nonNull(predictionHomeScore) && Objects.nonNull(predictionAwayScore)) {
			SoccerCupStages stage = SoccerCupStages.valueOf(prediction.getGame().getStage());
			Integer gameScore;

			boolean equalScores = Objects.equals(gameHomeScore, predictionHomeScore) && Objects.equals(gameAwayScore, predictionAwayScore);
			boolean correctWinner = Objects.nonNull(prediction.getWinner()) &&
					Objects.nonNull(prediction.getGame().getWinner()) &&
					Objects.equals(prediction.getWinner().getId(), prediction.getGame().getWinner().getId());

			boolean allCorrect;
			if (SoccerCupStages.GROUP_PHASE.equals(stage)) {
				allCorrect = equalScores;
			} else {
				allCorrect = equalScores && correctWinner;
			}
			// Correct score
			if (allCorrect) {
				gameScore = findAllCorrectSetting(settingMap, stage);
			// Wrong score, correct result
			} else {
				boolean equalsButWrongScoreForGroupStage =
						SoccerCupStages.GROUP_PHASE.toString().equals(prediction.getGame().getStage()) &&
						Objects.equals(gameHomeScore, gameAwayScore) && Objects.equals(predictionHomeScore, predictionAwayScore);
				if (correctWinner || equalsButWrongScoreForGroupStage) {
					gameScore = findWrongScoreSetting(settingMap, stage);
				// All wrong
				} else {
					gameScore = findAllWrongSetting(settingMap, stage);
				}
			}

			float totalScore = gameScore;

			if (Objects.nonNull(prediction.getGame().getWinner())) {
				Integer weight = weightsPerContestant.get(prediction.getGame().getWinner_fk());
				float userWeightCoef = 1f + (weight / 10f);
				totalScore = totalScore * userWeightCoef;
			} else {
				Integer homeTeamWeight = weightsPerContestant.get(prediction.getGame().getHome_team_fk());
				Integer awayTeamWeight = weightsPerContestant.get(prediction.getGame().getAway_team_fk());

				float homeTeamWeightValue = 1f + (homeTeamWeight / 10f);
				float awayTeamWeightValue = 1f + (awayTeamWeight / 10f);
				float averageWeightValue = (homeTeamWeightValue + awayTeamWeightValue) / 2;

				totalScore = totalScore * averageWeightValue;
			}

			return totalScore;
		} else {
			return 0f;
		}
	}

	public Float findPowerIndexMultiplier(League league) {
		return leagueSettingRepository.findByLeagueAndName(league, FootballSettingsRenderer.POWER_INDEX_MULTIPLIER)
				.map(LeagueSetting::getValue)
				.map(value -> Float.valueOf(value))
				.get();
	}

	public Integer findAllCorrectSetting(Map<String, LeagueSetting> leagueSettings, SoccerCupStages stage) {
		return Integer.valueOf(leagueSettings.get(stage.getName() + FootballSettingsRenderer.ALL_CORRECT).getValue());
	}

	public Integer findWrongScoreSetting(Map<String, LeagueSetting> leagueSettings, SoccerCupStages stage) {
		return Integer.valueOf(leagueSettings.get(stage.getName() + FootballSettingsRenderer.WRONG_SCORE).getValue());
	}

	public Integer findAllWrongSetting(Map<String, LeagueSetting> leagueSettings, SoccerCupStages stage) {
		return Integer.valueOf(leagueSettings.get(stage.getName() + FootballSettingsRenderer.ALL_WRONG).getValue());
	}

}

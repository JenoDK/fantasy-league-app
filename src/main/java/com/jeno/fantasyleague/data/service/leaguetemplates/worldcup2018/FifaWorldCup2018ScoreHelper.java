package com.jeno.fantasyleague.data.service.leaguetemplates.worldcup2018;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Maps;
import com.jeno.fantasyleague.data.repository.ContestantWeightRepository;
import com.jeno.fantasyleague.data.repository.LeagueRepository;
import com.jeno.fantasyleague.data.repository.LeagueSettingRepository;
import com.jeno.fantasyleague.data.repository.PredictionRepository;
import com.jeno.fantasyleague.data.service.repo.league.UserLeagueScore;
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
	private LeagueRepository leagueRepository;
	@Autowired
	private LeagueSettingRepository leagueSettingRepository;

	public List<UserLeagueScore> calculateTotalUserScores(League league) {
		ArrayListMultimap<Long, Prediction> predictionsPerUser = ArrayListMultimap.create();
		ArrayListMultimap<Long, ContestantWeight> contestantWeightsPerUser = ArrayListMultimap.create();
		List<User> leagueUsers = leagueRepository.fetchLeagueUsers(league.getId());
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
		Map<FifaWorldCup2018Stages, Double> scorePerStage = Maps.newHashMap();
		Map<LocalDateTime, Double> scorePerDate = Maps.newHashMap();
		predictions.forEach(prediction -> {
			FifaWorldCup2018Stages stage = FifaWorldCup2018Stages.valueOf(prediction.getGame().getStage());
			double scoreForPrediction = calculateScoreOfPrediction(prediction, settingMap, contestantWeights);
			if (scorePerStage.containsKey(stage)) {
				scorePerStage.put(stage, scorePerStage.get(stage) + scoreForPrediction);
			} else {
				scorePerStage.put(stage, scoreForPrediction);
			}
			LocalDateTime gameDate = prediction.getGame().getGameDateTime();
			if (scorePerDate.containsKey(gameDate)) {
				scorePerDate.put(gameDate, scorePerDate.get(gameDate) + scoreForPrediction);
			} else {
				scorePerDate.put(gameDate, scoreForPrediction);
			}
		});
		double score = 0d;
		for (LocalDateTime localDateTime : scorePerDate.keySet().stream()
				.sorted(LocalDateTime::compareTo)
				.collect(Collectors.toList())) {
			Double scoreFromDate = scorePerDate.get(localDateTime);
			score += scoreFromDate;
			scorePerDate.put(localDateTime, score);
		}
		return new UserLeagueScore(user, scorePerStage, scorePerDate);
	}

	public double calculateScoreOfPrediction(League league, Prediction prediction, User user) {
		Map<String, LeagueSetting> settingMap = leagueSettingRepository.findByLeague(league).stream()
				.collect(Collectors.toMap(LeagueSetting::getName, Function.identity()));
		return calculateScoreOfPrediction(prediction, settingMap, contestantWeightRepository.findByUserAndLeagueAndJoinContestant(user, league));
	}

	public Map<Long, Double> calculateScoresForUser(
			League league,
			List<Prediction> predictionsWithJoinedGames,
			List<ContestantWeight> contestantWeights,
			User user) {
		Map<String, LeagueSetting> settingMap = leagueSettingRepository.findByLeague(league).stream()
				.collect(Collectors.toMap(LeagueSetting::getName, Function.identity()));
		return predictionsWithJoinedGames.stream()
				.collect(Collectors.toMap(Prediction::getId, prediction -> calculateScoreOfPrediction(prediction, settingMap, contestantWeights)));
	}

	private double calculateScoreOfPrediction(
			Prediction prediction,
			Map<String, LeagueSetting> settingMap,
			List<ContestantWeight> weights) {
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
				gameScore = findAllCorrectSetting(settingMap, stage);
			// Wrong score, correct result
			} else {
				boolean correctWinner = Objects.nonNull(prediction.getWinner()) &&
						Objects.nonNull(prediction.getGame().getWinner()) &&
						Objects.equals(prediction.getWinner().getId(), prediction.getGame().getWinner().getId());

				boolean equalsButWrongScoreForGroupStage =
						FifaWorldCup2018Stages.GROUP_PHASE.toString().equals(prediction.getGame().getStage()) &&
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
				ContestantWeight contestantWeight = weights.stream()
						.filter(weight -> weight.getContestant().getId().equals(prediction.getGame().getWinner_fk()))
						.findFirst()
						.get();

				float userWeightCoef = 1f + (contestantWeight.getWeight() / 10f);
				totalScore = totalScore * userWeightCoef;
			} else {
				ContestantWeight homeTeamWeight = weights.stream()
						.filter(weight -> weight.getContestant().getId().equals(prediction.getGame().getHome_team_fk()))
						.findFirst()
						.get();
				ContestantWeight awayTeamWeight = weights.stream()
						.filter(weight -> weight.getContestant().getId().equals(prediction.getGame().getAway_team_fk()))
						.findFirst()
						.get();

				float homeTeamWeightValue = 1f + (homeTeamWeight.getWeight() / 10f);
				float awayTeamWeightValue = 1f + (awayTeamWeight.getWeight() / 10f);
				float averageWeightValue = (homeTeamWeightValue + awayTeamWeightValue) / 2;

				totalScore = totalScore * averageWeightValue;
			}

			return totalScore;
		} else {
			return 0f;
		}
	}

	public Float findPowerIndexMultiplier(League league) {
		return leagueSettingRepository.findByLeagueAndName(league, FifaWorldCup2018SettingRenderer.POWER_INDEX_MULTIPLIER)
				.map(LeagueSetting::getValue)
				.map(value -> Float.valueOf(value))
				.get();
	}

	public Integer findAllCorrectSetting(Map<String, LeagueSetting> leagueSettings, FifaWorldCup2018Stages stage) {
		return Integer.valueOf(leagueSettings.get(stage.getName() + FifaWorldCup2018SettingRenderer.ALL_CORRECT).getValue());
	}

	public Integer findWrongScoreSetting(Map<String, LeagueSetting> leagueSettings, FifaWorldCup2018Stages stage) {
		return Integer.valueOf(leagueSettings.get(stage.getName() + FifaWorldCup2018SettingRenderer.WRONG_SCORE).getValue());
	}

	public Integer findAllWrongSetting(Map<String, LeagueSetting> leagueSettings, FifaWorldCup2018Stages stage) {
		return Integer.valueOf(leagueSettings.get(stage.getName() + FifaWorldCup2018SettingRenderer.ALL_WRONG).getValue());
	}

}

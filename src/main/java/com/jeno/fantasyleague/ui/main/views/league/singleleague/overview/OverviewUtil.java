package com.jeno.fantasyleague.ui.main.views.league.singleleague.overview;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

import com.jeno.fantasyleague.data.service.leaguetemplates.worldcup2018.FifaWorldCup2018Stages;
import com.jeno.fantasyleague.model.League;
import com.jeno.fantasyleague.model.Prediction;
import com.jeno.fantasyleague.model.User;
import com.jeno.fantasyleague.resources.Resources;
import com.jeno.fantasyleague.util.GridUtil;

public class OverviewUtil {

	private OverviewUtil() {}

	public static boolean isHiddenForUser(User loggedInUser, League league, Prediction prediction) {
		if (loggedInUser.getId().equals(prediction.getUser().getId())) {
			return false;
		}

		if (FifaWorldCup2018Stages.GROUP_PHASE.toString().equals(prediction.getGame().getStage())) {
			return LocalDateTime.now().isBefore(league.getLeague_starting_date());
		} else {
			return LocalDateTime.now().isBefore(prediction.getGame().getGame_date_time());
		}
	}

	public static String getPredictionColumn(
			Integer homeTeamScore,
			Integer awayTeamScore,
			Optional<Boolean> homeTeamWonOptional,
			boolean isHidden,
			String predictionHiddenUtil) {
		if (isHidden) {
			return Resources.getMessage("hiddenUntil") + predictionHiddenUtil;
		} else {
			return getScoreWithWinner(homeTeamScore, awayTeamScore, homeTeamWonOptional);
		}
	}

	public static String getScoreWithWinner(Integer homeTeamScore, Integer awayTeamScore, Optional<Boolean> homeTeamWonOptional) {
		return homeTeamWonOptional
				.filter(homeTeamWon -> Objects.nonNull(homeTeamScore) && Objects.nonNull(awayTeamScore) && homeTeamScore.equals(awayTeamScore))
				.map(homeTeamWon -> {
					if (homeTeamWon) {
						return "(w) " + GridUtil.getScores(homeTeamScore, awayTeamScore);
					} else {
						return GridUtil.getScores(homeTeamScore, awayTeamScore) + " (w)";
					}
				})
				.orElse(GridUtil.getScores(homeTeamScore, awayTeamScore));
	}

	public static String getScoreFormatted(double score) {
		return new BigDecimal(score).setScale(2, BigDecimal.ROUND_HALF_UP).toEngineeringString();
	}

}

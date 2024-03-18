package com.jeno.fantasyleague.ui.main.views.league.singleleague.overview;

import com.jeno.fantasyleague.backend.model.Contestant;
import com.jeno.fantasyleague.backend.model.League;
import com.jeno.fantasyleague.backend.model.Prediction;
import com.jeno.fantasyleague.backend.model.User;
import com.jeno.fantasyleague.resources.Resources;
import com.jeno.fantasyleague.util.DateUtil;
import com.jeno.fantasyleague.util.LayoutUtil;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

public class OverviewUtil {

	private OverviewUtil() {}

	public static boolean isHiddenForUser(User loggedInUser, League league, Prediction prediction) {
		if (loggedInUser.getId().equals(prediction.getUser().getId())) {
			return false;
		}

		return DateUtil.nowIsBeforeUtcDateTime(prediction.getGame().getGameDateTime());
	}

	public static String getPredictionColumn(
			Integer homeTeamScore,
			Integer awayTeamScore,
			Optional<Boolean> homeTeamWonOptional,
			boolean isHidden,
			String predictionHiddenUtil) {
		if (Objects.isNull(homeTeamScore) || Objects.isNull(awayTeamScore)) {
			return Resources.getMessage("predictionIsIncomplete");
		} else if (isHidden) {
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
						return "(w) " + LayoutUtil.getScores(homeTeamScore, awayTeamScore);
					} else {
						return LayoutUtil.getScores(homeTeamScore, awayTeamScore) + " (w)";
					}
				})
				.orElse(LayoutUtil.getScores(homeTeamScore, awayTeamScore));
	}

	public static BigDecimal getScoreFormatted(double score) {
		return new BigDecimal(score).setScale(2, BigDecimal.ROUND_HALF_UP);
	}

	public static Component getTeamComponent(Contestant team, String placeholder, Integer teamWeight) {
		if (team != null) {
			HorizontalLayout teamLayout = LayoutUtil.createTeamLayout(team);
			if (teamWeight != null && teamWeight > 0) {
				Label stocksLabel = new Label(" - <b>Stocks: " + teamWeight + "</b>");
//				stocksLabel.addClassName(ValoTheme.LABEL_LIGHT);
				teamLayout.add(stocksLabel);
			}
			return teamLayout;
		} else {
			return new Label(placeholder);
		}
	}

}

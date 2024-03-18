package com.jeno.fantasyleague.ui.common.label;

import com.jeno.fantasyleague.resources.Resources;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.overview.OverviewUtil;

import java.util.Objects;
import java.util.Optional;

public class PredictionStatusLabel extends StatusLabel {

	private final String resourceKey;

	public PredictionStatusLabel(String resourceKey) {
		this.resourceKey = resourceKey;
	}

	public void setPredictionStatusText(
			Integer homeTeamPrediction,
			Integer awayTeamPrediction,
			Optional<Boolean> homeTeamPredictionIsWinner,
			boolean predictionIsHidden,
			String predictionHiddenUntil) {
		String prediction = OverviewUtil.getPredictionColumn(
				homeTeamPrediction,
				awayTeamPrediction,
				homeTeamPredictionIsWinner,
				predictionIsHidden,
				predictionHiddenUntil);
		if (Objects.isNull(homeTeamPrediction) || Objects.isNull(awayTeamPrediction)) {
			setErrorText(prediction);
		} else {
			reset();
			setText(Resources.getMessage(resourceKey, prediction));
		}
	}
}

package com.jeno.fantasyleague.ui.main.views.league.singleleague.matches;

import com.jeno.fantasyleague.backend.data.service.leaguetemplates.SoccerCupStages;
import com.jeno.fantasyleague.backend.model.Game;
import com.jeno.fantasyleague.resources.Resources;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.overview.OverviewUtil;
import com.jeno.fantasyleague.util.DateUtil;

public class MatchCardBean {

	private final MatchBean match;

	public MatchCardBean(MatchBean match) {
		this.match = match;
	}

	public String getDate() {
		return DateUtil.DATE_TIME_FORMATTER.format(getGame().getGameDateTime());
	}

	public String getPlace() {
		return getGame().getLocation();
	}

	public String getStage() {
		// For group stage we need a contestant, not every groups game has both contestants filled in. This is because some teams still need to come from the nations league by the end of march
		return SoccerCupStages.getLeagueStageTitle(getGame(), match.getHomeTeam() != null ? match.getHomeTeam() : match.getAwayTeam());
	}

	public String getPointsGained() {
		return Resources.getMessage("points") + ": " + OverviewUtil.getScoreFormatted(match.getPredictionScore());
	}

	private Game getGame() {
		return match.getPrediction().getGame();
	}

}

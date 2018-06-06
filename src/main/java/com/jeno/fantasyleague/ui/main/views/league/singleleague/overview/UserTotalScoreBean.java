package com.jeno.fantasyleague.ui.main.views.league.singleleague.overview;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

import com.jeno.fantasyleague.data.service.leaguetemplates.worldcup2018.FifaWorldCup2018Stages;
import com.jeno.fantasyleague.data.service.repo.league.UserLeagueScore;
import com.jeno.fantasyleague.model.User;

public class UserTotalScoreBean {

	private final User user;
	private final Map<FifaWorldCup2018Stages, Double> scores;
	private final Map<LocalDateTime, Double> scoresPerDate;

	public UserTotalScoreBean(User user, UserLeagueScore scores) {
		this.user = user;
		this.scores = scores.getScoresPerStage();
		this.scoresPerDate = scores.getScoresPerDate();
	}

	public User getUser() {
		return user;
	}

	public Map<LocalDateTime, Double> getScoresPerDate() {
		return scoresPerDate;
	}

	public double getTotalScore() {
		return scores.values().stream().mapToDouble(value -> value).sum();
	}

	public double getScore(FifaWorldCup2018Stages stage) {
		return scores.get(stage);
	}

}

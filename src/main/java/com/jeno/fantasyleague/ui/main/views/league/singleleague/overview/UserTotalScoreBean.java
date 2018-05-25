package com.jeno.fantasyleague.ui.main.views.league.singleleague.overview;

import java.util.Map;

import com.jeno.fantasyleague.data.service.leaguetemplates.worldcup2018.FifaWorldCup2018Stages;
import com.jeno.fantasyleague.model.User;

public class UserTotalScoreBean {

	private final User user;
	private final Map<FifaWorldCup2018Stages, Double> scores;

	public UserTotalScoreBean(User user, Map<FifaWorldCup2018Stages, Double> scores) {
		this.user = user;
		this.scores = scores;
	}

	public User getUser() {
		return user;
	}

	public double getTotalScore() {
		return scores.values().stream().mapToDouble(value -> value).sum();
	}

	public double getScore(FifaWorldCup2018Stages stage) {
		return scores.get(stage);
	}

}

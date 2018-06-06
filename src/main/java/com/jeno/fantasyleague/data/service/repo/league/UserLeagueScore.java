package com.jeno.fantasyleague.data.service.repo.league;

import java.time.LocalDateTime;
import java.util.Map;

import com.jeno.fantasyleague.data.service.leaguetemplates.worldcup2018.FifaWorldCup2018Stages;
import com.jeno.fantasyleague.model.User;

public class UserLeagueScore {

	private final User user;
	private final Map<FifaWorldCup2018Stages, Double> scoresPerStage;
	private final Map<LocalDateTime, Double> scoresPerDate;

	public UserLeagueScore(User user, Map<FifaWorldCup2018Stages, Double> scoresPerStage, Map<LocalDateTime, Double> scoresPerDate) {
		this.user = user;
		this.scoresPerStage = scoresPerStage;
		this.scoresPerDate = scoresPerDate;
	}

	public User getUser() {
		return user;
	}

	public Map<FifaWorldCup2018Stages, Double> getScoresPerStage() {
		return scoresPerStage;
	}

	public Map<LocalDateTime, Double> getScoresPerDate() {
		return scoresPerDate;
	}
}

package com.jeno.fantasyleague.backend.data.service.repo.league;

import java.time.LocalDateTime;
import java.util.Map;

import com.jeno.fantasyleague.backend.data.service.leaguetemplates.SoccerCupStages;
import com.jeno.fantasyleague.backend.model.User;

public class UserLeagueScore {

	private final User user;
	private final Map<SoccerCupStages, Double> scoresPerStage;
	private final Map<LocalDateTime, Double> scoresPerDate;

	public UserLeagueScore(User user, Map<SoccerCupStages, Double> scoresPerStage, Map<LocalDateTime, Double> scoresPerDate) {
		this.user = user;
		this.scoresPerStage = scoresPerStage;
		this.scoresPerDate = scoresPerDate;
	}

	public User getUser() {
		return user;
	}

	public Map<SoccerCupStages, Double> getScoresPerStage() {
		return scoresPerStage;
	}

	public Map<LocalDateTime, Double> getScoresPerDate() {
		return scoresPerDate;
	}
}

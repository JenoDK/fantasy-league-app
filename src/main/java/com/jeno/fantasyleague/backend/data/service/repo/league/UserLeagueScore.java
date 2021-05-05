package com.jeno.fantasyleague.backend.data.service.repo.league;

import java.util.List;
import java.util.Map;

import com.jeno.fantasyleague.backend.data.service.leaguetemplates.SoccerCupStages;
import com.jeno.fantasyleague.backend.model.Prediction;
import com.jeno.fantasyleague.backend.model.User;

public class UserLeagueScore {

	private final User user;
	private final Map<Long, Double> scoresPerGame;
	private final Map<SoccerCupStages, Double> scoresPerStage;
	private final List<Prediction> predictions;

	public UserLeagueScore(User user, Map<Long, Double> scoresPerGame, Map<SoccerCupStages, Double> scoresPerStage, List<Prediction> predictions) {
		this.user = user;
		this.scoresPerGame = scoresPerGame;
		this.scoresPerStage = scoresPerStage;
		this.predictions = predictions;
	}

	public User getUser() {
		return user;
	}

	public Map<Long, Double> getScoresPerGame() {
		return scoresPerGame;
	}

	public Map<SoccerCupStages, Double> getScoresPerStage() {
		return scoresPerStage;
	}

	public List<Prediction> getPredictions() {
		return predictions;
	}
}

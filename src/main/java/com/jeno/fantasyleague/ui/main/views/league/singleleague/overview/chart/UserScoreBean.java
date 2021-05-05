package com.jeno.fantasyleague.ui.main.views.league.singleleague.overview.chart;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.jeno.fantasyleague.backend.data.service.leaguetemplates.SoccerCupStages;
import com.jeno.fantasyleague.backend.data.service.repo.league.UserLeagueScore;
import com.jeno.fantasyleague.backend.model.Game;
import com.jeno.fantasyleague.backend.model.Prediction;
import com.jeno.fantasyleague.backend.model.User;
import com.jeno.fantasyleague.util.DecimalUtil;

public class UserScoreBean {

	private final UserLeagueScore userLeagueScore;
	private int position = 0;

	public UserScoreBean(UserLeagueScore userLeagueScore) {
		this.userLeagueScore = userLeagueScore;
	}

	public double getScore(SoccerCupStages stage) {
		return userLeagueScore.getScoresPerStage().get(stage);
	}

	public User getUser() {
		return userLeagueScore.getUser();
	}

	public List<Game> getGames() {
		return userLeagueScore.getPredictions().stream().map(Prediction::getGame).collect(Collectors.toList());
	}

	public List<Double> getScoresSorted(List<Long> gameIds) {
		return gameIds.stream()
				.map(id -> userLeagueScore.getScoresPerGame().get(id))
				.collect(Collectors.toList());
	}

	public Double getScoreForGames(List<Game> games) {
		return games.stream()
				.mapToDouble(g -> DecimalUtil.round(userLeagueScore.getScoresPerGame().get(g.getId()), 1))
				.sum();
	}

	public Double getScoreForGame(Game game) {
		return DecimalUtil.round(userLeagueScore.getScoresPerGame().get(game.getId()), 1);
	}

	public double getTotalScore() {
		return userLeagueScore.getScoresPerGame().values().stream().mapToDouble(value -> value).sum();
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public int getPosition() {
		return position;
	}

	public static List<UserScoreBean> transfer(List<UserLeagueScore> userLeagueScores) {
		List<UserScoreBean> beans = userLeagueScores.stream()
				.map(UserScoreBean::new)
				.sorted(Comparator.comparing(UserScoreBean::getTotalScore).reversed())
				.collect(Collectors.toList());
		int position = 1;
		for (UserScoreBean bean : beans) {
			bean.setPosition(position);
			position += 1;
		}
		return beans;
	}
}

package com.jeno.fantasyleague.ui.main.views.league.singleleague.overview.usertotalscore;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.jeno.fantasyleague.backend.data.service.leaguetemplates.SoccerCupStages;
import com.jeno.fantasyleague.backend.data.service.repo.league.UserLeagueScore;
import com.jeno.fantasyleague.backend.model.User;

public class UserTotalScoreBean {

	private final User user;
	private final Map<SoccerCupStages, Double> scores;
	private final Map<LocalDateTime, Double> scoresPerDate;
	private int position = 0;

	public UserTotalScoreBean(UserLeagueScore userLeagueScore) {
		this.user = userLeagueScore.getUser();
		this.scores = userLeagueScore.getScoresPerStage();
		this.scoresPerDate = userLeagueScore.getScoresPerDate();
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

	public double getScore(SoccerCupStages stage) {
		return scores.get(stage);
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public int getPosition() {
		return position;
	}

	public static List<UserTotalScoreBean> transfer(List<UserLeagueScore> userLeagueScores) {
		List<UserTotalScoreBean> beans = userLeagueScores.stream()
				.map(UserTotalScoreBean::new)
				.sorted(Comparator.comparing(UserTotalScoreBean::getTotalScore).reversed())
				.collect(Collectors.toList());
		int position = 1;
		for (UserTotalScoreBean bean : beans) {
			bean.setPosition(position);
			position += 1;
		}
		return beans;
	}
}

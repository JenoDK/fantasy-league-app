package com.jeno.fantasyleague.ui.main.views.league.singleleague.groups;

import com.jeno.fantasyleague.backend.model.Contestant;

public class GroupTeamBean {

	private final Contestant contestant;
	private final Integer goalsInGroup;
	private final Integer pointsInGroup;
	private final Integer predictedGoalsInGroup;
	private final Integer predictedPointsInGroup;

	public GroupTeamBean(Contestant contestant, Integer pointsInGroup, Integer goalsInGroup, Integer predictedGoalsInGroup, Integer predictedPointsInGroup) {
		this.contestant = contestant;
		this.pointsInGroup = pointsInGroup;
		this.goalsInGroup = goalsInGroup;
		this.predictedGoalsInGroup = predictedGoalsInGroup;
		this.predictedPointsInGroup = predictedPointsInGroup;
	}

	public Contestant getContestant() {
		return contestant;
	}

	public Integer getGoalsInGroup() {
		return goalsInGroup;
	}

	public Integer getPointsInGroup() {
		return pointsInGroup;
	}

	public Integer getPredictedGoalsInGroup() {
		return predictedGoalsInGroup;
	}

	public Integer getPredictedPointsInGroup() {
		return predictedPointsInGroup;
	}
}


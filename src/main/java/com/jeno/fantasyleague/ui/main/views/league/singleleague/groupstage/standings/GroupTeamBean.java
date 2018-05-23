package com.jeno.fantasyleague.ui.main.views.league.singleleague.groupstage.standings;

import com.jeno.fantasyleague.model.Contestant;

public class GroupTeamBean {

	private final Contestant contestant;
	private final Integer goalsInGroup;
	private final Integer pointsInGroup;

	public GroupTeamBean(Contestant contestant, Integer pointsInGroup, Integer goalsInGroup) {
		this.contestant = contestant;
		this.pointsInGroup = pointsInGroup;
		this.goalsInGroup = goalsInGroup;
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
}

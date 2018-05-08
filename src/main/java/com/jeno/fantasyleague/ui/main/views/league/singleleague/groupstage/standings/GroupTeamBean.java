package com.jeno.fantasyleague.ui.main.views.league.singleleague.groupstage.standings;

import com.jeno.fantasyleague.model.Contestant;

public class GroupTeamBean {

	private Contestant contestant;

	public GroupTeamBean(Contestant contestant) {
		this.contestant = contestant;
	}

	public Contestant getContestant() {
		return contestant;
	}

	public Integer getPoints_in_group() {
		return contestant.getPoints_in_group();
	}
}

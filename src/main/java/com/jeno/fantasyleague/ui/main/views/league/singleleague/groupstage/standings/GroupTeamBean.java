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
}

package com.jeno.fantasyleague.ui.main.views.league.singleleague.userscores;

import com.jeno.fantasyleague.model.League;
import com.jeno.fantasyleague.ui.main.views.league.SingleLeagueServiceProvider;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class UserScoresTab extends VerticalLayout {

	public UserScoresTab(League league, SingleLeagueServiceProvider singleLeagueServiceprovider) {
		super();
		setMargin(true);
		setSizeFull();

		addComponent(new Label("My Score: " + singleLeagueServiceprovider.getUserLeagueScore(league)));
	}

}

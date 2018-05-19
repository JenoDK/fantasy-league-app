package com.jeno.fantasyleague.ui.main.views.league.singleleague.knockoutstage;

import com.jeno.fantasyleague.model.League;
import com.jeno.fantasyleague.ui.main.views.league.SingleLeagueServiceProvider;
import com.vaadin.ui.HorizontalLayout;

public class RestFinalsGameLayout extends KnockoutGameLayout {

	public RestFinalsGameLayout(SingleLeagueServiceProvider singleLeagueServiceprovider, League league, KnockoutGameBean game) {
		super(singleLeagueServiceprovider, league, game);
	}

	@Override
	protected HorizontalLayout createHomeTeamComponent(KnockoutGameBean game) {
		return createTeamComponent(game.getContestant1(), game.getGame().getHome_team_placeholder());
	}

	@Override
	protected HorizontalLayout createAwayTeamComponent(KnockoutGameBean game) {
		return createTeamComponent(game.getContestant2(), game.getGame().getAway_team_placeholder());
	}

}

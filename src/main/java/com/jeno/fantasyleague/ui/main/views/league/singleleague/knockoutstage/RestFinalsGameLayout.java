package com.jeno.fantasyleague.ui.main.views.league.singleleague.knockoutstage;

import com.jeno.fantasyleague.backend.model.League;
import com.jeno.fantasyleague.ui.main.views.league.SingleLeagueServiceProvider;
import com.jeno.fantasyleague.util.GridUtil;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class RestFinalsGameLayout extends KnockoutGameLayout {

	public RestFinalsGameLayout(SingleLeagueServiceProvider singleLeagueServiceprovider, League league, KnockoutGameBean game) {
		super(singleLeagueServiceprovider, league, game);
	}

	@Override
	protected HorizontalLayout createHomeTeamComponent(KnockoutGameBean game) {
		return GridUtil.createTeamLayout(game.getHomeTeam(), game.getGame().getHome_team_placeholder());
	}

	@Override
	protected HorizontalLayout createAwayTeamComponent(KnockoutGameBean game) {
		return GridUtil.createTeamLayout(game.getAwayTeam(), game.getGame().getAway_team_placeholder());
	}

}

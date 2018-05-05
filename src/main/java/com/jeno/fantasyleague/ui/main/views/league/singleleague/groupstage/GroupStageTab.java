package com.jeno.fantasyleague.ui.main.views.league.singleleague.groupstage;

import com.jeno.fantasyleague.model.ContestantGroup;
import com.jeno.fantasyleague.model.League;
import com.jeno.fantasyleague.ui.main.views.league.SingleLeagueServiceProvider;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;

import java.util.Comparator;

public class GroupStageTab extends VerticalLayout {

	private TabSheet tabSheet;
	private VerticalLayout upcomingMatchesLayout;
	private VerticalLayout predictionsLayout;

	public GroupStageTab(League league, SingleLeagueServiceProvider singleLeagueService) {
		super();
		setMargin(false);
		setSpacing(false);

		tabSheet = new TabSheet();

		upcomingMatchesLayout = new VerticalLayout();
		upcomingMatchesLayout.setMargin(false);
		upcomingMatchesLayout.setSpacing(false);
		initGamesGrids(league, singleLeagueService);
		tabSheet.addTab(upcomingMatchesLayout, "Upcoming games");

		predictionsLayout = new VerticalLayout();
		tabSheet.addTab(predictionsLayout, "My Predictions");

		addComponent(tabSheet);
	}

	private void initGamesGrids(League league, SingleLeagueServiceProvider singleLeagueService) {
		singleLeagueService.getContestantGroupRepository().findByLeague(league).stream()
				.sorted(Comparator.comparing(ContestantGroup::getName))
				.forEach(group ->
					upcomingMatchesLayout.addComponent(new GroupLayout(singleLeagueService, league, group)));
	}

}

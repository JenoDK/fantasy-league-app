package com.jeno.fantasyleague.ui.main.views.league.singleleague.groupstage;

import com.jeno.fantasyleague.model.ContestantGroup;
import com.jeno.fantasyleague.model.League;
import com.jeno.fantasyleague.ui.main.views.league.SingleLeagueServiceProvider;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.groupstage.prediction.PredictionLayout;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.groupstage.standings.GroupStandingsLayout;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.groupstage.upcomingmatches.GroupLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;

import java.util.Comparator;
import java.util.List;

public class GroupStageTab extends VerticalLayout {

	private TabSheet tabSheet;
	private VerticalLayout upcomingMatchesLayout;
	private GridLayout groupStandingsLayout;
	private GridLayout predictionsLayout;

	public GroupStageTab(League league, SingleLeagueServiceProvider singleLeagueService) {
		super();
		setMargin(false);
		setSpacing(false);

		tabSheet = new TabSheet();

		upcomingMatchesLayout = new VerticalLayout();
		upcomingMatchesLayout.setMargin(false);
		upcomingMatchesLayout.setSpacing(false);

		List<ContestantGroup> groups = singleLeagueService.getContestantGroupRepository().findByLeague(league);

		groupStandingsLayout = new GridLayout(2, 1);
		predictionsLayout = new GridLayout(2, 1);

		initGroupLayouts(groups, league, singleLeagueService);

		tabSheet.addTab(upcomingMatchesLayout, "Upcoming games");
		tabSheet.addTab(groupStandingsLayout, "Group standings");
		tabSheet.addTab(predictionsLayout, "My Predictions");

		addComponent(tabSheet);
	}

	private void initGroupLayouts(
			List<ContestantGroup> groups,
			League league,
			SingleLeagueServiceProvider singleLeagueService) {
		groups.stream()
				.sorted(Comparator.comparing(ContestantGroup::getName))
				.forEach(group -> {
					upcomingMatchesLayout.addComponent(new GroupLayout(singleLeagueService, league, group));
					groupStandingsLayout.addComponent(new GroupStandingsLayout(singleLeagueService, group));
					predictionsLayout.addComponent(new PredictionLayout(singleLeagueService, league, group));
				});
	}

}

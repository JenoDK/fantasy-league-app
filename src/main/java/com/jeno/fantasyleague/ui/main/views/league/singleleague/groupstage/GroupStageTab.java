package com.jeno.fantasyleague.ui.main.views.league.singleleague.groupstage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.jeno.fantasyleague.model.ContestantGroup;
import com.jeno.fantasyleague.model.League;
import com.jeno.fantasyleague.ui.common.tabsheet.LazyTabSheet;
import com.jeno.fantasyleague.ui.main.views.league.SingleLeagueServiceProvider;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.groupstage.prediction.PredictionLayout;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.groupstage.standings.GroupStandingsLayout;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.groupstage.upcomingmatches.GroupLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.VerticalLayout;

public class GroupStageTab extends VerticalLayout {

	private LazyTabSheet tabSheet;

	public GroupStageTab(League league, SingleLeagueServiceProvider singleLeagueService) {
		super();
		setMargin(false);
		setSpacing(false);

		tabSheet = new LazyTabSheet();

		List<ContestantGroup> groups = singleLeagueService.getContestantGroupRepository().findByLeague(league).stream()
				.sorted(Comparator.comparing(ContestantGroup::getName))
				.collect(Collectors.toList());

		tabSheet.addLazyTab("upcomingGamesTab", "Upcoming games", () -> createUpcomingMatchesLayout(league, singleLeagueService, groups));
		tabSheet.addLazyTab("groupStandingsTab","Group standings", () -> createGroupStandingsLayout(singleLeagueService, groups));
		tabSheet.addLazyTab("predictionsTab","My Predictions", () -> createPredictionsLayout(league, singleLeagueService, groups));

		addComponent(tabSheet);
	}

	private GridLayout createPredictionsLayout(League league, SingleLeagueServiceProvider singleLeagueService, List<ContestantGroup> groups) {
		GridLayout predictionsLayout = new GridLayout(2, 1);
		groups.forEach(group -> predictionsLayout.addComponent(new PredictionLayout(singleLeagueService, league, group)));
		return predictionsLayout;
	}

	private GridLayout createGroupStandingsLayout(SingleLeagueServiceProvider singleLeagueService, List<ContestantGroup> groups) {
		GridLayout groupStandingsLayout = new GridLayout(2, 1);
		groups.forEach(group -> groupStandingsLayout.addComponent(new GroupStandingsLayout(singleLeagueService, group)));
		return groupStandingsLayout;
	}

	private VerticalLayout createUpcomingMatchesLayout(League league, SingleLeagueServiceProvider singleLeagueService, List<ContestantGroup> groups) {
		VerticalLayout upcomingMatchesLayout = new VerticalLayout();
		upcomingMatchesLayout.setMargin(false);
		upcomingMatchesLayout.setSpacing(false);
		groups.forEach(group -> upcomingMatchesLayout.addComponent(new GroupLayout(singleLeagueService, league, group)));
		return upcomingMatchesLayout;
	}

}

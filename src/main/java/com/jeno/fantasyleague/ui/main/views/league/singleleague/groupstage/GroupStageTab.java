package com.jeno.fantasyleague.ui.main.views.league.singleleague.groupstage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.jeno.fantasyleague.model.ContestantGroup;
import com.jeno.fantasyleague.model.League;
import com.jeno.fantasyleague.ui.main.views.league.SingleLeagueServiceProvider;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.groupstage.upcomingmatches.GroupLayout;
import com.vaadin.ui.VerticalLayout;

public class GroupStageTab extends VerticalLayout {

	public GroupStageTab(League league, SingleLeagueServiceProvider singleLeagueService) {
		super();
		setMargin(false);
		setSpacing(false);

		List<ContestantGroup> groups = singleLeagueService.getContestantGroupRepository().findByLeague(league).stream()
				.sorted(Comparator.comparing(ContestantGroup::getName))
				.collect(Collectors.toList());

		addComponent(createUpcomingMatchesLayout(league, singleLeagueService, groups));
	}

	private VerticalLayout createUpcomingMatchesLayout(League league, SingleLeagueServiceProvider singleLeagueService, List<ContestantGroup> groups) {
		VerticalLayout upcomingMatchesLayout = new VerticalLayout();
		upcomingMatchesLayout.setMargin(false);
		upcomingMatchesLayout.setSpacing(false);
		groups.forEach(group -> upcomingMatchesLayout.addComponent(new GroupLayout(singleLeagueService, league, group)));
		return upcomingMatchesLayout;
	}

}

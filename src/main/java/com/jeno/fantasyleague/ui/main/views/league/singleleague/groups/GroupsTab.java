package com.jeno.fantasyleague.ui.main.views.league.singleleague.groups;

import com.jeno.fantasyleague.backend.model.ContestantGroup;
import com.jeno.fantasyleague.backend.model.League;
import com.jeno.fantasyleague.ui.common.tabsheet.LazyTabComponent;
import com.jeno.fantasyleague.ui.main.views.league.SingleLeagueServiceProvider;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class GroupsTab extends LazyTabComponent {

	private List<GroupStandingsLayout> groupStandingsLayouts;

	public GroupsTab(League league, SingleLeagueServiceProvider singleLeagueServiceprovider) {
		super();
		setMargin(false);
		setPadding(false);
		setSpacing(false);

		List<ContestantGroup> groups = singleLeagueServiceprovider.getContestantGroupRepository().findByLeague(league).stream()
				.sorted(Comparator.comparing(ContestantGroup::getName))
				.collect(Collectors.toList());

		MenuBar filterBar = new MenuBar();
		filterBar.setWidthFull();
		filterBar.addThemeVariants(MenuBarVariant.LUMO_SMALL, MenuBarVariant.LUMO_PRIMARY);

		MenuItem refreshItem = filterBar.addItem(VaadinIcon.REFRESH.create());
		refreshItem.addClickListener(ignored -> groupStandingsLayouts.forEach(GroupStandingsLayout::refresh));

		add(filterBar);
		add(createUpcomingMatchesLayout(league, singleLeagueServiceprovider, groups));
	}

	private VerticalLayout createUpcomingMatchesLayout(League league, SingleLeagueServiceProvider singleLeagueService, List<ContestantGroup> groups) {
		VerticalLayout upcomingMatchesLayout = new VerticalLayout();
		upcomingMatchesLayout.setMargin(false);
		upcomingMatchesLayout.setPadding(false);
		upcomingMatchesLayout.setSpacing(false);
		groupStandingsLayouts = groups.stream().map(group -> new GroupStandingsLayout(singleLeagueService, league, group)).collect(Collectors.toList());
		groupStandingsLayouts.forEach(groupStandingsLayout -> upcomingMatchesLayout.add(groupStandingsLayout));
		return upcomingMatchesLayout;
	}

}

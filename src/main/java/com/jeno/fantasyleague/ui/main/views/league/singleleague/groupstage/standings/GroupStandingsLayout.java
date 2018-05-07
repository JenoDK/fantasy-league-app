package com.jeno.fantasyleague.ui.main.views.league.singleleague.groupstage.standings;

import com.jeno.fantasyleague.model.ContestantGroup;
import com.jeno.fantasyleague.ui.main.views.league.SingleLeagueServiceProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import java.util.Set;
import java.util.stream.Collectors;

public class GroupStandingsLayout extends VerticalLayout {

	public GroupStandingsLayout(SingleLeagueServiceProvider singleLeagueService, ContestantGroup group) {
		super();
		setSpacing(false);

		Label groupLabel = new Label(group.getName(), ContentMode.HTML);
		groupLabel.addStyleName(ValoTheme.LABEL_H3);

		GroupStandingsGrid groupStandingsGrid = new GroupStandingsGrid();
		groupStandingsGrid.setItems(fetchGroupContestants(singleLeagueService, group));

		Button refreshButton = new Button("Refresh", VaadinIcons.REFRESH);
		refreshButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
		refreshButton.addStyleName(ValoTheme.BUTTON_TINY);
		refreshButton.addClickListener(ignored ->
			groupStandingsGrid.setItems(fetchGroupContestants(singleLeagueService, group)));

		HorizontalLayout titleLayout = new HorizontalLayout();
		titleLayout.setWidth(500, Unit.PIXELS);
		titleLayout.setSpacing(true);
		titleLayout.addComponent(groupLabel);
		titleLayout.addComponent(refreshButton);
		titleLayout.setComponentAlignment(refreshButton, Alignment.MIDDLE_RIGHT);
		addComponent(titleLayout);
		addComponent(groupStandingsGrid);
	}

	public Set<GroupTeamBean> fetchGroupContestants(SingleLeagueServiceProvider singleLeagueService, ContestantGroup group) {
		return singleLeagueService.getContestantGroupRepository().fetchGroupContestants(group.getId()).stream()
				.map(GroupTeamBean::new)
				.collect(Collectors.toSet());
	}
}

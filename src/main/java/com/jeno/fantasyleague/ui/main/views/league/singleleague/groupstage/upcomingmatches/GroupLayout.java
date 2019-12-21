package com.jeno.fantasyleague.ui.main.views.league.singleleague.groupstage.upcomingmatches;

import com.jeno.fantasyleague.backend.model.ContestantGroup;
import com.jeno.fantasyleague.backend.model.League;
import com.jeno.fantasyleague.ui.main.views.league.SingleLeagueServiceProvider;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.groupstage.standings.GroupStandingsLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class GroupLayout extends VerticalLayout {


	public GroupLayout(SingleLeagueServiceProvider singleLeagueService, League league, ContestantGroup group) {
		super();
		setSpacing(false);

		HorizontalLayout gridWrapper = new HorizontalLayout();
		gridWrapper.setWidth("100%");
		GamesLayout gamesLayout = new GamesLayout(singleLeagueService, league, group);
		GroupStandingsLayout groupLayout = new GroupStandingsLayout(singleLeagueService, league, group);
		gridWrapper.add(gamesLayout);
		gridWrapper.add(groupLayout);
		gridWrapper.setFlexGrow(7, gamesLayout);
		gridWrapper.setFlexGrow(3, groupLayout);

		Label groupLabel = new Label(group.getName());
//		groupLabel.addClassName(ValoTheme.LABEL_H2);

		Button refreshButton = new Button(VaadinIcon.REFRESH.create());
//		refreshButton.addClassName(ValoTheme.BUTTON_TINY);
//		refreshButton.addClassName(ValoTheme.BUTTON_ICON_ONLY);
		refreshButton.addClickListener(ignored -> {
			gamesLayout.refresh();
			groupLayout.refresh();
		});

		HorizontalLayout titleLayout = new HorizontalLayout();
		titleLayout.setWidth("100%");
		titleLayout.add(groupLabel);
		titleLayout.add(refreshButton);
		titleLayout.setFlexGrow(1, groupLabel);
		titleLayout.setFlexGrow(1, refreshButton);
//		titleLayout.setComponentAlignment(refreshButton, Alignment.MIDDLE_RIGHT);

		add(titleLayout);
		add(gridWrapper);
	}

}

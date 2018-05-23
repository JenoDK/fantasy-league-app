package com.jeno.fantasyleague.ui.main.views.league.singleleague.groupstage.upcomingmatches;

import com.jeno.fantasyleague.model.ContestantGroup;
import com.jeno.fantasyleague.model.League;
import com.jeno.fantasyleague.ui.main.views.league.SingleLeagueServiceProvider;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.groupstage.standings.GroupStandingsLayout;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class GroupLayout extends VerticalLayout {


	public GroupLayout(SingleLeagueServiceProvider singleLeagueService, League league, ContestantGroup group) {
		super();
		setSpacing(false);

		HorizontalLayout gridWrapper = new HorizontalLayout();
		gridWrapper.setWidth(100f, Unit.PERCENTAGE);
		GamesLayout gamesLayout = new GamesLayout(singleLeagueService, league, group);
		GroupStandingsLayout groupLayout = new GroupStandingsLayout(singleLeagueService, league, group);
		gridWrapper.addComponent(gamesLayout);
		gridWrapper.addComponent(groupLayout);
		gridWrapper.setExpandRatio(gamesLayout, 7);
		gridWrapper.setExpandRatio(groupLayout, 3);

		Label groupLabel = new Label(group.getName(), ContentMode.HTML);
		groupLabel.addStyleName(ValoTheme.LABEL_H2);

		Button refreshButton = new Button(VaadinIcons.REFRESH);
		refreshButton.addStyleName(ValoTheme.BUTTON_TINY);
		refreshButton.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
		refreshButton.addClickListener(ignored -> {
			gamesLayout.refresh();
			groupLayout.refresh();
		});

		HorizontalLayout titleLayout = new HorizontalLayout();
		titleLayout.setWidth(100f, Unit.PERCENTAGE);
		titleLayout.addComponent(groupLabel);
		titleLayout.addComponent(refreshButton);
		titleLayout.setExpandRatio(groupLabel, 1);
		titleLayout.setExpandRatio(refreshButton, 1);
		titleLayout.setComponentAlignment(refreshButton, Alignment.MIDDLE_RIGHT);

		addComponent(titleLayout);
		addComponent(gridWrapper);
	}

}

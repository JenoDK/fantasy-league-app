package com.jeno.fantasyleague.ui.main.views.league.singleleague;

import java.util.Optional;

import com.jeno.fantasyleague.backend.model.League;
import com.jeno.fantasyleague.ui.common.tabsheet.CustomMenuBar;
import com.jeno.fantasyleague.ui.main.views.league.SingleLeagueServiceProvider;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.faq.FaqTab;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.leaguesettings.LeagueSettingsTab;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.matches.MatchTab;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.overview.OverviewTab;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.stocks.StocksTab;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.users.UsersTab;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class LeagueMenuBar extends CustomMenuBar {

	private final League league;
	private final SingleLeagueServiceProvider singleLeagueServiceprovider;

	public LeagueMenuBar(League league, SingleLeagueServiceProvider singleLeagueServiceprovider, VerticalLayout tabLayout) {
		super(tabLayout);
		this.league = league;
		this.singleLeagueServiceprovider = singleLeagueServiceprovider;

		initLayout();
	}

	private void initLayout() {
		HorizontalLayout mainMenuItemLayout = new HorizontalLayout();
		Label mainMenuItemLabel = new Label();
		mainMenuItemLabel.getStyle().set("cursor", "pointer");
		mainMenuItemLayout.add(VaadinIcon.MENU.create(), mainMenuItemLabel);
		MenuItem mainMenu = addItem(mainMenuItemLayout);

		CustomMenuItem overviewItem = addLazyItemForMenu(mainMenu, mainMenuItemLabel, "overview", "Overview", () -> new OverviewTab(league, singleLeagueServiceprovider, this));
		CustomMenuItem matchesItem = addLazyItemForMenu(mainMenu, mainMenuItemLabel, "matches", "Matches", () -> new MatchTab(league, singleLeagueServiceprovider, this));
		addLazyItemForMenu(mainMenu, mainMenuItemLabel, "teamWeightsTab", "Purchase stocks", () -> new StocksTab(league, singleLeagueServiceprovider));
//		addLazyItemForMenu(mainMenu, mainMenuItemLabel, "groupStageTab", "Group Stage", () -> new GroupStageTab(league, singleLeagueServiceprovider));
//		addLazyItemForMenu(mainMenu, mainMenuItemLabel, "knockoutStageTab", "Knockout Stage", () -> new KnockoutStageTab(league, singleLeagueServiceprovider));
		addLazyItemForMenu(mainMenu, mainMenuItemLabel, "faq", "FAQ", () -> new FaqTab(league, singleLeagueServiceprovider));
		if (singleLeagueServiceprovider.loggedInUserIsLeagueAdmin(league)) {
			addLazyItemForMenu(mainMenu, mainMenuItemLabel, "usersTab", "Users", () -> new UsersTab(league, singleLeagueServiceprovider));
			addLazyItemForMenu(mainMenu, mainMenuItemLabel, "leagueSettingsTab", "League Settings", () -> new LeagueSettingsTab(league, singleLeagueServiceprovider));
		}
		// Activate this by default
		overviewItem.getListener().onComponentEvent(null);
	}

	private CustomMenuItem addLazyItemForMenu(MenuItem menuItem, Label label, String id, String caption, ComponentCreationFunction function) {
		return addLazyItem(Optional.of(menuItem.getSubMenu()), id, caption, function, event -> label.setText(caption));
	}

}

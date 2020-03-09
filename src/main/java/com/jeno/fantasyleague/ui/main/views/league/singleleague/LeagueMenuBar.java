package com.jeno.fantasyleague.ui.main.views.league.singleleague;

import java.util.Optional;

import com.jeno.fantasyleague.backend.model.League;
import com.jeno.fantasyleague.ui.common.tabsheet.CustomMenuBar;
import com.jeno.fantasyleague.ui.main.views.league.SingleLeagueServiceProvider;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.faq.FaqTab;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.matches.MatchTab;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.users.UsersTab;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;

public class LeagueMenuBar extends CustomMenuBar {

	private final BehaviorSubject<ClickEvent<MenuItem>> backToLeagueGrid = BehaviorSubject.create();
	private final PublishSubject<ClickEvent<MenuItem>> back = PublishSubject.create();
	private final League league;
	private final SingleLeagueServiceProvider singleLeagueServiceprovider;

	public LeagueMenuBar(League league, SingleLeagueServiceProvider singleLeagueServiceprovider, VerticalLayout tabLayout) {
		super(tabLayout);
		this.league = league;
		this.singleLeagueServiceprovider = singleLeagueServiceprovider;

		initLayout();
	}

	private void initLayout() {
		MenuItem backItem = addItem(VaadinIcon.ARROW_LEFT.create());
		backItem.setVisible(false);
		backItem.addClickListener(back::onNext);
		HorizontalLayout mainMenuItemLayout = new HorizontalLayout();
		Label mainMenuItemLabel = new Label();
		mainMenuItemLabel.getStyle().set("cursor", "pointer");
		mainMenuItemLayout.add(VaadinIcon.MENU.create(), mainMenuItemLabel);
		MenuItem mainMenu = addItem(mainMenuItemLayout);
		CustomMenuItem matchesItem = addLazyItemForMenu(mainMenu, mainMenuItemLabel, "matches", "Matches", () -> new MatchTab(league, singleLeagueServiceprovider, back, backItem));
//		CustomMenuItem overviewItem = addLazyItemForMenu(mainMenu, mainMenuItemLabel, "overview", "Overview", () -> new OverviewTab(league, singleLeagueServiceprovider));
//		addLazyItemForMenu(mainMenu, mainMenuItemLabel, "teamWeightsTab", "Purchase stocks", () -> new TeamWeightsTab(league, singleLeagueServiceprovider));
//		addLazyItemForMenu(mainMenu, mainMenuItemLabel, "groupStageTab", "Group Stage", () -> new GroupStageTab(league, singleLeagueServiceprovider));
//		addLazyItemForMenu(mainMenu, mainMenuItemLabel, "knockoutStageTab", "Knockout Stage", () -> new KnockoutStageTab(league, singleLeagueServiceprovider));
		addLazyItemForMenu(mainMenu, mainMenuItemLabel, "faq", "FAQ", () -> new FaqTab(league, singleLeagueServiceprovider));
		if (singleLeagueServiceprovider.loggedInUserIsLeagueCreator(league)) {
			addLazyItemForMenu(mainMenu, mainMenuItemLabel, "usersTab", "Users", () -> new UsersTab(league, singleLeagueServiceprovider)).getListener().onComponentEvent(null);
//			addLazyItemForMenu(mainMenu, mainMenuItemLabel, "leagueSettingsTab", "League Settings", () -> new LeagueSettingsTab(league, singleLeagueServiceprovider));
		}
		mainMenu.getSubMenu().addItem("Back to your leagues", backToLeagueGrid::onNext);
		// Activate this by default
		matchesItem.getListener().onComponentEvent(null);
	}

	private CustomMenuItem addLazyItemForMenu(MenuItem menuItem, Label label, String id, String caption, ComponentCreationFunction function) {
		return addLazyItem(Optional.of(menuItem.getSubMenu()), id, caption, function, event -> label.setText(caption));
	}

	public Observable<ClickEvent<MenuItem>> backToLeaguesView() {
		return backToLeagueGrid;
	}

}

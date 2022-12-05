package com.jeno.fantasyleague.ui.main.views.league.singleleague;

import com.jeno.fantasyleague.ui.common.tabsheet.CustomTabs;
import com.jeno.fantasyleague.ui.common.tabsheet.LazyTab;
import com.jeno.fantasyleague.ui.common.tabsheet.LazyTabComponent;
import com.jeno.fantasyleague.ui.main.views.league.SingleLeagueServiceProvider;
import com.jeno.fantasyleague.ui.main.views.league.gridlayout.LeagueBean;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.faq.FaqTab;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.groups.GroupsTab;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.leaguesettings.LeagueSettingsTab;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.matches.MatchTab;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.overview.OverviewTab;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.stocks.StocksTab;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.users.UsersTab;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.TabsVariant;

public class LeagueTabs extends CustomTabs {

	private final LeagueBean leagueBean;
	private final SingleLeagueServiceProvider singleLeagueServiceprovider;
	private final VerticalLayout tabLayout;

	public LeagueTabs(LeagueBean leagueBean, SingleLeagueServiceProvider singleLeagueServiceprovider, VerticalLayout tabLayout) {
		super();
		this.leagueBean = leagueBean;
		this.singleLeagueServiceprovider = singleLeagueServiceprovider;
		this.tabLayout = tabLayout;

		initLayout();
	}

	private void initLayout() {
		setWidthFull();
		addThemeVariants(TabsVariant.LUMO_ICON_ON_TOP);

		LazyTab overview = createTab("Overview", () -> new OverviewTab(leagueBean.getLeague(), singleLeagueServiceprovider));
		LazyTab matches = createTab("Matches", () -> new MatchTab(leagueBean.getLeague(), singleLeagueServiceprovider, false, singleLeagueServiceprovider.getLoggedInUser()));
		LazyTab groups = createTab("Groups", () -> new GroupsTab(leagueBean.getLeague(), singleLeagueServiceprovider));
		LazyTab stocks = createTab("Stocks", () -> new StocksTab(leagueBean.getLeague(), singleLeagueServiceprovider));
		LazyTab faq = createTab("FAQ", () -> new FaqTab(leagueBean.getLeague(), singleLeagueServiceprovider));
		add(overview, matches, groups, stocks, faq);
		if (singleLeagueServiceprovider.loggedInUserIsLeagueAdmin(leagueBean.getLeague())) {
			LazyTab users = createTab("Users", () -> new UsersTab(leagueBean.getLeague(), singleLeagueServiceprovider));
			LazyTab settings = createTab("Settings", () -> new LeagueSettingsTab(leagueBean.getLeague(), singleLeagueServiceprovider));
			add(users, settings);
		}
	}

	private LazyTab createTab(String title, LazyTabComponent.ComponentCreationFunction componentCreationFunction) {
		LazyTab tab = new LazyTab(title, tabLayout) {
			@Override
			protected Component initLayout() {
				return componentCreationFunction.createComponent();
			}
		};
		return tab;
	}

}

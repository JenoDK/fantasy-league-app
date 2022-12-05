package com.jeno.fantasyleague.ui.main.views.admin;

import java.util.Optional;

import com.jeno.fantasyleague.backend.model.League;
import com.jeno.fantasyleague.ui.common.tabsheet.CustomTabs;
import com.jeno.fantasyleague.ui.common.tabsheet.LazyTab;
import com.jeno.fantasyleague.ui.common.tabsheet.LazyTabComponent;
import com.jeno.fantasyleague.ui.main.views.admin.matches.AdminMatchesTab;
import com.jeno.fantasyleague.ui.main.views.admin.stocks.AdminStocksTab;
import com.jeno.fantasyleague.ui.main.views.admin.usermatches.AdminUserMatchesTab;
import com.jeno.fantasyleague.ui.main.views.league.SingleLeagueServiceProvider;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.TabsVariant;

public class AdminTabs extends CustomTabs {

	private final SingleLeagueServiceProvider singleLeagueServiceprovider;
	private final VerticalLayout tabLayout;
	private final Optional<League> optionalLeague;

	public AdminTabs(Optional<League> optionalLeague, SingleLeagueServiceProvider singleLeagueServiceprovider, VerticalLayout tabLayout) {
		super();
		this.optionalLeague = optionalLeague;
		this.singleLeagueServiceprovider = singleLeagueServiceprovider;
		this.tabLayout = tabLayout;

		initLayout();
	}

	private void initLayout() {
		setWidthFull();
		addThemeVariants(TabsVariant.LUMO_ICON_ON_TOP);

		LazyTab matches = createTab("Matches", () -> new AdminMatchesTab(singleLeagueServiceprovider, optionalLeague));
		LazyTab stocks = createTab("Stocks", () -> new AdminStocksTab(singleLeagueServiceprovider, optionalLeague));
		LazyTab userMatches = createTab("User matches", () -> new AdminUserMatchesTab(singleLeagueServiceprovider));
		add(matches, stocks, userMatches);
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

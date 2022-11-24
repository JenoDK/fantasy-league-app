package com.jeno.fantasyleague.ui.main.views.admin.stocks;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.jeno.fantasyleague.backend.model.League;
import com.jeno.fantasyleague.backend.model.User;
import com.jeno.fantasyleague.backend.model.enums.Template;
import com.jeno.fantasyleague.ui.common.tabsheet.LazyTabComponent;
import com.jeno.fantasyleague.ui.main.views.league.SingleLeagueServiceProvider;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.stocks.StocksTab;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;

public class AdminStocksTab extends LazyTabComponent {

	private final SingleLeagueServiceProvider singleLeagueServiceProvider;
	private final Optional<League> optionalLeague;
	private User user;
	private League league;
	private VerticalLayout layout;

	public AdminStocksTab(SingleLeagueServiceProvider singleLeagueServiceProvider, Optional<League> optionalLeague) {
		this.singleLeagueServiceProvider = singleLeagueServiceProvider;
		this.optionalLeague = optionalLeague;

		initLayout();
	}

	private void initLayout() {
		layout = new VerticalLayout();
		layout.setMaxWidth("1200px");
		layout.setAlignItems(FlexComponent.Alignment.CENTER);
		layout.setMargin(false);
		layout.setPadding(false);

		ComboBox<User> userComboBox = new ComboBox<>("Select User");
		userComboBox.setWidthFull();
		userComboBox.setItemLabelGenerator(u -> "Username: " + u.getUsername() + " Name: " + u.getName() + " email: " + u.getEmail());
		userComboBox.setDataProvider(new ListDataProvider<>(singleLeagueServiceProvider.getUserRepository().findAll()));
		userComboBox.addValueChangeListener(event -> {
			setUser(event.getValue());
			drawStocksTab();
		});

		ComboBox<League> leagueCombobox = new ComboBox<>("Select League");
		leagueCombobox.setWidthFull();
		leagueCombobox.setItemLabelGenerator(League::getName);
		leagueCombobox.setDataProvider(new ListDataProvider<>(getLeagues()));
		leagueCombobox.addValueChangeListener(event -> {
			setLeague(event.getValue());
			drawStocksTab();
		});

		add(userComboBox, leagueCombobox, layout);
	}

	private void drawStocksTab() {
		layout.removeAll();
		if (user != null && league != null) {
			StocksTab stocksTab = new StocksTab(league, singleLeagueServiceProvider, true, user);
			layout.add(stocksTab);
		}
	}

	private void setUser(User user) {
		this.user = user;
	}

	private void setLeague(League league) {
		this.league = league;
	}

	private List<League> getLeagues() {
		return singleLeagueServiceProvider.getLeagueRepository().findAll().stream()
				.filter(leagueBean -> Template.FIFA_WORLD_CUP_2022.equals(leagueBean.getTemplate()))
				.collect(Collectors.toList());
	}
}

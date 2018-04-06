package com.jeno.fantasyleague.ui.main.views.league.singleleague.users;

import com.jeno.fantasyleague.model.League;
import com.jeno.fantasyleague.model.User;
import com.jeno.fantasyleague.ui.main.views.league.SingleLeagueServiceProvider;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import java.util.List;

public class UsersTab extends VerticalLayout {

	public UsersTab(League league, SingleLeagueServiceProvider singleLeagueServiceProvider) {
		super();
		setMargin(false);
		setSpacing(false);

		Label leagueUsersLabel = new Label("League Users", ContentMode.HTML);
		leagueUsersLabel.addStyleName(ValoTheme.LABEL_H3);
		addComponent(leagueUsersLabel);
		List<User> users = singleLeagueServiceProvider.getLeagueRepository().fetchLeagueUsers(league.getId());
		addComponent(new UserGrid(DataProvider.fromStream(users.stream())));
		addComponent(new InviteUserLayout(league, users, singleLeagueServiceProvider));
	}

}

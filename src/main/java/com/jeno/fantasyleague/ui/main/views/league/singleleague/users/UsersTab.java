package com.jeno.fantasyleague.ui.main.views.league.singleleague.users;

import com.google.common.collect.Lists;
import com.jeno.fantasyleague.model.League;
import com.jeno.fantasyleague.model.User;
import com.jeno.fantasyleague.ui.main.views.league.SingleLeagueServiceProvider;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import java.util.List;

public class UsersTab extends HorizontalLayout {

	public UsersTab(League league, SingleLeagueServiceProvider singleLeagueServiceProvider) {
		super();
		setMargin(false);
		setSpacing(true);

		VerticalLayout leftSide = new VerticalLayout();
		leftSide.setMargin(false);
		leftSide.setSpacing(false);

		Label leagueUsersLabel = new Label("League Users", ContentMode.HTML);
		leagueUsersLabel.addStyleName(ValoTheme.LABEL_H3);
		leftSide.addComponent(leagueUsersLabel);
		List<User> users = singleLeagueServiceProvider.getLeagueRepository().fetchLeagueUsers(league.getId());

		leftSide.addComponent(new UserGrid(DataProvider.fromStream(users.stream())));

		addComponent(leftSide);

		if (singleLeagueServiceProvider.userIsLeagueAdmin(league)) {
			List<User> usersWithPendingInvites = singleLeagueServiceProvider.getUsersWithPendingInvite(league);
			List<User> usersToExcludeFromInviteChoices = Lists.newArrayList(users);
			usersToExcludeFromInviteChoices.addAll(usersWithPendingInvites);
			addComponent(new InviteUserLayout(league, usersToExcludeFromInviteChoices, singleLeagueServiceProvider));
		}
	}

}

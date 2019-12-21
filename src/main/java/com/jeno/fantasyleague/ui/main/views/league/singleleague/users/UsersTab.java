package com.jeno.fantasyleague.ui.main.views.league.singleleague.users;

import java.util.List;

import com.google.common.collect.Lists;
import com.jeno.fantasyleague.backend.model.League;
import com.jeno.fantasyleague.backend.model.User;
import com.jeno.fantasyleague.ui.main.views.league.SingleLeagueServiceProvider;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.DataProvider;

public class UsersTab extends HorizontalLayout {

	public UsersTab(League league, SingleLeagueServiceProvider singleLeagueServiceProvider) {
		super();
		setMargin(false);
		setSpacing(true);

		VerticalLayout leftSide = new VerticalLayout();
		leftSide.setMargin(false);
		leftSide.setSpacing(false);

		Label leagueUsersLabel = new Label("League Users");
//		leagueUsersLabel.addClassName(ValoTheme.LABEL_H3);
		leftSide.add(leagueUsersLabel);
		List<User> users = singleLeagueServiceProvider.getLeagueRepository().fetchLeagueUsers(league.getId());

		leftSide.add(new UserGrid(DataProvider.fromStream(users.stream()), singleLeagueServiceProvider, league));

		add(leftSide);

		if (singleLeagueServiceProvider.loggedInUserIsLeagueAdmin(league)) {
			List<User> usersWithPendingInvites = singleLeagueServiceProvider.getUsersWithPendingInvite(league);
			List<User> usersToExcludeFromInviteChoices = Lists.newArrayList(users);
			usersToExcludeFromInviteChoices.addAll(usersWithPendingInvites);
			add(new InviteUserLayout(league, usersToExcludeFromInviteChoices, singleLeagueServiceProvider));
		}
	}

}

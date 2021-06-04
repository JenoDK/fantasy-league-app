package com.jeno.fantasyleague.ui.main.views.league.singleleague.users;

import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import com.jeno.fantasyleague.backend.model.League;
import com.jeno.fantasyleague.backend.model.LeagueUser;
import com.jeno.fantasyleague.backend.model.User;
import com.jeno.fantasyleague.ui.common.tabsheet.LazyTabComponent;
import com.jeno.fantasyleague.ui.main.views.league.SingleLeagueServiceProvider;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;

public class UsersTab extends LazyTabComponent {

	public UsersTab(League league, SingleLeagueServiceProvider singleLeagueServiceProvider) {
		super();
		setMargin(false);
		setPadding(false);
		setSizeFull();

		VerticalLayout leftSide = new VerticalLayout();
		leftSide.setMargin(false);
		leftSide.setPadding(false);
		leftSide.setSpacing(false);
		leftSide.setSizeFull();

		leftSide.add(new H3("League Users"));
		List<User> users = singleLeagueServiceProvider.getLeagueUserRepository().findByLeague(league.getId()).stream().map(LeagueUser::getUser).collect(Collectors.toList());
		UserGrid usersGrid = new UserGrid(DataProvider.fromStream(users.stream()), singleLeagueServiceProvider, league);
		if (singleLeagueServiceProvider.loggedInUserIsLeagueCreator(league)) {
			usersGrid.addColumn(new ComponentRenderer<>(user -> UserGrid.promoteButton(user, singleLeagueServiceProvider, league)))
					.setWidth("130px");
		}
		leftSide.add(usersGrid);

		List<User> pendingInvites = singleLeagueServiceProvider.getUsersWithPendingInvite(league, users);
		UserGrid pendingUserInvitesGrid = new UserGrid(DataProvider.fromStream(pendingInvites.stream()), singleLeagueServiceProvider, league);
		leftSide.add(new H3("Pending invites"));
		leftSide.add(pendingUserInvitesGrid);

		add(leftSide);

		if (singleLeagueServiceProvider.loggedInUserIsLeagueAdmin(league)) {
			List<User> usersToExcludeFromInviteChoices = Lists.newArrayList(users);
			usersToExcludeFromInviteChoices.addAll(pendingInvites);
			InviteUserLayout inviteUserLayout = new InviteUserLayout(league, usersToExcludeFromInviteChoices, singleLeagueServiceProvider);
			inviteUserLayout.userInvited().subscribe(ignored -> pendingUserInvitesGrid.setItems(singleLeagueServiceProvider.getUsersWithPendingInvite(league, users)));
			add(inviteUserLayout);
		}
	}

}

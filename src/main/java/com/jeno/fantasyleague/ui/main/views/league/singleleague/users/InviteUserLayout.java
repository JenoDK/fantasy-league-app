package com.jeno.fantasyleague.ui.main.views.league.singleleague.users;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.Lists;
import com.jeno.fantasyleague.backend.data.service.email.ApplicationEmailService;
import com.jeno.fantasyleague.backend.model.League;
import com.jeno.fantasyleague.backend.model.User;
import com.jeno.fantasyleague.resources.Resources;
import com.jeno.fantasyleague.ui.common.field.CustomButton;
import com.jeno.fantasyleague.ui.common.grid.CustomGrid;
import com.jeno.fantasyleague.ui.common.grid.CustomGridBuilder;
import com.jeno.fantasyleague.ui.main.broadcast.Broadcaster;
import com.jeno.fantasyleague.ui.main.views.league.SingleLeagueServiceProvider;
import com.jeno.fantasyleague.util.Images;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

public class InviteUserLayout extends VerticalLayout {

	private final BehaviorSubject<ClickEvent<Button>> userInvited = BehaviorSubject.create();

	public InviteUserLayout(League league, List<User> usersToExclude, SingleLeagueServiceProvider singleLeagueServiceProvider) {
		super();
		setMargin(false);
		setPadding(false);

		H3 inviteUsers = new H3("Invite Users");
		add(inviteUsers);

		Set<Long> usersToExcludeIds =  usersToExclude.stream()
				.map(User::getId)
				.collect(Collectors.toSet());

		DataProvider<User, String> dataProvider = getAddUserComboboxDataProvider(singleLeagueServiceProvider, usersToExcludeIds);

		ListDataProvider<User> usersToInviteDataProvider = DataProvider.fromStream(Stream.empty());
		CustomGridBuilder<User> usersToInviteGridBuilder = UserGrid.getDefaultUserGridBuilder(usersToInviteDataProvider);
		CustomGrid<User> usersToInviteGrid = usersToInviteGridBuilder
			.withIconColumn(
					new CustomGridBuilder.ColumnProvider<>(
							"removeColumn",
							user -> new CustomGridBuilder.IconColumnValue(Images.Icons.REMOVE, grid -> {
								grid.removeItem(user);
								usersToExcludeIds.remove(user.getId());
								dataProvider.refreshAll();
							}),
							""))
			.build();
		usersToInviteGrid.setAdjustHeightDynamically(false);

		ComboBox<User> userComboBox = new ComboBox<>("Select user");
//		userComboBox.addClassName(ValoTheme.COMBOBOX_SMALL);
		userComboBox.setWidthFull();
		userComboBox.setItemLabelGenerator(u -> "Username: " + u.getUsername() + " Name: " + u.getName() + " email: " + u.getEmail());
		userComboBox.setDataProvider(dataProvider);
		userComboBox.addFocusListener(ignored -> dataProvider.refreshAll());
		userComboBox.addValueChangeListener(event -> {
			if (event.getValue() != null) {
				usersToInviteGrid.addItem(event.getValue());
				userComboBox.setValue(userComboBox.getEmptyValue());
				usersToExcludeIds.add(event.getValue().getId());
				dataProvider.refreshAll();
			}
		});

		add(userComboBox);
		add(usersToInviteGrid);

		Button inviteButton = new CustomButton("Invite Users");
		inviteButton.addClickListener(ignored -> {
			if (singleLeagueServiceProvider.loggedInUserIsLeagueAdmin(league)) {
				usersToInviteGrid.getItems().forEach(
						user -> {
							Broadcaster.broadcast(
									user.getId(),
									singleLeagueServiceProvider.createLeagueInviteUserNotification(user, league));
							try {
								ApplicationEmailService emailService = singleLeagueServiceProvider.getEmailService();
								emailService.sendEmail(
										"euro2020-manager league invite",
										"You got invited to participate in the league " + league.getName() + ". Log in to https://euro2020-manager.com and accept the invite.",
										user);
							} catch (Exception e) {
								e.printStackTrace();
							}
						});
				usersToInviteGrid.setItems(Lists.newArrayList());
				dataProvider.refreshAll();
				userInvited.onNext(ignored);
			} else {
				Notification.show(Resources.getMessage("adminRightsRevoked"));
			}
		});
		add(inviteButton);
	}

	public Observable<ClickEvent<Button>> userInvited() {
		return userInvited;
	}

	public CallbackDataProvider<User, String> getAddUserComboboxDataProvider(SingleLeagueServiceProvider singleLeagueServiceProvider, Set<Long> existingUserIds) {
		return DataProvider.fromFilteringCallbacks(
				query -> query.getFilter()
							.map(filter -> singleLeagueServiceProvider.getUserService()
									.fetchUsersByNameAndExclude(filter, query.getOffset(), query.getLimit(), existingUserIds).stream())
							.orElse(singleLeagueServiceProvider.getUserService()
									.fetchUsersAndExclude(query.getOffset(), query.getLimit(), existingUserIds).stream()),
				query -> query.getFilter()
							.map(filter -> singleLeagueServiceProvider.getUserService()
									.getUsersCountByNameAndExclude(filter, existingUserIds))
							.orElse(singleLeagueServiceProvider.getUserService()
									.getUserCountAndExclude(existingUserIds)));
	}
}

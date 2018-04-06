package com.jeno.fantasyleague.ui.main.views.league.singleleague.users;

import com.jeno.fantasyleague.model.League;
import com.jeno.fantasyleague.model.User;
import com.jeno.fantasyleague.ui.common.grid.CustomGrid;
import com.jeno.fantasyleague.ui.common.grid.CustomGridBuilder;
import com.jeno.fantasyleague.ui.main.broadcast.Broadcaster;
import com.jeno.fantasyleague.ui.main.broadcast.Notification;
import com.jeno.fantasyleague.ui.main.views.league.SingleLeagueServiceProvider;
import com.jeno.fantasyleague.util.Images;
import com.vaadin.data.provider.CallbackDataProvider;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InviteUserLayout extends VerticalLayout {

	public InviteUserLayout(League league, List<User> existingUsers, SingleLeagueServiceProvider singleLeagueServiceProvider) {
		super();
		setMargin(false);

		Label inviteUsers = new Label("Invite Users", ContentMode.HTML);
		inviteUsers.addStyleName(ValoTheme.LABEL_H3);
		addComponent(inviteUsers);

		Set<Long> existingUserIds =  existingUsers.stream()
				.map(User::getId)
				.collect(Collectors.toSet());

		DataProvider<User, String> dataProvider = getAddUserComboboxDataProvider(singleLeagueServiceProvider, existingUserIds);

		ListDataProvider<User> usersToInviteDataProvider = DataProvider.fromStream(Stream.empty());
		CustomGridBuilder usersToInviteGridBuilder = UserGrid.getDefaultUserGridBuilder(usersToInviteDataProvider);
		CustomGrid<User> usersToInviteGrid = usersToInviteGridBuilder
			.withIconColumn(
				new CustomGridBuilder.ColumnProvider<User, CustomGridBuilder.IconColumnValue>(
						"removeColumn",
						user -> new CustomGridBuilder.IconColumnValue(new ThemeResource(Images.Icons.REMOVE), grid -> {
							grid.removeItem(user);
							existingUserIds.remove(user.getId());
							dataProvider.refreshAll();
						}),
						""))
			.build();

		ComboBox<User> userComboBox = new ComboBox<>("Select user");
		userComboBox.setItemCaptionGenerator(user -> user.getUsername());
		userComboBox.setDataProvider(dataProvider);
		userComboBox.addValueChangeListener(event -> {
			if (event.getValue() != null) {
				usersToInviteGrid.addItem(event.getValue());
				userComboBox.setValue(userComboBox.getEmptyValue());
				existingUserIds.add(event.getValue().getId());
				dataProvider.refreshAll();
			}
		});

		addComponent(userComboBox);
		addComponent(usersToInviteGrid);

		Button inviteButton = new Button("Invite Users");
		inviteButton.addClickListener(ignored -> {
			usersToInviteDataProvider.getItems().forEach(
					user -> Broadcaster.broadcast(user.getId(), new Notification("TEST", "MSG")));
		});
		addComponent(inviteButton);
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

package com.jeno.fantasyleague.ui.main.navigation;

import java.util.List;

import com.jeno.fantasyleague.backend.model.User;
import com.jeno.fantasyleague.backend.model.UserNotification;
import com.jeno.fantasyleague.ui.main.NotificationModel;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class TopBar extends HorizontalLayout {

	private final User user;
	private NavigationBar navigationBar;
	private ProfileMenuBar profileBar;

	public TopBar(User user, List<UserNotification> userNotifications, NotificationModel notificationModel) {
		super();
		this.user = user;

		initLayout(userNotifications, notificationModel);
	}

	private void initLayout(List<UserNotification> userNotifications, NotificationModel notificationModel) {
		addClassName("topbar");
		setWidth("100%");

		navigationBar = new NavigationBar();
		profileBar = new ProfileMenuBar(user, userNotifications, notificationModel);

		add(navigationBar);
		add(profileBar);
	}

	public void updateNotifications(List<UserNotification> userNotifications) {
		profileBar.updateNotifications(userNotifications);
	}
}

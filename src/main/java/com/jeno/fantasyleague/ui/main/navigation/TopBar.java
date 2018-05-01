package com.jeno.fantasyleague.ui.main.navigation;

import com.jeno.fantasyleague.model.User;
import com.jeno.fantasyleague.model.UserNotification;
import com.jeno.fantasyleague.ui.main.NotificationModel;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;

import java.util.List;

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
		addStyleName("topbar");
		setWidth("100%");
		setHeightUndefined();

		navigationBar = new NavigationBar();
		profileBar = new ProfileMenuBar(user, userNotifications, notificationModel);

		addComponent(navigationBar);
		addComponent(profileBar);
		setComponentAlignment(navigationBar, Alignment.MIDDLE_LEFT);
		setComponentAlignment(profileBar, Alignment.MIDDLE_RIGHT);
	}

	public void updateNotifications(List<UserNotification> userNotifications) {
		profileBar.updateNotifications(userNotifications);
	}
}

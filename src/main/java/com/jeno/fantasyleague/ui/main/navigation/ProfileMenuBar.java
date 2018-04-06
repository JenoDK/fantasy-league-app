package com.jeno.fantasyleague.ui.main.navigation;

import com.jeno.fantasyleague.model.User;
import com.jeno.fantasyleague.ui.main.broadcast.Broadcaster;
import com.jeno.fantasyleague.ui.main.broadcast.Notification;
import com.jeno.fantasyleague.ui.main.views.state.State;
import com.jeno.fantasyleague.util.ImageUtil;
import com.jeno.fantasyleague.util.Images;
import com.jeno.fantasyleague.util.VaadinUtil;
import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.MenuBar;

import java.io.ByteArrayInputStream;

public class ProfileMenuBar extends MenuBar {

	private final User user;

	private MenuItem notificationItem;

	public ProfileMenuBar(User user) {
		super();
		this.user = user;

		initLayout();
	}

	private void initLayout() {
		setWidthUndefined();
		setHeight(60f, Unit.PIXELS);
		addStyleName("fantasy-league-menubar");

		createNotificationMenuItem();
		createProfileMenuItem();
	}

	private void createProfileMenuItem() {
		MenuItem profileItem = addItem("profile", ImageUtil.getUserProfilePictureResource(user), null);
		profileItem.setStyleName("menu-state-item");
		profileItem.setText("");
		profileItem.addItem("Profile", item -> getUI().getNavigator().navigateTo(State.PROFILE.getIdentifier()));
		profileItem.addItem("Logout", item -> VaadinUtil.logout());
	}

	private void createNotificationMenuItem() {
		notificationItem = addItem("notification", new ThemeResource(Images.NOTIFICATION), null);
		notificationItem.setStyleName("menu-state-item");
		notificationItem.setText("");
	}

	public void addClientNotification(Notification message) {
		notificationItem.setIcon(new ThemeResource(Images.NOTIFICATION_ACTIVE));
	}
}

package com.jeno.fantasyleague.ui.main.navigation;

import com.google.common.collect.Lists;
import com.jeno.fantasyleague.model.User;
import com.jeno.fantasyleague.model.UserNotification;
import com.jeno.fantasyleague.ui.main.NotificationModel;
import com.jeno.fantasyleague.ui.main.views.state.State;
import com.jeno.fantasyleague.util.ImageUtil;
import com.jeno.fantasyleague.util.Images;
import com.jeno.fantasyleague.util.VaadinUtil;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.MenuBar;

import java.util.Comparator;
import java.util.List;

public class ProfileMenuBar extends MenuBar {

	private final User user;
	private final NotificationModel notificationModel;

	private MenuItem notificationItem;
	private List<UserNotification> userNotifications = Lists.newArrayList();

	public ProfileMenuBar(User user, List<UserNotification> userNotifications, NotificationModel notificationModel) {
		super();
		this.user = user;
		this.notificationModel = notificationModel;

		initLayout(userNotifications);
	}

	private void initLayout(List<UserNotification> userNotifications) {
		setWidthUndefined();
		setHeight(60f, Unit.PIXELS);
		addStyleName("fantasy-league-menubar");

		createNotificationMenuItem();
		createProfileMenuItem();
		updateNotifications(userNotifications);
	}

	private void createProfileMenuItem() {
		MenuItem profileItem = addItem("profile", ImageUtil.getUserProfilePictureResource(user), null);
		profileItem.setStyleName("menu-state-item profile-item");
		profileItem.setText(user.getUsername());
		profileItem.addItem("Profile", item -> getUI().getNavigator().navigateTo(State.PROFILE.getIdentifier()));
		profileItem.addItem("Logout", item -> VaadinUtil.logout());
	}

	private void createNotificationMenuItem() {
		notificationItem = addItem("notification", new ThemeResource(Images.NOTIFICATION), null);
		notificationItem.setStyleName("menu-state-item");
		notificationItem.setText("");
	}

	public void updateNotifications(List<UserNotification> userNotifications) {
		this.userNotifications = userNotifications;
		notificationItem.removeChildren();
		userNotifications.stream()
				.sorted(Comparator.comparing(UserNotification::getCreatedAt))
				.forEach(this::addNotificationItem);
		if (!userNotifications.isEmpty()) {
			notificationItem.setIcon(new ThemeResource(Images.NOTIFICATION_ACTIVE));
		} else {
			notificationItem.setIcon(new ThemeResource(Images.NOTIFICATION));
		}
	}

	private void addNotificationItem(UserNotification notification) {
		MenuBar.MenuItem menuItem = notificationItem.addItem(notification.getMessage(), ImageUtil.getUserProfilePictureResource(notification.getCreatedBy()), item -> {});
		menuItem.setStyleName("menu-notification-item");
		if (notification.getNotification_type().isNeedsAccepting()) {
			MenuBar.MenuItem acceptItem = menuItem.addItem(
					"Accept",
					new ThemeResource(Images.Icons.CHECK),
					ignored -> {
						notificationModel.notificationAccepted(notification).ifPresent(error -> {
							// TODO find a way to inform user
						});
						notificationItem.removeChild(menuItem);
						// TODO Reload notifications/remove this one ?
					});
			MenuBar.MenuItem declineItem = menuItem.addItem(
					"Decline (not implemented yet)",
					new ThemeResource(Images.Icons.REMOVE),
					ignored -> {
						notificationModel.notificationDeclined(notification).ifPresent(error -> {
							// TODO find a way to inform user
						});
						notificationItem.removeChild(menuItem);
						// TODO Reload notifications/remove this one ?
					});
			acceptItem.setStyleName("accept-decline-item");
			declineItem.setStyleName("accept-decline-item");
		}
	}
}

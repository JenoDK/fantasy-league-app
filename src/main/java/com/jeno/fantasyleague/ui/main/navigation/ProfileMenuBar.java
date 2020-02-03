package com.jeno.fantasyleague.ui.main.navigation;

import java.util.Comparator;
import java.util.List;

import com.google.common.collect.Lists;
import com.jeno.fantasyleague.backend.model.User;
import com.jeno.fantasyleague.backend.model.UserNotification;
import com.jeno.fantasyleague.ui.main.NotificationModel;
import com.jeno.fantasyleague.ui.main.views.state.State;
import com.jeno.fantasyleague.util.ImageUtil;
import com.jeno.fantasyleague.util.Images;
import com.jeno.fantasyleague.util.VaadinUtil;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.menubar.MenuBar;

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
		setHeight("60px");
		addClassName("fantasy-league-menubar");

		createNotificationMenuItem();
		createProfileMenuItem();
		updateNotifications(userNotifications);
	}

	private void createProfileMenuItem() {
		MenuItem profileItem = addItem("profile", null);
		// TODO
//		profileItem.addAsFirst(ImageUtil.getUserProfilePictureImage(user));
		profileItem.getElement().getClassList().add("menu-state-item");
		profileItem.getElement().getClassList().add("profile-item");
		profileItem.setText(user.getUsername());
		profileItem.getSubMenu().addItem("Profile", item -> getUI().ifPresent(ui -> ui.navigate(State.PROFILE.getIdentifier())));
		profileItem.getSubMenu().addItem("Logout", item -> VaadinUtil.logout());
	}

	private void createNotificationMenuItem() {
		notificationItem = addItem("notification", null);
		// TODO
//		notificationItem.addAsFirst(new ThemeResource(Images.NOTIFICATION));
		notificationItem.getElement().getClassList().add("menu-state-item");
		notificationItem.setText("");
	}

	public void updateNotifications(List<UserNotification> userNotifications) {
		this.userNotifications = userNotifications;
		notificationItem.removeAll();
		userNotifications.stream()
				.sorted(Comparator.comparing(UserNotification::getCreatedAt))
				.forEach(this::addNotificationItem);
		if (!userNotifications.isEmpty()) {
			// TODO
//			notificationItem.addAsFirst(new ThemeResource(Images.NOTIFICATION_ACTIVE));
		} else {
//			notificationItem.addAsFirst(new ThemeResource(Images.NOTIFICATION));
		}
	}

	private void addNotificationItem(UserNotification notification) {
		MenuItem menuItem = notificationItem.getSubMenu().addItem(notification.getMessage(), item -> {});
		menuItem.addComponentAsFirst(ImageUtil.getUserProfilePictureImage(notification.getCreatedBy()));
		menuItem.getElement().getClassList().add("menu-notification-item");
		if (notification.getNotification_type().isNeedsAccepting()) {
			MenuItem acceptItem = menuItem.getSubMenu().addItem(
					"Accept",
					ignored -> {
						notificationModel.notificationAccepted(notification).ifPresent(error -> {
							// TODO find a way to inform user
						});
						notificationItem.remove(menuItem);
						// TODO Reload notifications/remove this one ?
					});
			// TODO
			acceptItem.addComponentAsFirst(new Image(Images.Icons.CHECK, "check"));
			MenuItem declineItem = menuItem.getSubMenu().addItem(
					"Decline (not implemented yet)",
					ignored -> {
						notificationModel.notificationDeclined(notification).ifPresent(error -> {
							// TODO find a way to inform user
						});
						notificationItem.remove(menuItem);
						// TODO Reload notifications/remove this one ?
					});
			acceptItem.getElement().getClassList().add("accept-decline-item");
			declineItem.getElement().getClassList().add("accept-decline-item");
		}
	}
}

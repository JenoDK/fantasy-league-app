package com.jeno.fantasyleague.ui.main.views.notifications;

import com.jeno.fantasyleague.backend.model.UserNotification;

public class NotificationBean {

	private UserNotification notification;

	public NotificationBean(UserNotification notification) {
		this.notification = notification;
	}

	public UserNotification getNotification() {
		return notification;
	}
}

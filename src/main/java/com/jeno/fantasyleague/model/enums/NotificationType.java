package com.jeno.fantasyleague.model.enums;

import com.jeno.fantasyleague.data.service.notificationtypes.NotificationTypeConfig;

public enum NotificationType {

	LEAGUE_INVITE(true, NotificationTypeConfig.LEAGUE_INVITE);

	private final boolean needsAccepting;
	private final String notificationTypeBeanName;

	NotificationType(boolean needsAccepting, String notificationTypeBeanName) {
		this.needsAccepting = needsAccepting;
		this.notificationTypeBeanName = notificationTypeBeanName;
	}

	public boolean isNeedsAccepting() {
		return needsAccepting;
	}

	public String getNotificationTypeBeanName() {
		return notificationTypeBeanName;
	}

}

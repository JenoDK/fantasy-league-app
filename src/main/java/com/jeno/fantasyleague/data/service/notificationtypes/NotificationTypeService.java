package com.jeno.fantasyleague.data.service.notificationtypes;

import com.jeno.fantasyleague.model.UserNotification;

public interface NotificationTypeService {

	void accepted(UserNotification notification) throws NotificationException;

	void declined(UserNotification notification) throws NotificationException;

}

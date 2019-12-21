package com.jeno.fantasyleague.backend.data.service.notificationtypes;

import com.jeno.fantasyleague.backend.model.UserNotification;

public interface NotificationTypeService {

	void accepted(UserNotification notification) throws NotificationException;

	void declined(UserNotification notification) throws NotificationException;

}

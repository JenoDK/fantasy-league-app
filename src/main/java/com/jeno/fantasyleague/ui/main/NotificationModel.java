package com.jeno.fantasyleague.ui.main;

import java.util.Optional;

import com.jeno.fantasyleague.backend.data.service.notificationtypes.NotificationException;
import com.jeno.fantasyleague.backend.data.service.notificationtypes.NotificationTypeService;
import com.jeno.fantasyleague.backend.model.UserNotification;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
public class NotificationModel {

	@Autowired
	private BeanFactory beanFactory;

	public Optional<String> notificationAccepted(UserNotification notification) {
		try {
			beanFactory.getBean(notification.getNotification_type().getNotificationTypeBeanName(), NotificationTypeService.class)
					.accepted(notification);
			return Optional.empty();
		} catch (NotificationException e) {
			return Optional.of(e.getMessage());
		}
	}

	public Optional<String> notificationDeclined(UserNotification notification) {
		try {
			beanFactory.getBean(notification.getNotification_type().getNotificationTypeBeanName(), NotificationTypeService.class)
					.declined(notification);
			return Optional.empty();
		} catch (NotificationException e) {
			return Optional.of(e.getMessage());
		}
	}

}

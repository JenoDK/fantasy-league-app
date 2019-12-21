package com.jeno.fantasyleague.security;

import java.util.List;

import com.jeno.fantasyleague.backend.data.repository.UserNotificationRepository;
import com.jeno.fantasyleague.backend.model.User;
import com.jeno.fantasyleague.backend.model.UserNotification;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
public class SecurityHolder {

	private final UserNotificationRepository userNotificationRepository;
	private final CurrentUser currentUser;

	@Autowired
	public SecurityHolder(UserNotificationRepository userNotificationRepository, CurrentUser currentUser) {
		this.userNotificationRepository = userNotificationRepository;
		this.currentUser = currentUser;
	}

	public User getUser() {
		return currentUser.getUser();
	}

	public List<UserNotification> getUserNotifications() {
		return userNotificationRepository.findByUserAndViewed(getUser(), false);
	}
}

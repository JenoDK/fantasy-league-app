package com.jeno.fantasyleague.data.security;

import com.jeno.fantasyleague.data.repository.UserNotificationRepository;
import com.jeno.fantasyleague.data.service.repo.user.UserService;
import com.jeno.fantasyleague.model.User;
import com.jeno.fantasyleague.model.UserNotification;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@SpringComponent
public class SecurityHolder {

	@Autowired
	private UserNotificationRepository userNotificationRepository;
	@Autowired
	private UserService userService;

	@Transactional
	public void loadUser(VaadinRequest vaadinRequest) {
		Principal principal = vaadinRequest.getUserPrincipal();
		if (principal == null || StringUtils.isEmpty(principal.getName())){
			return;
		}
		Optional<User> user = userService.findByUsernameAndJoinRoles(principal.getName());
		if (!user.isPresent()) {
			return;
		}
		VaadinSession.getCurrent().setAttribute(User.class, user.get());
	}
	
	public User getUser() {
		return VaadinSession.getCurrent().getAttribute(User.class);
	}

	public List<UserNotification> getUserNotifications() {
		return userNotificationRepository.findByUserAndViewed(getUser(), false);
	}
}

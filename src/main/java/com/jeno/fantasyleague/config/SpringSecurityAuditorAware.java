package com.jeno.fantasyleague.config;

import com.jeno.fantasyleague.model.User;
import com.vaadin.server.VaadinSession;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

public class SpringSecurityAuditorAware implements AuditorAware<User> {

	public Optional<User> getCurrentAuditor() {
		return Optional.ofNullable(VaadinSession.getCurrent().getAttribute(User.class));
	}
}

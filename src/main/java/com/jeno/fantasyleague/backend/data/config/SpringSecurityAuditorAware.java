package com.jeno.fantasyleague.backend.data.config;

import java.util.Optional;

import com.jeno.fantasyleague.backend.model.User;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.data.domain.AuditorAware;

public class SpringSecurityAuditorAware implements AuditorAware<User> {

	@Override
	public Optional<User> getCurrentAuditor() {
		return Optional.ofNullable(VaadinSession.getCurrent().getAttribute(User.class));
	}
}

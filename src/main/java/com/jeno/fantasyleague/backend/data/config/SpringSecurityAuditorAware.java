package com.jeno.fantasyleague.backend.data.config;

import com.jeno.fantasyleague.backend.model.User;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

public class SpringSecurityAuditorAware implements AuditorAware<User> {

	@Override
	public Optional<User> getCurrentAuditor() {
		return Optional.ofNullable(VaadinSession.getCurrent())
				.map(session -> session.getAttribute(User.class));
	}
}

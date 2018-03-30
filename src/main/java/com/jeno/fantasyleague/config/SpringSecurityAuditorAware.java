package com.jeno.fantasyleague.config;

import com.jeno.fantasyleague.data.security.CustomUserDetails;
import com.jeno.fantasyleague.model.User;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class SpringSecurityAuditorAware implements AuditorAware<User> {

	public Optional<User> getCurrentAuditor() {
		return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
				.filter(Authentication::isAuthenticated)
				.filter(auth -> auth.getPrincipal() instanceof CustomUserDetails)
				.map(auth -> ((CustomUserDetails) auth.getPrincipal()).getUser());
	}
}

package com.jeno.fantasyleague.backend.data.config;

import com.jeno.fantasyleague.backend.model.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class AuditingConfig {

	@Bean
	public AuditorAware<User> auditorProvider() {
		return new SpringSecurityAuditorAware();
	}

}

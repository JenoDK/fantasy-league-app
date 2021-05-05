package com.jeno.fantasyleague.servlets;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.jeno.fantasyleague.backend.data.repository.UserRepository;

@Configuration
public class ServletConfiguration {

	@Bean
	public ServletRegistrationBean customServletBean(UserRepository userRepository) {
		ServletRegistrationBean bean = new ServletRegistrationBean(new ProfileImageServlet(userRepository), "/profileImage");
		return bean;
	}

}

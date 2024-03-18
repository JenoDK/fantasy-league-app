package com.jeno.fantasyleague.servlets;

import com.jeno.fantasyleague.backend.data.repository.UserRepository;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServletConfiguration {

	@Bean
	public ServletRegistrationBean customServletBean(UserRepository userRepository) {
		ServletRegistrationBean bean = new ServletRegistrationBean(new ProfileImageServlet(userRepository), "/profileImage");
		return bean;
	}

}

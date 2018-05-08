package com.jeno.fantasyleague.config;

import com.jeno.fantasyleague.data.security.SecurityHolder;
import com.jeno.fantasyleague.model.User;
import com.jeno.fantasyleague.resources.Resources;
import com.vaadin.server.ServiceException;
import com.vaadin.server.SessionExpiredException;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.server.SpringVaadinServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component("vaadinServlet")
public class CustomVaadinServlet extends SpringVaadinServlet {

	@Autowired
	private SecurityHolder securityHolder;

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		VaadinRequest vaadinRequest = createVaadinRequest(request);
		try {
			// We need to initialize the Vaadin Session first
			getService().findVaadinSession(vaadinRequest);
			if (VaadinSession.getCurrent().getAttribute(User.class) == null) {
				securityHolder.loadUser(createVaadinRequest(request));
			}
			Resources.set(loadResources());
		} catch (SessionExpiredException | ServiceException e) {
			throw new ServletException(e);
		}
		super.service(request, response);
	}
	private Resources loadResources() {
		return WebApplicationContextUtils.getWebApplicationContext(
				getServletContext()).getBean(Resources.class);
	}


}

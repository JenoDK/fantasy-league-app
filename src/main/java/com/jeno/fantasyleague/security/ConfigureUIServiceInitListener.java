package com.jeno.fantasyleague.security;

import org.springframework.beans.factory.annotation.Autowired;

import com.jeno.fantasyleague.resources.Resources;
import com.jeno.fantasyleague.ui.common.OfflineBanner;
import com.jeno.fantasyleague.ui.exception.AccessDeniedException;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.server.VaadinServiceInitListener;
import com.vaadin.flow.spring.annotation.SpringComponent;

/**
 * Adds before enter listener to check access to views.
 * Adds the Offline banner.
 * 
 */
@SpringComponent
public class ConfigureUIServiceInitListener implements VaadinServiceInitListener {

	@Autowired
	private Resources resources;
	@Autowired
	private SecurityHolder securityHolder;

	@Override
	public void serviceInit(ServiceInitEvent event) {
		event.getSource().addUIInitListener(uiEvent -> {
			final UI ui = uiEvent.getUI();
			ui.add(new OfflineBanner());
			ui.addBeforeEnterListener(this::beforeEnter);
			Resources.set(resources);
			securityHolder.loadUser(VaadinRequest.getCurrent());
		});
	}

	/**
	 * Reroutes the user if she is not authorized to access the view. 
	 *
	 * @param event
	 *            before navigation event with event details
	 */
	private void beforeEnter(BeforeEnterEvent event) {
		final boolean accessGranted = SecurityUtils.isAccessGranted(event.getNavigationTarget());
		if (!accessGranted) {
			if (SecurityUtils.isUserLoggedIn()) {
				event.rerouteToError(AccessDeniedException.class);
			}
		}
	}
}

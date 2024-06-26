package com.jeno.fantasyleague.security;

import com.jeno.fantasyleague.resources.Resources;
import com.jeno.fantasyleague.ui.common.OfflineBanner;
import com.jeno.fantasyleague.ui.exception.AccessDeniedException;
import com.jeno.fantasyleague.util.DateUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.server.VaadinServiceInitListener;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.beans.factory.annotation.Autowired;

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
			ui.getPage().retrieveExtendedClientDetails(extendedClientDetails -> {
				VaadinSession.getCurrent().setAttribute(DateUtil.TIMEZONE_ID, extendedClientDetails.getTimeZoneId());
				VaadinSession.getCurrent().setAttribute(DateUtil.TIMEZONE_OFFSET_ATTRIBUTE, extendedClientDetails.getRawTimezoneOffset() / 1000);
			});
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

package com.jeno.fantasyleague.ui.main.navigation;

import com.jeno.fantasyleague.model.User;
import com.jeno.fantasyleague.ui.main.broadcast.Notification;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;

public class TopBar extends HorizontalLayout {

	private final User user;
	private NavigationBar navigationBar;
	private ProfileMenuBar profileBar;

	public TopBar(User user) {
		super();
		this.user = user;

		initLayout();
	}

	private void initLayout() {
		addStyleName("topbar");
		setWidth("100%");
		setHeightUndefined();

		navigationBar = new NavigationBar();
		profileBar = new ProfileMenuBar(user);

		addComponent(navigationBar);
		addComponent(profileBar);
		setComponentAlignment(navigationBar, Alignment.MIDDLE_LEFT);
		setComponentAlignment(profileBar, Alignment.MIDDLE_RIGHT);
	}

	public void addClientNotification(Notification message) {
		profileBar.addClientNotification(message);
	}
}

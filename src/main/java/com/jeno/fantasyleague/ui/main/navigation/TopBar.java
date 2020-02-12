package com.jeno.fantasyleague.ui.main.navigation;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class TopBar extends HorizontalLayout {

	private NavigationBar navigationBar;

	public TopBar() {
		super();
		initLayout();
	}

	private void initLayout() {
		addClassName("topbar");
		setWidth("100%");
		navigationBar = new NavigationBar();
		add(navigationBar);
	}
}

package com.jeno.fantasyleague.ui.main.navigation;

import com.jeno.fantasyleague.ui.main.views.state.State;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.MenuBar;

import java.util.Comparator;

public class NavigationBar extends MenuBar {

	public NavigationBar() {
		initLayout();
	}

	private void initLayout() {
		setWidthUndefined();
		setHeight(60f, Unit.PIXELS);
		addStyleName("fantasy-league-menubar");

		State.getMenuItems().stream()
				.sorted(Comparator.comparing(State::getSeq))
				.forEach(this::addMenuBarItem);
	}

	private void addMenuBarItem(State state) {
		MenuItem item = addItem(
				state.getName(),
				new ThemeResource(state.getIconPath()),
				selectedItem -> getUI().getNavigator().navigateTo(state.getIdentifier()));
		item.setStyleName("menu-state-item");
		item.setText("");
	}

}

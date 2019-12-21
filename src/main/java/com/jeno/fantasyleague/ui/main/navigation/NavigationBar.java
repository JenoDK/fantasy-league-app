package com.jeno.fantasyleague.ui.main.navigation;

import java.util.Comparator;

import com.jeno.fantasyleague.ui.main.views.state.State;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.menubar.MenuBar;

public class NavigationBar extends MenuBar {

	public NavigationBar() {
		initLayout();
	}

	private void initLayout() {
		setWidthFull();
		setHeight("60px");
		addClassName("fantasy-league-menubar");

		State.getMenuItems().stream()
				.sorted(Comparator.comparing(State::getSeq))
				.forEach(this::addMenuBarItem);
	}

	private void addMenuBarItem(State state) {
		MenuItem item = addItem(
				state.getName(),
				selectedItem -> getUI().ifPresent(ui -> ui.navigate(state.getIdentifier())));
		item.addComponentAsFirst(new Icon("lumo", state.getIcon()));
		item.getElement().getClassList().add("menu-state-item");
		item.setText("");
	}

}

package com.jeno.fantasyleague.ui.common.tabsheet;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.tabs.Tab;

public abstract class LazyTab extends Tab {

	private final HasComponents mainLayout;

	private boolean isDrawn = false;
	private Component tabLayout;

	public LazyTab(String title, HasComponents mainLayout) {
		super(title);
		this.mainLayout = mainLayout;
	}

	public void show() {
		if (!isDrawn) {
			tabLayout = initLayout();
			mainLayout.add(tabLayout);
			isDrawn = true;
		}
		tabLayout.setVisible(true);
	}

	public void hide() {
		tabLayout.setVisible(false);
	}

	protected abstract Component initLayout();

}

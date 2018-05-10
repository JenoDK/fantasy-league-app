package com.jeno.fantasyleague.ui.common.tabsheet;

import com.vaadin.ui.VerticalLayout;

public class LazyTabComponent extends VerticalLayout {

	private final String tabId;

	public LazyTabComponent(String tabId) {
		this.tabId = tabId;

		setMargin(false);
		setSpacing(false);
	}

	public String getTabId() {
		return tabId;
	}
}

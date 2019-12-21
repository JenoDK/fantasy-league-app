package com.jeno.fantasyleague.ui.common.tabsheet;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;

public class LazyTabComponent extends Tab {

	private final String tabId;

	private VerticalLayout layout = new VerticalLayout();

	public LazyTabComponent(String tabId, String caption) {
		super(caption);
		this.tabId = tabId;

		add(layout);
		layout.setMargin(false);
		layout.setSpacing(false);
	}

	public String getTabId() {
		return tabId;
	}
}

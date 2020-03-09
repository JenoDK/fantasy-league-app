package com.jeno.fantasyleague.ui.common.tabsheet;

import com.vaadin.flow.component.tabs.Tab;

public class LazyTabComponent extends Tab {

	private final String tabId;

	public LazyTabComponent(String tabId, String caption) {
		super(caption);
		this.tabId = tabId;
	}

	public String getTabId() {
		return tabId;
	}
}

package com.jeno.fantasyleague.ui.common.tabsheet;

import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;

public abstract class CustomTabs extends Tabs {

	public CustomTabs() {
		super();
		addSelectedChangeListener(event -> {
			if (isLazyTab(event.getSelectedTab()) && isLazyTab(event.getPreviousTab())) {
				if (event.getPreviousTab() != null) {
					((LazyTab) event.getPreviousTab()).hide();
				}
				if (event.getSelectedTab() != null) {
					((LazyTab) event.getSelectedTab()).show();
				}
			} else {
				throw new RuntimeException("Only LazyTab's are supported in Customtabs class");
			}
		});
	}

	private boolean isLazyTab(Tab tab) {
		if (tab == null) {
			return true;
		}
		return tab instanceof LazyTab;
	}
}

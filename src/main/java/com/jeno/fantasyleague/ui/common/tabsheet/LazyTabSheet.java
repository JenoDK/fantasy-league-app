package com.jeno.fantasyleague.ui.common.tabsheet;

import java.util.Map;

import com.google.common.collect.Maps;
import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet;

public class LazyTabSheet extends TabSheet {

	Map<String, TabComponentCreationFunction> functions;

	public LazyTabSheet() {
		functions = Maps.newHashMap();
		addSelectedTabChangeListener(event -> {
			if (getSelectedTab() instanceof LazyTabComponent) {
				LazyTabComponent tabRootComponent = (LazyTabComponent) getSelectedTab();
				final String id = tabRootComponent.getTabId();

				if (functions.containsKey(id)) {
					tabRootComponent.addComponent(functions.get(id).createComponent());
					functions.remove(id);
				}
			}
		});
	}

	public void addLazyTab(String id, String caption, TabComponentCreationFunction function) {
		if (id == null || id.isEmpty()) {
			throw new RuntimeException("LazyTabs does not work with tabs without an ID");
		}
		LazyTabComponent rootLayout = new LazyTabComponent(id);
		functions.put(id, function);
		addTab(rootLayout, caption);
	}

	public interface TabComponentCreationFunction {
		Component createComponent();
	}
}

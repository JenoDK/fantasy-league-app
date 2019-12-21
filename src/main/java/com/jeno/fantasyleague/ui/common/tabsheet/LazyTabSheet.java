package com.jeno.fantasyleague.ui.common.tabsheet;

import java.util.Map;

import com.google.common.collect.Maps;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.tabs.Tabs;

public class LazyTabSheet extends Tabs {

	Map<String, TabComponentCreationFunction> functions;

	public LazyTabSheet() {
		functions = Maps.newHashMap();
		addSelectedChangeListener(event -> {
			if (getSelectedTab() instanceof LazyTabComponent) {
				LazyTabComponent tabRootComponent = (LazyTabComponent) getSelectedTab();
				final String id = tabRootComponent.getTabId();

				if (functions.containsKey(id)) {
					tabRootComponent.add(functions.get(id).createComponent());
					functions.remove(id);
				}
			}
		});
	}

	public void addLazyTab(String id, String caption, TabComponentCreationFunction function) {
		if (id == null || id.isEmpty()) {
			throw new RuntimeException("LazyTabs does not work with tabs without an ID");
		}
		LazyTabComponent rootLayout = new LazyTabComponent(id, caption);
		functions.put(id, function);
		add(rootLayout);
	}

	public interface TabComponentCreationFunction {
		Component createComponent();
	}
}

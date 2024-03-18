package com.jeno.fantasyleague.ui.common.tabsheet;

import com.google.common.collect.Maps;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class CustomMenuBar extends MenuBar {

	private VerticalLayout tabLayout;
	private String currentId;
	Map<String, LazyTabComponent.ComponentCreationFunction> functions;
	Map<String, LazyTabComponent> initializedTabs;

	public CustomMenuBar(VerticalLayout tabLayout) {
		this.tabLayout = tabLayout;
		this.functions = Maps.newHashMap();
		this.initializedTabs = Maps.newHashMap();
		setWidth("100%");
	}

	public MenuItem addItem(VaadinIcon icon, String text, ComponentEventListener<ClickEvent<MenuItem>> listener) {
		HorizontalLayout layout = new HorizontalLayout();
		Label label = new Label(text);
		label.getElement().getStyle().set("cursor", "pointer");
		layout.add(icon.create(), label);
		return addItem(layout, listener);
	}

	public MenuItem addItemToSubMenu(MenuItem subMenuItem, VaadinIcon icon, String text, ComponentEventListener<ClickEvent<MenuItem>> listener) {
		HorizontalLayout layout = new HorizontalLayout();
		Label label = new Label(text);
		label.getElement().getStyle().set("cursor", "pointer");
		layout.add(icon.create(), label);
		return subMenuItem.getSubMenu().addItem(layout, listener);
	}

	public CustomMenuItem addLazyItem(Optional<SubMenu> subMenu, String id, String caption, LazyTabComponent.ComponentCreationFunction function, ComponentEventListener<ClickEvent<MenuItem>> clickListener) {
		if (id == null || id.isEmpty()) {
			throw new RuntimeException("LazyTabs does not work with tabs without an ID");
		}
		functions.put(id, function);
		ComponentEventListener<ClickEvent<MenuItem>> listener = event -> {
			if (!Objects.equals(id, currentId)) {
				clickListener.onComponentEvent(event);
				if (initializedTabs.containsKey(id)) {
					initializedTabs.values().forEach(LazyTabComponent::hide);
					initializedTabs.get(id).show();
				} else if (functions.containsKey(id)) {
					initializedTabs.values().forEach(LazyTabComponent::hide);
					LazyTabComponent tabComponent = functions.get(id).createComponent();
					tabLayout.add(tabComponent);
					initializedTabs.put(id, tabComponent);
				}
				currentId = id;
			}
		};
		MenuItem menuItem;
		if (subMenu.isPresent()) {
			menuItem = subMenu.get().addItem(caption, listener);
		} else {
			menuItem = addItem(caption, listener);
		}
		return new CustomMenuItem(menuItem, listener);
	}

	public class CustomMenuItem {

		private MenuItem item;
		private ComponentEventListener<ClickEvent<MenuItem>> listener;

		public CustomMenuItem(MenuItem item, ComponentEventListener<ClickEvent<MenuItem>> listener) {
			this.item = item;
			this.listener = listener;
		}

		public MenuItem getItem() {
			return item;
		}

		public ComponentEventListener<ClickEvent<MenuItem>> getListener() {
			return listener;
		}
	}

}

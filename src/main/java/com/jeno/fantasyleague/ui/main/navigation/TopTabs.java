package com.jeno.fantasyleague.ui.main.navigation;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.jeno.fantasyleague.security.SecurityUtils;
import com.jeno.fantasyleague.ui.main.views.state.State;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabVariant;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.VaadinServlet;

public class TopTabs extends Tabs {

	public TopTabs() {
		super();
		initLayout();
	}

	private void initLayout() {
		setOrientation(Tabs.Orientation.HORIZONTAL);
		List<Tab> tabs = State.getMenuItems().stream()
				.filter(state -> SecurityUtils.isAccessGranted(state.getViewClass()))
				.sorted(Comparator.comparing(State::getSeq))
				.map(this::createTab)
			.collect(Collectors.toList());
		tabs.add(createTab(createLogoutLink()));
		add(tabs.toArray(Tab[]::new));
	}

	private Tab createTab(State state) {
		return createTab(state.getIcon(), state.getName(), state.getViewClass());
	}

	private static Tab createTab(VaadinIcon icon, String title, Class<? extends Component> viewClass) {
		RouterLink link = new RouterLink(null, viewClass);
		return createTab(populateLink(link, icon, title));
	}

	private static Tab createTab(Component content) {
		final Tab tab = new Tab();
		tab.addThemeVariants(TabVariant.LUMO_ICON_ON_TOP);
		tab.add(content);
		return tab;
	}

	private static Anchor createLogoutLink() {
		String contextPath = VaadinServlet.getCurrent().getServletContext().getContextPath();
		final Anchor a = populateLink(new Anchor(), VaadinIcon.ARROW_RIGHT, "Logout");
		a.setHref(contextPath + "/logout");
		return a;
	}

	private static <T extends HasComponents> T populateLink(T a, VaadinIcon icon, String title) {
		a.add(icon.create());
		a.add(title);
		return a;
	}
}

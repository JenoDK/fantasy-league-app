package com.jeno.fantasyleague.ui.main;

import static com.jeno.fantasyleague.util.AppConst.VIEWPORT;

import java.util.Optional;

import com.jeno.fantasyleague.ui.main.navigation.TopTabs;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

@Viewport(VIEWPORT)
@JsModule("./styles/shared-styles.js")
@Theme(value = Lumo.class, variant = Lumo.DARK)
public class MainView extends AppLayout {

	private TopTabs topTabs;

	public MainView() {
		this.setDrawerOpened(false);
		Span appName = new Span("");
		appName.addClassName("hide-on-mobile");

		topTabs = new TopTabs();

		this.addToNavbar(appName);
		this.addToNavbar(true, topTabs);
	}

	@Override
	protected void afterNavigation() {
		super.afterNavigation();

		String target = RouteConfiguration.forSessionScope().getUrl(this.getContent().getClass());
		Optional<Component> tabToSelect = topTabs.getChildren()
				.filter(tab -> {
					Component child = tab.getChildren().findFirst().get();
					return child instanceof RouterLink && ((RouterLink) child).getHref().equals(target);
				})
				.findFirst();
		tabToSelect.ifPresent(tab -> topTabs.setSelectedTab((Tab)tab));
	}

}

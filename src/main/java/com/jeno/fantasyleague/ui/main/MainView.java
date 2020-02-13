package com.jeno.fantasyleague.ui.main;

import static com.jeno.fantasyleague.util.AppConst.VIEWPORT;

import com.jeno.fantasyleague.ui.main.navigation.TopTabs;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.page.Viewport;

@Viewport(VIEWPORT)
@JsModule("./styles/shared-styles.js")
public class MainView extends AppLayout {

	public MainView() {

		this.setDrawerOpened(false);
		Span appName = new Span("");
		appName.addClassName("hide-on-mobile");

		TopTabs topTabs = new TopTabs();

		this.addToNavbar(appName);
		this.addToNavbar(true, topTabs);
	}

}

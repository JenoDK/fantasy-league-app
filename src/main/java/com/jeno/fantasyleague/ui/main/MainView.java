package com.jeno.fantasyleague.ui.main;

import static com.jeno.fantasyleague.util.AppConst.VIEWPORT;

import com.jeno.fantasyleague.ui.main.navigation.TopBar;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.server.PWA;

@Viewport(VIEWPORT)
@PWA(name = "Fantasy League", shortName = "fantasy-league",
		startPath = "login",
		backgroundColor = "#227aef", themeColor = "#227aef",
		offlinePath = "offline-page.html",
		offlineResources = {"images/offline-login-banner.jpg"})
public class MainView extends AppLayout {

	private TopBar topBar;

	public MainView() {
		topBar = new TopBar();
		this.addToNavbar(true, topBar);
	}

}

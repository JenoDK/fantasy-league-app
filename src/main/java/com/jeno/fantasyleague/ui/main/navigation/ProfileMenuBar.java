package com.jeno.fantasyleague.ui.main.navigation;

import com.jeno.fantasyleague.model.User;
import com.jeno.fantasyleague.ui.main.views.state.State;
import com.jeno.fantasyleague.util.Images;
import com.jeno.fantasyleague.util.VaadinUtil;
import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.MenuBar;

import java.io.ByteArrayInputStream;

public class ProfileMenuBar extends MenuBar {

	private final User user;

	public ProfileMenuBar(User user) {
		super();
		this.user = user;

		initLayout();
	}

	private void initLayout() {
		setWidthUndefined();
		setHeight(60f, Unit.PIXELS);
		addStyleName("fantasy-league-menubar");

		Resource resource;
		if (user.getProfile_picture() != null) {
			resource = new StreamResource(
					() -> new ByteArrayInputStream(user.getProfile_picture()),
					"prifle_picture.png");
		} else {
			resource = new ThemeResource(Images.DEFAULT_PROFILE_PICTURE);
		}
		MenuItem profileItem = addItem("profile", resource, null);
		profileItem.setStyleName("menu-state-item");
		profileItem.setText("");
		profileItem.addItem("Profile", item -> getUI().getNavigator().navigateTo(State.PROFILE.getIdentifier()));
		profileItem.addItem("Logout", item -> VaadinUtil.logout());
	}

}

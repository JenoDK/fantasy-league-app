package com.jeno.fantasyleague.ui.main;

import com.jeno.fantasyleague.model.User;
import com.jeno.fantasyleague.ui.main.views.state.State;
import com.vaadin.server.*;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.io.ByteArrayInputStream;
import java.util.Comparator;

public class NavigationBar extends HorizontalLayout {

	private final User user;

	public NavigationBar(User user) {
		this.user = user;

		init();
	}

	private void init() {
		setWidth("100%");

		// Add navigation bar
		final CssLayout menuBar = new CssLayout();
		menuBar.addStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
		// For each state add a navigation button
		State.getMenuItems().stream()
				.sorted(Comparator.comparing(State::getSeq))
				.forEach(stateOption -> menuBar.addComponent(createNavigationButton(stateOption.getName(), stateOption.getIdentifier())));
		addComponent(menuBar);

		// Add profile view to this, makes a bit more sense to not put it in menu bar
		final CssLayout accountBar = new CssLayout();
		accountBar.addStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);

		// Add profile button
		accountBar.addComponent(createNavigationButton(State.PROFILE.getName(), State.PROFILE.getIdentifier()));

		// Add logout button
		Button logoutButton = new Button("Logout");
		logoutButton.addStyleName(ValoTheme.BUTTON_SMALL);
		logoutButton.addClickListener(this::logout);
		accountBar.addComponent(logoutButton);

		Resource resource;
		if (user.getProfile_picture() != null) {
			resource = new StreamResource(
					() -> new ByteArrayInputStream(user.getProfile_picture()),
					"prifle_picture.png");
		} else {
			resource = new ThemeResource("images/default_profile_picture.png");
		}
		Image profileImage = new Image("", resource);
		profileImage.setWidth(70f, Unit.PIXELS);
		profileImage.setHeight(70f, Unit.PIXELS);

		accountBar.addComponent(profileImage);

		addComponent(accountBar);

		// Alignments
		setComponentAlignment(menuBar, Alignment.MIDDLE_LEFT);
		setComponentAlignment(accountBar, Alignment.MIDDLE_RIGHT);
	}

	private Button createNavigationButton(String caption, final String viewName) {
		Button button = new Button(caption);
		button.addStyleName(ValoTheme.BUTTON_SMALL);
		button.addClickListener((Button.ClickListener) event -> getUI().getNavigator().navigateTo(viewName));
		return button;
	}

	private void logout(Button.ClickEvent event) {
		String logoutUrl = VaadinService.getCurrentRequest().getContextPath() + "/logout";
		UI.getCurrent().getPage().setLocation(logoutUrl);
	}

}

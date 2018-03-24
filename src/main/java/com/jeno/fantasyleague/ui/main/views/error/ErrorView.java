package com.jeno.fantasyleague.ui.main.views.error;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class ErrorView extends VerticalLayout implements View {

	private Label message;

	public ErrorView() {
		setMargin(true);
		message = new Label("Please click one of the buttons at the top of the screen.");
		addComponent(message);
		message.addStyleName(ValoTheme.LABEL_COLORED);
	}

	@Override
	public void enter(ViewChangeListener.ViewChangeEvent event) {
	}

}

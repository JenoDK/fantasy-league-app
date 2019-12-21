package com.jeno.fantasyleague.ui.main.views.error;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class ErrorView extends VerticalLayout {

	private Label message;

	public ErrorView() {
		setMargin(true);
		message = new Label("Please click one of the buttons at the top of the screen.");
		add(message);
//		message.addClassName(ValoTheme.LABEL_COLORED);
	}

}

package com.jeno.demo.ui.views.accessdenied;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import javax.annotation.PostConstruct;

@SpringComponent
public class AccessDeniedView extends VerticalLayout implements View {

	private Label message;

	@PostConstruct
	void init() {
		setMargin(true);
		message = new Label();
		addComponent(message);
		message.addStyleName(ValoTheme.LABEL_FAILURE);
		message.setContentMode(ContentMode.HTML);
	}

	@Override
	public void enter(ViewChangeListener.ViewChangeEvent event) {
		message.setValue(String.format("Sorry, but you don't have access to the view <b>%s</b>.", event.getViewName()));
	}
}

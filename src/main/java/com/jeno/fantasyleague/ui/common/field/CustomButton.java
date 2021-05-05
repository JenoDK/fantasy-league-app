package com.jeno.fantasyleague.ui.common.field;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;

public class CustomButton extends Button {

	public CustomButton() {
		super();
		initStyles();
	}

	public CustomButton(String caption) {
		super(caption);
		initStyles();
	}

	public CustomButton(String caption, Component icon) {
		super(caption, icon);
		initStyles();
	}

	public CustomButton(String caption, ComponentEventListener<ClickEvent<Button>> listener) {
		super(caption, listener);
		initStyles();
	}

	public CustomButton(VaadinIcon icon) {
		super(icon.create());
		initStyles();
	}

	private void initStyles() {
		setThemeName("primary");
	}

	public void addPreventClickPropagation() {
		getElement().addEventListener("click", event -> {}).addEventData("event.stopPropagation()");
	}

}

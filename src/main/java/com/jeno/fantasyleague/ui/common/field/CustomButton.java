package com.jeno.fantasyleague.ui.common.field;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Resource;
import com.vaadin.ui.Button;
import com.vaadin.ui.themes.ValoTheme;

public class CustomButton extends Button {

	public CustomButton() {
		super();
		initStyles();
	}

	public CustomButton(String caption) {
		super(caption);
		initStyles();
	}

	public CustomButton(String caption, Resource icon) {
		super(caption, icon);
		initStyles();
	}

	public CustomButton(String caption, ClickListener listener) {
		super(caption, listener);
		initStyles();
	}

	public CustomButton(VaadinIcons icon) {
		super(icon);
		initStyles();
	}

	private void initStyles() {
		addStyleName(ValoTheme.BUTTON_PRIMARY);
		addStyleName(ValoTheme.BUTTON_TINY);
	}

}

package com.jeno.demo.ui.common;

import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;

public class CustomTitleForm extends FormLayout {

	private String title;

	private Label titleLabel;

	public CustomTitleForm(String title) {
		this.title = title;

		initLayout();
	}

	private void initLayout() {
		titleLabel = new Label(title, ContentMode.HTML);
		titleLabel.addStyleName(ValoTheme.LABEL_COLORED);
		titleLabel.addStyleName(ValoTheme.LABEL_H3);
		titleLabel.setWidthUndefined();

		addComponent(titleLabel);
		setComponentAlignment(titleLabel, Alignment.MIDDLE_CENTER);
	}

}

package com.jeno.fantasyleague.ui.common;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;

public class CustomTitleForm extends FormLayout {

	private String title;

	public CustomTitleForm(String title) {
		this.title = title;
		initLayout();
	}

	private void initLayout() {
		Label titleLabel = new Label(title);
		add(titleLabel);
	}

}

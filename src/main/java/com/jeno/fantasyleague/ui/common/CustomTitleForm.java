package com.jeno.fantasyleague.ui.common;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextField;

public class CustomTitleForm extends FormLayout {

	private String title;

	private TextField titleLabel;

	public CustomTitleForm(String title) {
		this.title = title;

		initLayout();
	}

	private void initLayout() {
		titleLabel = new TextField(title);

		add(titleLabel);
	}

}

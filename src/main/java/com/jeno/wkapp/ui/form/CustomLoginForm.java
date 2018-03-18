package com.jeno.wkapp.ui.form;

import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.LoginForm;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class CustomLoginForm extends VerticalLayout {

	private Label errorLabel;
	private LoginForm loginForm;

	public CustomLoginForm() {
		super();

		loginForm = new LoginForm();
		errorLabel = new Label("", ContentMode.HTML);

		addComponent(loginForm);
		addComponent(errorLabel);

		setComponentAlignment(loginForm, Alignment.MIDDLE_CENTER);
		setComponentAlignment(errorLabel, Alignment.MIDDLE_CENTER);
	}

	public void setError(String errorMsg) {
		errorLabel.setStyleName(ValoTheme.LABEL_FAILURE);
		errorLabel.setValue(errorMsg);
	}

	public void addLoginListener(LoginForm.LoginListener loginListener) {
		loginForm.addLoginListener(loginListener);
	}

}

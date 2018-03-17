package com.jeno.wkapp.ui.login;

import com.vaadin.server.ErrorMessage;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import sun.rmi.runtime.Log;

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

	public void addLoginListener(LoginForm.LoginListener loginEvent) {
		loginForm.addLoginListener(loginEvent);
	}

}

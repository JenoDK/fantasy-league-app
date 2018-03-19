package com.jeno.demo.ui.login;

import com.vaadin.server.ExternalResource;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

public class CustomLoginForm extends LoginForm {

	private Label errorLabel;
	private Link forgotPasswordLink;

	@Override
	protected Component createContent(TextField userNameField, PasswordField passwordField, Button loginButton) {
		forgotPasswordLink = new Link("Forgot password", new ExternalResource("/forgotPassword"));
		errorLabel = new Label("", ContentMode.HTML);

		VerticalLayout layout = new VerticalLayout();
		layout.setSpacing(true);
		layout.setMargin(true);
		layout.addComponent(userNameField);
		layout.addComponent(passwordField);
		layout.addComponent(forgotPasswordLink);
		layout.addComponent(loginButton);
		layout.addComponent(errorLabel);

		return layout;
	}

	public void setError(String errorMsg) {
		errorLabel.setStyleName(ValoTheme.LABEL_FAILURE);
		errorLabel.setValue(errorMsg);
	}

}

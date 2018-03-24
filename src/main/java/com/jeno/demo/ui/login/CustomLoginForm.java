package com.jeno.demo.ui.login;

import com.jeno.demo.ui.common.CustomTitleForm;
import com.vaadin.icons.VaadinIcons;
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
		forgotPasswordLink.setIcon(VaadinIcons.QUESTION_CIRCLE_O);
		errorLabel = new Label("", ContentMode.HTML);

		userNameField.setIcon(VaadinIcons.CLIPBOARD_USER);
		passwordField.setIcon(VaadinIcons.PASSWORD);
		loginButton.setIcon(VaadinIcons.USER_CHECK);

		CustomTitleForm layout = new CustomTitleForm("Login");
		layout.setWidthUndefined();
		layout.addComponent(userNameField);
		layout.addComponent(passwordField);
		layout.addComponent(forgotPasswordLink);
		layout.addComponent(loginButton);
		layout.addComponent(errorLabel);
		layout.setComponentAlignment(userNameField, Alignment.MIDDLE_CENTER);
		layout.setComponentAlignment(passwordField, Alignment.MIDDLE_CENTER);
		layout.setComponentAlignment(forgotPasswordLink, Alignment.MIDDLE_CENTER);
		layout.setComponentAlignment(loginButton, Alignment.MIDDLE_CENTER);
		layout.setComponentAlignment(errorLabel, Alignment.MIDDLE_CENTER);

		return layout;
	}

	public void setError(String errorMsg) {
		errorLabel.setStyleName(ValoTheme.LABEL_FAILURE);
		errorLabel.setValue(errorMsg);
	}

}

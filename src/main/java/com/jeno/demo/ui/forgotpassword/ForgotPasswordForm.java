package com.jeno.demo.ui.forgotpassword;

import com.jeno.demo.util.RxUtil;
import com.jeno.demo.util.VaadinUtil;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;
import io.reactivex.Observable;

public class ForgotPasswordForm extends FormLayout {

	private Button button;
	private TextField emailField;
	private Label errorLabel;

	public ForgotPasswordForm() {
		super();
		initLayout();
	}

	private void initLayout() {
		emailField = new TextField("Email");
		VaadinUtil.addValidator(emailField, new EmailValidator("Not a valid email adress"));

		button = new Button("Request password reset");

		errorLabel = new Label("", ContentMode.HTML);

		addComponent(emailField);
		addComponent(button);
		addComponent(errorLabel);
	}

	public void setError(String errorMsg) {
		errorLabel.setStyleName(ValoTheme.LABEL_FAILURE);
		errorLabel.setValue(errorMsg);
	}

	public Observable<String> resetPassword() {
		return RxUtil.clicks(button)
				.filter(ignored -> emailField.getComponentError() == null)
				.map(ignored -> emailField.getValue());
	}
}

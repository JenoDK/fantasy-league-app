package com.jeno.demo.ui.resetpassword;

import com.jeno.demo.model.User;
import com.jeno.demo.ui.common.CustomTitleForm;
import com.jeno.demo.util.RxUtil;
import com.jeno.demo.util.VaadinUtil;
import com.vaadin.data.BeanValidationBinder;
import com.vaadin.event.ShortcutAction;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.themes.ValoTheme;
import io.reactivex.Observable;

import java.util.Map;
import java.util.stream.Collectors;

public class ResetPasswordForm extends CustomTitleForm {

	private PasswordField passwordField;
	private PasswordField repeatPasswordField;

	private BeanValidationBinder<ResetPasswordBean> binder = new BeanValidationBinder<>(ResetPasswordBean.class);

	private Button submit;

	private Label errorLabel;

	public ResetPasswordForm(User user) {
		super("Resetting password for " + user.getUsername());
		initLayout();
		initBinder();
	}

	private void initBinder() {
		binder.forField(passwordField).bind("password");
		binder.forField(repeatPasswordField)
				.withValidator(VaadinUtil.getPasswordsMatchValidator(passwordField))
				.bind("repeatPassword");
		binder.setBean(new ResetPasswordBean());
	}

	private void initLayout() {
		setWidthUndefined();

		passwordField = new PasswordField("Password");
		repeatPasswordField = new PasswordField("Repeat Password");

		addComponent(passwordField);
		addComponent(repeatPasswordField);

		submit = new Button("Submit");
		submit.setClickShortcut(ShortcutAction.KeyCode.ENTER);
		addComponent(submit);

		errorLabel = new Label("", ContentMode.HTML);
		addComponent(errorLabel);
	}

	public Observable<String> validSubmit() {
		return RxUtil.clicks(submit)
				// Validate and don't emit if invalid
				.filter(ignored -> binder.validate().isOk())
				// Map to actual User object
				.map(ignored -> passwordField.getValue());
	}

	public void setErrorMap(Map<String, String> errorMap) {
		Map<String, String> errorsWithoutBinding = VaadinUtil.setErrorMap(binder, errorMap);
		if (!errorsWithoutBinding.isEmpty()) {
			errorLabel.setStyleName(ValoTheme.LABEL_FAILURE);
			errorLabel.setValue(errorMap.values().stream().collect(Collectors.joining("<br/>")));
		}
	}
}

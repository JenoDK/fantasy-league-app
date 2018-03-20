package com.jeno.demo.ui.register;

import com.jeno.demo.model.User;
import com.jeno.demo.ui.common.CustomTitleForm;
import com.jeno.demo.util.RxUtil;
import com.jeno.demo.util.VaadinUtil;
import com.vaadin.data.BeanValidationBinder;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.ErrorMessage;
import com.vaadin.shared.ui.ErrorLevel;
import com.vaadin.ui.*;
import io.reactivex.Observable;

import java.util.Map;

public class RegisterUserForm extends CustomTitleForm {

	private TextField nameField;
	private TextField userNameField;
	private TextField emailField;
	private PasswordField passwordField;
	private PasswordField repeatPasswordField;

	private Link forgotPasswordLink;

	private Button submit;

	private BeanValidationBinder<UserBean> binder = new BeanValidationBinder<>(UserBean.class);

	public RegisterUserForm() {
		super("Register");
		initLayout();
		initBinder();
	}

	private void initBinder() {
		binder.forField(nameField).bind("name");
		binder.forField(userNameField).bind("username");
		binder.forField(emailField)
				.withValidator(new EmailValidator("Not a valid email adress"))
				.bind("email");
		binder.forField(passwordField).bind("password");
		binder.forField(repeatPasswordField)
			.withValidator(VaadinUtil.getPasswordsMatchValidator(passwordField))
			.bind("repeatPassword");
		binder.setBean(new UserBean());
	}

	private void initLayout() {
		setWidthUndefined();

		nameField = new TextField("Name");
		userNameField = new TextField("Username");
		emailField = new TextField("Email");
		passwordField = new PasswordField("Password");
		repeatPasswordField = new PasswordField("Repeat Password");

		addComponent(nameField);
		addComponent(userNameField);
		addComponent(emailField);
		addComponent(passwordField);
		addComponent(repeatPasswordField);

		submit = new Button("Submit");
		submit.setClickShortcut(ShortcutAction.KeyCode.ENTER);
		addComponent(submit);
	}

	public Observable<User> validSubmit() {
		return RxUtil.clicks(submit)
				// Validate and don't emit if invalid
				.filter(ignored -> binder.validate().isOk())
				// Map to actual User object
				.map(ignored -> binder.getBean().createUserObject());
	}

	public void setErrorMap(Map<String, String> errorMap) {
		VaadinUtil.setErrorMap(binder, errorMap);
	}
}

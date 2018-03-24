package com.jeno.fantasyleague.ui.register;

import com.jeno.fantasyleague.model.User;
import com.jeno.fantasyleague.ui.common.CustomTitleForm;
import com.jeno.fantasyleague.util.RxUtil;
import com.jeno.fantasyleague.util.VaadinUtil;
import com.vaadin.data.BeanValidationBinder;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Button;
import com.vaadin.ui.Link;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
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
		nameField.setIcon(VaadinIcons.USER);

		userNameField = new TextField("Username");
		userNameField.setIcon(VaadinIcons.CLIPBOARD_USER);

		emailField = new TextField("Email");
		emailField.setIcon(VaadinIcons.MAILBOX);

		passwordField = new PasswordField("Password");
		passwordField.setIcon(VaadinIcons.PASSWORD);

		repeatPasswordField = new PasswordField("Repeat Password");
		repeatPasswordField.setIcon(VaadinIcons.PASSWORD);

		addComponent(nameField);
		addComponent(userNameField);
		addComponent(emailField);
		addComponent(passwordField);
		addComponent(repeatPasswordField);

		submit = new Button("Submit", VaadinIcons.USER_CHECK);
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

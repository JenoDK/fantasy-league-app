package com.jeno.fantasyleague.ui.register;

import java.util.Map;

import com.jeno.fantasyleague.backend.model.User;
import com.jeno.fantasyleague.ui.common.CustomTitleForm;
import com.jeno.fantasyleague.util.RxUtil;
import com.jeno.fantasyleague.util.VaadinUtil;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.validator.EmailValidator;
import io.reactivex.Observable;

public class RegisterUserForm extends CustomTitleForm {

	private TextField nameField;
	private TextField userNameField;
	private TextField emailField;
	private PasswordField passwordField;
	private PasswordField repeatPasswordField;

	private Anchor forgotPasswordLink;

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
		nameField = new TextField("Name");
		nameField.setPrefixComponent(VaadinIcon.USER.create());

		userNameField = new TextField("Username");
		userNameField.setPrefixComponent(VaadinIcon.CLIPBOARD_USER.create());

		emailField = new TextField("Email");
		emailField.setPrefixComponent(VaadinIcon.MAILBOX.create());

		passwordField = new PasswordField("Password");
		passwordField.setPrefixComponent(VaadinIcon.PASSWORD.create());

		repeatPasswordField = new PasswordField("Repeat Password");
		repeatPasswordField.setPrefixComponent(VaadinIcon.PASSWORD.create());

		add(nameField);
		add(userNameField);
		add(emailField);
		add(passwordField);
		add(repeatPasswordField);

		submit = new Button("Submit", VaadinIcon.USER_CHECK.create());
		submit.addClickShortcut(Key.ENTER);
		add(submit);
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

package com.jeno.fantasyleague.ui.register;

import java.util.Map;

import com.jeno.fantasyleague.backend.model.User;
import com.jeno.fantasyleague.ui.common.CustomTitleForm;
import com.jeno.fantasyleague.ui.common.image.ImageUploadWithPlaceholder;
import com.jeno.fantasyleague.util.RxUtil;
import com.jeno.fantasyleague.util.VaadinUtil;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.validator.EmailValidator;
import io.reactivex.Observable;

@CssImport(value = "./styles/shared-styles.css")
public class RegisterUserForm extends CustomTitleForm {

	private TextField nameField;
	private TextField userNameField;
	private EmailField emailField;
	private PasswordField passwordField;
	private PasswordField repeatPasswordField;
	private ImageUploadWithPlaceholder profilePictureField;

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
		addClassName("width-mobile-100");
		addClassName("width-40");
		setResponsiveSteps(new ResponsiveStep("0", 1, ResponsiveStep.LabelsPosition.TOP));

		nameField = new TextField();
		nameField.setPlaceholder("Name");
		nameField.setPrefixComponent(VaadinIcon.USER.create());

		userNameField = new TextField();
		userNameField.setPlaceholder("Username");
		userNameField.setPrefixComponent(VaadinIcon.CLIPBOARD_USER.create());

		emailField = new EmailField();
		emailField.setPlaceholder("Email");
		emailField.setPrefixComponent(VaadinIcon.MAILBOX.create());

		passwordField = new PasswordField();
		passwordField.setPlaceholder("Password");
		passwordField.setPrefixComponent(VaadinIcon.PASSWORD.create());

		repeatPasswordField = new PasswordField();
		repeatPasswordField.setPlaceholder("Repeat Password");
		repeatPasswordField.setPrefixComponent(VaadinIcon.PASSWORD.create());

		profilePictureField = new ImageUploadWithPlaceholder();

		add(nameField);
		add(userNameField);
		add(emailField);
		add(passwordField);
		add(repeatPasswordField);
		add(profilePictureField);

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

	public ImageUploadWithPlaceholder getProfilePictureUploader() {
		return profilePictureField;
	}
}

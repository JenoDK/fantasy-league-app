package com.jeno.fantasyleague.ui.resetpassword;

import java.util.Map;
import java.util.stream.Collectors;

import com.jeno.fantasyleague.backend.model.User;
import com.jeno.fantasyleague.ui.common.CustomTitleForm;
import com.jeno.fantasyleague.util.RxUtil;
import com.jeno.fantasyleague.util.VaadinUtil;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import io.reactivex.Observable;

@CssImport(value = "./styles/shared-styles.css")
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
		addClassName("width-mobile-100");
		addClassName("width-40");
		setResponsiveSteps(new ResponsiveStep("0", 1, ResponsiveStep.LabelsPosition.TOP));

		passwordField = new PasswordField("Password");
		passwordField.setPrefixComponent(VaadinIcon.PASSWORD.create());
		repeatPasswordField = new PasswordField("Repeat Password");
		repeatPasswordField.setPrefixComponent(VaadinIcon.PASSWORD.create());

		add(passwordField);
		add(repeatPasswordField);

		submit = new Button("Submit", VaadinIcon.USER_CHECK.create());
		submit.addClickShortcut(Key.ENTER);
		add(submit);

		errorLabel = new Label("");
		add(errorLabel);
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
//			errorLabel.addClassName(ValoTheme.LABEL_FAILURE);
			errorLabel.setText(errorMap.values().stream().collect(Collectors.joining("<br/>")));
		}
	}
}

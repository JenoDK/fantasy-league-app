package com.jeno.demo.ui.forgotpassword;

import com.jeno.demo.ui.common.CustomTitleForm;
import com.jeno.demo.ui.resetpassword.ResetPasswordBean;
import com.jeno.demo.util.RxUtil;
import com.jeno.demo.util.VaadinUtil;
import com.vaadin.data.BeanValidationBinder;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;
import io.reactivex.Observable;

public class ForgotPasswordForm extends CustomTitleForm {

	private Button button;
	private TextField emailField;
	private Label errorLabel;

	private BeanValidationBinder<ForgotPasswordBean> binder = new BeanValidationBinder<>(ForgotPasswordBean.class);

	public ForgotPasswordForm() {
		super("Forgot password");
		initLayout();
		initBinder();
	}

	private void initBinder() {
		binder.forField(emailField)
				.withValidator(new EmailValidator("Not a valid email adress"))
				.bind("email");
		binder.setBean(new ForgotPasswordBean());
	}

	private void initLayout() {
		emailField = new TextField("Email");
		emailField.setIcon(VaadinIcons.MAILBOX);

		button = new Button("Reset", VaadinIcons.REFRESH);

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
				.filter(ignored -> binder.validate().isOk())
				.map(ignored -> emailField.getValue());
	}
}

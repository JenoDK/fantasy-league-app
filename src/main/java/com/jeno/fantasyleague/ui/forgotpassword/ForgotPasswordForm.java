package com.jeno.fantasyleague.ui.forgotpassword;

import com.jeno.fantasyleague.ui.common.CustomTitleForm;
import com.jeno.fantasyleague.util.RxUtil;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.validator.EmailValidator;
import io.reactivex.Observable;

public class ForgotPasswordForm extends CustomTitleForm {

	private Button button;
	private EmailField emailField;
	private Text errorLabel;

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
		emailField = new EmailField("Email");
		emailField.setPrefixComponent(VaadinIcon.MAILBOX.create());

		button = new Button("Reset", VaadinIcon.REFRESH.create());

		errorLabel = new Text("");

		add(emailField);
		add(button);
		add(errorLabel);
	}

	public void setError(String errorMsg) {
		errorLabel.setText(errorMsg);
	}

	public Observable<String> resetPassword() {
		return RxUtil.clicks(button)
				.filter(ignored -> binder.validate().isOk())
				.map(ignored -> emailField.getValue());
	}
}

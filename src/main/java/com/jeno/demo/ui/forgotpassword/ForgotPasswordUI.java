package com.jeno.demo.ui.forgotpassword;

import com.jeno.demo.data.repository.UserRepository;
import com.jeno.demo.data.service.email.PasswordResetService;
import com.jeno.demo.exception.EmailException;
import com.jeno.demo.model.User;
import com.jeno.demo.ui.RedirectUI;
import com.jeno.demo.util.VaadinUtil;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Component;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@SpringUI(path = "/forgotPassword")
@Title("Forgot Password")
@Theme("valo")
public class ForgotPasswordUI extends RedirectUI {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordResetService passwordResetService;

	private ForgotPasswordForm form;

	public ForgotPasswordUI() {
		super("Login", "/login");
	}

	@Override
	protected Component getMiddleComponent() {
		form = new ForgotPasswordForm();
		form.setWidthUndefined();
		form.resetPassword()
				.map(email -> {
					Optional<User> user = userRepository.findByEmail(email);
					if (!user.isPresent()) {
						form.setError("No account for email " + email + " found.");
					}
					return user;
				})
				.filter(Optional::isPresent)
				.map(Optional::get)
				.subscribe(this::resetPassword);
		return form;
	}

	private void resetPassword(User user) {
		try {
			passwordResetService.sendPasswordResetMail(user, VaadinUtil.getRootRequestURL());
			actionSuccessful("Email to reset password sent to " + user.getEmail());
		} catch (EmailException e) {
			form.setError("Something went wrong, try again later");
		}
	}

}

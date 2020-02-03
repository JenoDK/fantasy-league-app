package com.jeno.fantasyleague.ui.forgotpassword;

import java.util.Optional;

import com.jeno.fantasyleague.backend.data.repository.UserRepository;
import com.jeno.fantasyleague.backend.data.service.email.PasswordResetService;
import com.jeno.fantasyleague.backend.model.User;
import com.jeno.fantasyleague.exception.EmailException;
import com.jeno.fantasyleague.ui.RedirectUI;
import com.jeno.fantasyleague.ui.annotation.AlwaysAllow;
import com.jeno.fantasyleague.util.VaadinUtil;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@PageTitle("Forgot Password")
@Route("forgotPassword")
@AlwaysAllow
public class ForgotPasswordUI extends RedirectUI {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordResetService passwordResetService;

	private ForgotPasswordForm form;

	public ForgotPasswordUI() {
		super();
	}

	@Override
	protected Component getMiddleComponent() {
		form = new ForgotPasswordForm();
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

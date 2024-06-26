package com.jeno.fantasyleague.ui.forgotpassword;

import com.jeno.fantasyleague.backend.data.repository.UserRepository;
import com.jeno.fantasyleague.backend.data.service.email.PasswordResetService;
import com.jeno.fantasyleague.backend.model.User;
import com.jeno.fantasyleague.exception.EmailException;
import com.jeno.fantasyleague.ui.RedirectUI;
import com.jeno.fantasyleague.ui.annotation.AlwaysAllow;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.Optional;

@PageTitle("Forgot Password")
@Route("forgotPassword")
@AlwaysAllow
public class ForgotPasswordUI extends RedirectUI implements RouterLayout {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordResetService passwordResetService;

	@Value("${app.base.url}")
	private String baseUrl;

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
			passwordResetService.sendPasswordResetMail(user, baseUrl);
			actionSuccessful("Email to reset password sent to " + user.getEmail());
		} catch (EmailException e) {
			form.setError("Something went wrong, try again later");
		}
	}

}

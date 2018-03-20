package com.jeno.demo.ui.resetpassword;

import com.jeno.demo.data.dao.UserDao;
import com.jeno.demo.data.dao.ValidationException;
import com.jeno.demo.data.repository.PasswordResetTokenRepository;
import com.jeno.demo.data.repository.UserRepository;
import com.jeno.demo.model.PasswordResetToken;
import com.jeno.demo.model.User;
import com.jeno.demo.ui.RedirectUI;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Date;

@SpringUI(path = "/resetPassword")
@Title("Reset Password")
@Theme("valo")
public class ResetPasswordUI extends RedirectUI {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordResetTokenRepository passwordResetTokenRepository;
	@Autowired
	private UserDao userDao;

	Component middleComponent;

	private ResetPasswordForm form;
	private Label errorLabel;
	private User user;

	public ResetPasswordUI() {
		super("Login", "/login");
	}

	@Override
	protected void init(VaadinRequest request) {
		try {
			user = processResetPasswordRequest(request);
			form = new ResetPasswordForm(user);
			form.validSubmit().subscribe(newPassword -> {
				try {
					user.setPassword(newPassword);
					userDao.update(user);
					actionSuccessful("Password updated for account " + user.getUsername());
				} catch (ValidationException ex) {
					form.setErrorMap(ex.getErrorMap());
				}
			});
			middleComponent = form;
		} catch (InvalidResetPasswordRequest e) {
			middleComponent = createErrorComponent("Bad request: " + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			middleComponent = createErrorComponent("Something went wrong, try again later or contact administrator");
		}
		super.init(request);
	}

	private User processResetPasswordRequest(VaadinRequest request) {
		String[] idParameterValue = request.getParameterMap().get("id");
		String[] tokenParameterValue = request.getParameterMap().get("token");

		String userId = Arrays.stream(idParameterValue)
				.findFirst()
				.orElseThrow(() -> new InvalidResetPasswordRequest("Missing id parameter in URL"));

		String token = Arrays.stream(tokenParameterValue)
				.findFirst()
				.orElseThrow(() -> new InvalidResetPasswordRequest("Missing token parameter in URL"));

		// Map userid to Long
		Long userIdLong;
		try {
			userIdLong = Long.valueOf(userId);
		} catch (NumberFormatException e) {
			throw new InvalidResetPasswordRequest("Bad 'id' parameter in URL");
		}

		// Fetch user
		User user = userRepository.findById(userIdLong)
				.orElseThrow(() -> new InvalidResetPasswordRequest("User not found"));
		// Fetch passwordResetToken
		PasswordResetToken pwResettoken = passwordResetTokenRepository.findByTokenAndUser(token, user)
				.orElseThrow(() -> new InvalidResetPasswordRequest("No password reset token found for user " + user.getUsername()));

		// Check if token is not expired
		Date now = new Date();
		if (now.before(pwResettoken.getExpiryDate())) {
			return user;
		} else {
			throw new InvalidResetPasswordRequest("Password reset token expired, request a new one");
		}
	}

	private Label createErrorComponent(String errorMessage) {
		errorLabel = new Label("", ContentMode.HTML);
		errorLabel.setStyleName(ValoTheme.LABEL_FAILURE);
		errorLabel.setValue(errorMessage);
		return errorLabel;
	}

	@Override
	protected Component getMiddleComponent() {
		return middleComponent;
	}

}

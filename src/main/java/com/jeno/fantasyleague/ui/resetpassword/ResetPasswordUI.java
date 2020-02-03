package com.jeno.fantasyleague.ui.resetpassword;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.jeno.fantasyleague.backend.data.dao.UserDao;
import com.jeno.fantasyleague.backend.data.dao.ValidationException;
import com.jeno.fantasyleague.backend.data.repository.PasswordResetTokenRepository;
import com.jeno.fantasyleague.backend.data.repository.UserRepository;
import com.jeno.fantasyleague.backend.model.PasswordResetToken;
import com.jeno.fantasyleague.backend.model.User;
import com.jeno.fantasyleague.ui.RedirectUI;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Location;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@PageTitle("Reset Password")
@Route("resetPassword")
public class ResetPasswordUI extends RedirectUI implements HasUrlParameter<String> {

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
		super();
	}


	@Override
	public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
		Location location = event.getLocation();
		QueryParameters queryParameters = location.getQueryParameters();
		Map<String, List<String>> parametersMap = queryParameters.getParameters();
		try {
			user = processResetPasswordRequest(parametersMap);
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
	}

	private User processResetPasswordRequest(Map<String, List<String>> parametersMap) {
		List<String> idParameterValue = parametersMap.get("id");
		List<String> tokenParameterValue = parametersMap.get("token");

		String userId = idParameterValue.stream()
				.findFirst()
				.orElseThrow(() -> new InvalidResetPasswordRequest("Missing id parameter in URL"));

		String token = tokenParameterValue.stream()
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
				.filter(pwResettoken1 -> !pwResettoken1.isUsed())
				.orElseThrow(() -> new InvalidResetPasswordRequest("No active password reset token found for user " + user.getUsername()));

		// Check if token is not expired
		Date now = new Date();
		if (now.before(pwResettoken.getExpiryDate())) {
			pwResettoken.setUsed(true);
			passwordResetTokenRepository.saveAndFlush(pwResettoken);
			return user;
		} else {
			throw new InvalidResetPasswordRequest("Password reset token expired, request a new one");
		}
	}

	private Label createErrorComponent(String errorMessage) {
		errorLabel = new Label("");
//		errorLabel.setStyleName(ValoTheme.LABEL_FAILURE);
//		errorLabel.setValue(errorMessage);
		return errorLabel;
	}

	@Override
	protected Component getMiddleComponent() {
		return middleComponent;
	}

}

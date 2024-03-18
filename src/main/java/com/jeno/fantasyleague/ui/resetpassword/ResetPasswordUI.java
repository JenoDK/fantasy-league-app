package com.jeno.fantasyleague.ui.resetpassword;

import com.jeno.fantasyleague.backend.data.dao.UserDao;
import com.jeno.fantasyleague.backend.data.dao.ValidationException;
import com.jeno.fantasyleague.backend.data.repository.PasswordResetTokenRepository;
import com.jeno.fantasyleague.backend.data.repository.UserRepository;
import com.jeno.fantasyleague.backend.model.PasswordResetToken;
import com.jeno.fantasyleague.backend.model.User;
import com.jeno.fantasyleague.ui.RedirectUI;
import com.jeno.fantasyleague.ui.annotation.AlwaysAllow;
import com.jeno.fantasyleague.ui.common.label.StatusLabel;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.router.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.Map;

@PageTitle("Reset Password")
@Route("resetPassword")
@AlwaysAllow
public class ResetPasswordUI extends RedirectUI implements HasUrlParameter<String>, RouterLayout {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordResetTokenRepository passwordResetTokenRepository;
	@Autowired
	private UserDao userDao;

	Component middleComponent;

	private ResetPasswordForm form;
	private StatusLabel errorLabel;

	public ResetPasswordUI() {
		super(false);
	}


	@Override
	public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
		Location location = event.getLocation();
		QueryParameters queryParameters = location.getQueryParameters();
		Map<String, List<String>> parametersMap = queryParameters.getParameters();
		try {
			PasswordResetToken token = processResetPasswordRequest(parametersMap);
			User user = token.getUser();
			form = new ResetPasswordForm(user);
			form.validSubmit().subscribe(newPassword -> {
				try {
					user.setPassword(newPassword);
					userDao.update(user);
					token.setUsed(true);
					passwordResetTokenRepository.saveAndFlush(token);
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
		initLayout();
	}

	private PasswordResetToken processResetPasswordRequest(Map<String, List<String>> parametersMap) {
		List<String> tokenParameterValue = parametersMap.get("token");

		String token = tokenParameterValue.stream()
				.findFirst()
				.orElseThrow(() -> new InvalidResetPasswordRequest("Missing token parameter in URL"));

		// Fetch passwordResetToken
		PasswordResetToken pwResettoken = passwordResetTokenRepository.findByToken(token)
				.filter(pwResettoken1 -> !pwResettoken1.isUsed())
				.orElseThrow(() -> new InvalidResetPasswordRequest("No active password reset token found for token " + token + ". Please retry resetting your password"));

		// Check if token is not expired
		Date now = new Date();
		if (now.before(pwResettoken.getExpiryDate())) {
			return pwResettoken;
		} else {
			throw new InvalidResetPasswordRequest("Password reset token expired, request a new one");
		}
	}

	private Label createErrorComponent(String errorMessage) {
		errorLabel = new StatusLabel(true);
		errorLabel.setErrorText(errorMessage);
		return errorLabel;
	}

	@Override
	protected Component getMiddleComponent() {
		return middleComponent;
	}

}

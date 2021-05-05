package com.jeno.fantasyleague.ui.activateaccount;

import java.util.List;
import java.util.Map;

import com.jeno.fantasyleague.backend.data.dao.UserDao;
import com.jeno.fantasyleague.backend.data.repository.AccountActivationTokenRepository;
import com.jeno.fantasyleague.backend.data.repository.UserRepository;
import com.jeno.fantasyleague.backend.model.AccountActivationToken;
import com.jeno.fantasyleague.backend.model.User;
import com.jeno.fantasyleague.ui.RedirectUI;
import com.jeno.fantasyleague.ui.resetpassword.InvalidResetPasswordRequest;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Location;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLayout;

import org.springframework.beans.factory.annotation.Autowired;

@PageTitle("Reset Password")
@Route("activateAccount")
public class ActivateAccountUI extends RedirectUI implements HasUrlParameter<String>, RouterLayout {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private AccountActivationTokenRepository accountActivationTokenRepository;
	@Autowired
	private UserDao userDao;

	Component middleComponent;

	private Label errorLabel;
	private User user;

	public ActivateAccountUI() {
		super();
	}

	@Override
	public void setParameter(BeforeEvent event, String parameter) {
		Location location = event.getLocation();
		QueryParameters queryParameters = location.getQueryParameters();
		Map<String, List<String>> parametersMap = queryParameters.getParameters();
		try {
			user = processResetPasswordRequest(parametersMap);
			user.setActive(true);
			userDao.update(user);
			middleComponent = new VerticalLayout();
			actionSuccessful("Thanks for your activation " + user.getUsername());
		} catch (InvalidAccountActivationRequest e) {
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
			throw new InvalidAccountActivationRequest("Bad 'id' parameter in URL");
		}

		// Fetch user
		User user = userRepository.findById(userIdLong)
				.orElseThrow(() -> new InvalidAccountActivationRequest("User not found"));
		// Fetch passwordResetToken
		AccountActivationToken accountActivationToken = accountActivationTokenRepository.findByTokenAndUser(token, user)
				.filter(accountActivationToken1 -> !accountActivationToken1.isUsed())
				.orElseThrow(() -> new InvalidAccountActivationRequest("No active account activation token found for user " + user.getUsername()));

		accountActivationToken.setUsed(true);
		accountActivationTokenRepository.saveAndFlush(accountActivationToken);
		return user;
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

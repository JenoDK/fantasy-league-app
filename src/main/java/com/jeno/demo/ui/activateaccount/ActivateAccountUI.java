package com.jeno.demo.ui.activateaccount;

import com.jeno.demo.data.dao.UserDao;
import com.jeno.demo.data.repository.AccountActivationTokenRepository;
import com.jeno.demo.data.repository.UserRepository;
import com.jeno.demo.model.AccountActivationToken;
import com.jeno.demo.model.User;
import com.jeno.demo.ui.RedirectUI;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

@SpringUI(path = "/activateAccount")
@Title("Reset Password")
@Theme("valo")
public class ActivateAccountUI extends RedirectUI {

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
		super("Login", "/login");
	}

	@Override
	protected void init(VaadinRequest request) {
		try {
			user = processResetPasswordRequest(request);
			user.setActive(true);
			userDao.update(user);
			middleComponent = new VerticalLayout();
			super.init(request);
			actionSuccessful("Thanks for your activation " + user.getUsername());
		} catch (InvalidAccountActivationRequest e) {
			middleComponent = createErrorComponent("Bad request: " + e.getMessage());
			super.init(request);
		} catch (Exception e) {
			e.printStackTrace();
			middleComponent = createErrorComponent("Something went wrong, try again later or contact administrator");
			super.init(request);
		}
	}

	private User processResetPasswordRequest(VaadinRequest request) {
		String[] idParameterValue = request.getParameterMap().get("id");
		String[] tokenParameterValue = request.getParameterMap().get("token");

		String userId = Arrays.stream(idParameterValue)
				.findFirst()
				.orElseThrow(() -> new InvalidAccountActivationRequest("Missing id parameter in URL"));

		String token = Arrays.stream(tokenParameterValue)
				.findFirst()
				.orElseThrow(() -> new InvalidAccountActivationRequest("Missing token parameter in URL"));

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

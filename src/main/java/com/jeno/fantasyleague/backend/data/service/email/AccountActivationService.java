package com.jeno.fantasyleague.backend.data.service.email;

import com.jeno.fantasyleague.backend.data.repository.AccountActivationTokenRepository;
import com.jeno.fantasyleague.backend.model.AccountActivationToken;
import com.jeno.fantasyleague.backend.model.User;
import com.jeno.fantasyleague.exception.EmailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AccountActivationService {

	@Autowired
	private AccountActivationTokenRepository accountActivationTokenRepository;

	@Autowired
	private ApplicationEmailService emailService;

	public void sendAccountActivationEmail(User user, String contextPath) throws EmailException {
		String token = UUID.randomUUID().toString();
		AccountActivationToken accountActivationToken = new AccountActivationToken(user, token);
		accountActivationTokenRepository.save(accountActivationToken);
		String url = contextPath + "/activateAccount?id=" + user.getId() + "&token=" + token;
		emailService.sendEmail(
				"Activate Account " + user.getUsername(),
				"Click the following link to activate your account: " + url,
				user);
	}
}

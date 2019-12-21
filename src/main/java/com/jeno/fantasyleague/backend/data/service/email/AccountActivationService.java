package com.jeno.fantasyleague.backend.data.service.email;

import java.util.UUID;

import com.jeno.fantasyleague.backend.data.repository.AccountActivationTokenRepository;
import com.jeno.fantasyleague.exception.EmailException;
import com.jeno.fantasyleague.backend.model.AccountActivationToken;
import com.jeno.fantasyleague.backend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

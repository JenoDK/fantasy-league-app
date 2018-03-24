package com.jeno.demo.data.service.email;

import com.google.common.collect.Lists;
import com.jeno.demo.data.repository.AccountActivationTokenRepository;
import com.jeno.demo.exception.EmailException;
import com.jeno.demo.model.AccountActivationToken;
import com.jeno.demo.model.User;
import it.ozimov.springboot.mail.model.Email;
import it.ozimov.springboot.mail.model.defaultimpl.DefaultEmail;
import it.ozimov.springboot.mail.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.internet.InternetAddress;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

@Service
public class AccountActivationService {

	@Autowired
	private AccountActivationTokenRepository accountActivationTokenRepository;

	@Autowired
	private EmailService emailService;

	public void sendAccountActivationEmail(User user, String contextPath) throws EmailException {
		String token = UUID.randomUUID().toString();
		AccountActivationToken accountActivationToken = new AccountActivationToken(user, token);
		accountActivationTokenRepository.save(accountActivationToken);
		String url = contextPath + "/activateAccount?id=" + user.getId() + "&token=" + token;
		try {
			final Email email = DefaultEmail.builder()
					.from(new InternetAddress("jenotestemail@gmail.com", "Jeno Test"))
					.to(Lists.newArrayList(new InternetAddress(user.getEmail(), user.getName())))
					.subject("Activate Account " + user.getUsername())
					.body("Click the following link to activate your account: " + url)
					.encoding("UTF-8").build();

			emailService.send(email);
		} catch (UnsupportedEncodingException | RuntimeException e) {
			throw new EmailException("Something went wrong", e);
		}
	}
}

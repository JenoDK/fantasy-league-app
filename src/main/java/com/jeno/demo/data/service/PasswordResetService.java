package com.jeno.demo.data.service;

import com.google.common.collect.Lists;
import com.jeno.demo.data.repository.PasswordResetTokenRepository;
import com.jeno.demo.exception.EmailException;
import com.jeno.demo.model.PasswordResetToken;
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
public class PasswordResetService {

	@Autowired
	private PasswordResetTokenRepository passwordResetTokenRepository;

	@Autowired
	private EmailService emailService;

	public void sendPasswordResetMail(User user, String contextPath) throws EmailException {
		String token = UUID.randomUUID().toString();
		PasswordResetToken pwResetToken = new PasswordResetToken(user, token);
		passwordResetTokenRepository.save(pwResetToken);
		String url = contextPath + "/resetPassword?id=" + user.getId() + "&token=" + token;
		try {
			final Email email = DefaultEmail.builder()
					.from(new InternetAddress("jenotestemail@gmail.com", "Jeno Test"))
					.to(Lists.newArrayList(new InternetAddress(user.getEmail(), user.getName())))
					.subject("Reset password " + user.getUsername())
					.body("Click the following link to reset your password: " + url)
					.encoding("UTF-8").build();

			emailService.send(email);
		} catch (UnsupportedEncodingException | RuntimeException e) {
			throw new EmailException("Something went wrong", e);
		}
	}
}

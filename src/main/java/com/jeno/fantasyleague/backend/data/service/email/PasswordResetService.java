package com.jeno.fantasyleague.backend.data.service.email;

import com.jeno.fantasyleague.backend.data.repository.PasswordResetTokenRepository;
import com.jeno.fantasyleague.backend.model.PasswordResetToken;
import com.jeno.fantasyleague.backend.model.User;
import com.jeno.fantasyleague.exception.EmailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PasswordResetService {

	@Autowired
	private PasswordResetTokenRepository passwordResetTokenRepository;

	@Autowired
	private ApplicationEmailService emailService;

	public void sendPasswordResetMail(User user, String contextPath) throws EmailException {
		String token = UUID.randomUUID().toString();
		PasswordResetToken pwResetToken = new PasswordResetToken(user, token);
		passwordResetTokenRepository.save(pwResetToken);
		String url = contextPath + "/resetPassword?token=" + token;
		emailService.sendEmail(
				"Reset password " + user.getUsername(),
				"Click the following link to reset your password: " + url,
				user);
	}
}

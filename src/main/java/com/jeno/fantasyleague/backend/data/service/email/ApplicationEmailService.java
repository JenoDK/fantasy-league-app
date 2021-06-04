package com.jeno.fantasyleague.backend.data.service.email;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.jeno.fantasyleague.backend.model.User;
import com.jeno.fantasyleague.exception.EmailException;

@Service
public class ApplicationEmailService {

	private static final Logger LOG = LogManager.getLogger(ApplicationEmailService.class.getName());

	@Autowired
	private JavaMailSender emailSender;

	public void sendEmail(String subject, String body, User user) {
		try {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setFrom("noreply@jenodekeyzer.com");
			message.setSubject(subject);
			message.setTo(user.getEmail());
			message.setSubject(subject);
			message.setText(body);
			emailSender.send(message);

		} catch (Exception e) {
			LOG.error("Something went wrong while sending the mail", e);
			throw new EmailException("Something went wrong", e);
		}
	}

	private String getHtmlBody(String body) {
		return "<!doctype html>\n" +
				"<html>\n" +
					"\t<body>" +
						body +
					"</body>\n" +
				"</html>";
	}

}

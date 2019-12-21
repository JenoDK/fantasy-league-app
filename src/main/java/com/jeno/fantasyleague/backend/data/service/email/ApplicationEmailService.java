package com.jeno.fantasyleague.backend.data.service.email;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.mail.internet.InternetAddress;

import com.google.common.collect.Lists;
import com.jeno.fantasyleague.exception.EmailException;
import com.jeno.fantasyleague.backend.model.User;
import it.ozimov.springboot.mail.model.Email;
import it.ozimov.springboot.mail.model.defaultimpl.DefaultEmail;
import it.ozimov.springboot.mail.service.EmailService;
import it.ozimov.springboot.mail.service.exception.CannotSendEmailException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApplicationEmailService {

	private static final Logger LOG = LogManager.getLogger(ApplicationEmailService.class.getName());

	@Autowired
	private EmailService emailService;

	public void sendEmail(String subject, String body, User user) {
		try {
			final Email email = DefaultEmail.builder()
					.from(new InternetAddress("jenotestemail@gmail.com", "Fantasy League application"))
					.to(Lists.newArrayList(new InternetAddress(user.getEmail(), user.getName())))
					.subject(subject)
					.body("")
					.encoding("UTF-8").build();

			final Map<String, Object> modelObject = new HashMap<>();
			modelObject.put("content", body);

			emailService.send(email, "mailtemplate/htmltemplate.ftl", modelObject);
		} catch (UnsupportedEncodingException | CannotSendEmailException | RuntimeException e) {
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

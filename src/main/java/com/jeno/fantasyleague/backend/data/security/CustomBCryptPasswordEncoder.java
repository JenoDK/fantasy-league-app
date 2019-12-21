package com.jeno.fantasyleague.backend.data.security;

import java.util.regex.Pattern;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomBCryptPasswordEncoder extends BCryptPasswordEncoder {

	private Pattern BCRYPT_PATTERN = Pattern
			.compile("\\A\\$2a?\\$\\d\\d\\$[./0-9A-Za-z]{53}");

	public boolean isBCrypt(String password) {
		return BCRYPT_PATTERN.matcher(password).matches();
	}
}

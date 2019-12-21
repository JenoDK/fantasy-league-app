package com.jeno.fantasyleague.backend.model;

import java.util.Arrays;

public enum RoleName {
	ROLE_USER,
	ROLE_ADMIN;

	public static String[] allRoles() {
		return Arrays.stream(values())
				.map(Enum::toString)
				.toArray(String[]::new);
	}
}

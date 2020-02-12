package com.jeno.fantasyleague.ui.main.views.profile;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

public class UserProfileBean {

	@NotEmpty
	@Length(max = 30)
	private String username;

	public UserProfileBean(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}

package com.jeno.fantasyleague.ui.main.views.profile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class UserProfileBean {

	@NotBlank
	@Size(max = 30)
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

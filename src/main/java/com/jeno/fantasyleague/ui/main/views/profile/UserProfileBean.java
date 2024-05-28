package com.jeno.fantasyleague.ui.main.views.profile;

import com.jeno.fantasyleague.backend.model.User;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import javax.validation.constraints.NotEmpty;

public class UserProfileBean {

	@NotEmpty
	@Size(max = 30)
	private String username;

	@NotEmpty
	@Size(max = 40)
	@Email
	private String email;

	public UserProfileBean(User user) {
		this.username = user.getUsername();
		this.email = user.getEmail();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}

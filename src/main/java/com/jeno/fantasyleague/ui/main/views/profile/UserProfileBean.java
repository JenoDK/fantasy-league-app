package com.jeno.fantasyleague.ui.main.views.profile;

import com.jeno.fantasyleague.backend.model.User;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

public class UserProfileBean {

	@NotEmpty
	@Length(max = 30)
	private String username;

	@NotEmpty
	@Length(max = 40)
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

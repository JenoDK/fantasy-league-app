package com.jeno.fantasyleague.ui.register;


import com.jeno.fantasyleague.backend.model.User;
import javax.validation.constraints.Size;
import javax.validation.constraints.NotEmpty;

public class UserBean extends User {

	@NotEmpty
	@Size(max = 100)
	private String repeatPassword;

	public String getRepeatPassword() {
		return repeatPassword;
	}

	public void setRepeatPassword(String repeatPassword) {
		this.repeatPassword = repeatPassword;
	}

	public User createUserObject() {
		User user = new User();
		user.setName(getName());
		user.setUsername(getUsername());
		user.setEmail(getEmail());
		user.setPassword(getPassword());
		return user;
	}

}

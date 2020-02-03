package com.jeno.fantasyleague.ui.register;


import com.jeno.fantasyleague.backend.model.User;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

public class UserBean extends User {

	@NotEmpty
	@Length(max = 100)
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

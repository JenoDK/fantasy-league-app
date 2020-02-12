package com.jeno.fantasyleague.ui.resetpassword;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

public class ResetPasswordBean {

	@NotEmpty
	@Length(max = 100)
	private String password;

	@NotEmpty
	@Length(max = 100)
	private String repeatPassword;

	public ResetPasswordBean() {
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRepeatPassword() {
		return repeatPassword;
	}

	public void setRepeatPassword(String repeatPassword) {
		this.repeatPassword = repeatPassword;
	}

}

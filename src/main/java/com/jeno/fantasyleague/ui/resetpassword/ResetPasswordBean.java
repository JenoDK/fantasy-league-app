package com.jeno.fantasyleague.ui.resetpassword;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class ResetPasswordBean {

	@NotBlank
	@Size(max = 100)
	private String password;

	@NotBlank
	@Size(max = 100)
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

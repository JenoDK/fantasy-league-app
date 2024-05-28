package com.jeno.fantasyleague.ui.forgotpassword;

import org.hibernate.annotations.NaturalId;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class ForgotPasswordBean {

	@NaturalId
	@NotEmpty
	@Size(max = 40)
	@Email
	private String email;

	public ForgotPasswordBean() {
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}

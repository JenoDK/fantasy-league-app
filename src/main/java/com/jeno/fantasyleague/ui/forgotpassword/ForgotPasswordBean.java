package com.jeno.fantasyleague.ui.forgotpassword;

import org.hibernate.annotations.NaturalId;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

public class ForgotPasswordBean {

	@NaturalId
	@NotEmpty
	@Length(max = 40)
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

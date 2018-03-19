package com.jeno.demo.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Entity
@Table(name = "passwordresettoken")
public class PasswordResetToken {

	private static final int EXPIRATION_IN_MINUTES = 60 * 24;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String token;

	@OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
	@JoinColumn(nullable = false, name = "user_id")
	private User user;

	private Date expiryDate;

	public PasswordResetToken(User user, String token) {
		this.user = user;
		this.token = token;
		this.expiryDate = createExpiryDate();
	}

	private Date createExpiryDate() {
		return Date.from(LocalDateTime.now().plusMinutes(EXPIRATION_IN_MINUTES).atZone(ZoneId.systemDefault()).toInstant());
	}

}

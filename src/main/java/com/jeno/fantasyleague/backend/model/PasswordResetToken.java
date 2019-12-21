package com.jeno.fantasyleague.backend.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
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

	@NotNull
	private boolean used = false;

	public PasswordResetToken() {
	}

	public PasswordResetToken(User user, String token) {
		this.user = user;
		this.token = token;
		this.expiryDate = createExpiryDate();
	}

	private Date createExpiryDate() {
		return Date.from(LocalDateTime.now().plusMinutes(EXPIRATION_IN_MINUTES).atZone(ZoneId.systemDefault()).toInstant());
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public boolean isUsed() {
		return used;
	}

	public void setUsed(boolean used) {
		this.used = used;
	}
}

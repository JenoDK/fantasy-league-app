package com.jeno.fantasyleague.backend.model;

import com.jeno.fantasyleague.backend.model.audit.UserAudit;
import com.jeno.fantasyleague.backend.model.enums.NotificationType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "usernotification")
public class UserNotification extends UserAudit {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "user_id")
	private User user;

	@NotNull
	@Enumerated(EnumType.STRING)
	private NotificationType notification_type;

	@NotNull
	private boolean viewed = false;

	@NotNull
	private String message;

	private Long reference_id;

	private String reference_table;

	public UserNotification() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public NotificationType getNotification_type() {
		return notification_type;
	}

	public void setNotification_type(NotificationType notification_type) {
		this.notification_type = notification_type;
	}

	public boolean isViewed() {
		return viewed;
	}

	public void setViewed(boolean viewed) {
		this.viewed = viewed;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Long getReference_id() {
		return reference_id;
	}

	public void setReference_id(Long reference_id) {
		this.reference_id = reference_id;
	}

	public String getReference_table() {
		return reference_table;
	}

	public void setReference_table(String reference_table) {
		this.reference_table = reference_table;
	}
}

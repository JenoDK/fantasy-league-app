package com.jeno.fantasyleague.backend.model;

import com.jeno.fantasyleague.backend.model.enums.ReminderType;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Records a reminder email that has been sent, so the same reminder is never sent twice
 * (survives restarts and overlapping scheduled runs). Uniqueness is on
 * {@code (user, reminder_type, reference_id)}:
 * <ul>
 *     <li>{@code STOCK_48H} / {@code STOCK_24H} - reference_id is the league id</li>
 *     <li>{@code PREDICTION} - reference_id is the game id (each match is reminded at most once per user)</li>
 * </ul>
 */
@Entity
@Table(name = "reminder_email", uniqueConstraints = {
		@UniqueConstraint(columnNames = {"user_id", "reminder_type", "reference_id"})
})
public class ReminderEmail {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "user_id")
	private User user;

	@ManyToOne(targetEntity = League.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "league_id")
	private League league;

	@Enumerated(EnumType.STRING)
	@Column(name = "reminder_type")
	private ReminderType reminder_type;

	@Column(name = "reference_id")
	private Long reference_id;

	@Column(name = "sent_at")
	private LocalDateTime sent_at;

	public ReminderEmail() {
	}

	public ReminderEmail(User user, League league, ReminderType reminder_type, Long reference_id) {
		this.user = user;
		this.league = league;
		this.reminder_type = reminder_type;
		this.reference_id = reference_id;
		this.sent_at = LocalDateTime.now();
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

	public League getLeague() {
		return league;
	}

	public void setLeague(League league) {
		this.league = league;
	}

	public ReminderType getReminder_type() {
		return reminder_type;
	}

	public void setReminder_type(ReminderType reminder_type) {
		this.reminder_type = reminder_type;
	}

	public Long getReference_id() {
		return reference_id;
	}

	public void setReference_id(Long reference_id) {
		this.reference_id = reference_id;
	}

	public LocalDateTime getSent_at() {
		return sent_at;
	}

	public void setSent_at(LocalDateTime sent_at) {
		this.sent_at = sent_at;
	}
}

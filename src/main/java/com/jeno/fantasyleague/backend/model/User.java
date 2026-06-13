package com.jeno.fantasyleague.backend.model;

import com.google.common.collect.Sets;
import com.jeno.fantasyleague.backend.model.audit.DateAudit;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.NaturalId;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "users", uniqueConstraints = {
		@UniqueConstraint(columnNames = {
				"username"
		}),
		@UniqueConstraint(columnNames = {
				"email"
		})
})
public class User extends DateAudit {

	public enum GraphPreference {
		COLUMN, COLUMN_FLAGS, LINE
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Size(max = 40)
	private String name;

	@NotBlank
	@Size(max = 30)
	private String username;

	@NaturalId(mutable = true)
	@NotBlank
	@Size(max = 40)
	@Email
	private String email;

	@NotBlank
	@Size(max = 100)
	private String password;

	private boolean active = false;

	@Column(name = "reminder_emails_enabled", nullable = false)
	private boolean reminder_emails_enabled = true;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "user_roles",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = Sets.newHashSet();

	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	private Set<UserNotification> notifications;

	// The profile_picture LONGBLOB column stays in the DB but is intentionally not mapped:
	// bytes are served over HTTP by ProfileImageServlet and never loaded with the entity.
	@Formula("(profile_picture is not null)")
	private Boolean hasProfilePicture = Boolean.FALSE;

	@Enumerated(EnumType.STRING)
	private GraphPreference graph_preference = GraphPreference.COLUMN_FLAGS;

	@Size(max = 100)
	@Column(name = "external_auth_id")
	private String externalAuthId;

	public User() {
	}

	public User(String name, String username, String email, String password) {
		this.name = name;
		this.username = username;
		this.email = email;
		this.password = password;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isReminder_emails_enabled() {
		return reminder_emails_enabled;
	}

	public void setReminder_emails_enabled(boolean reminder_emails_enabled) {
		this.reminder_emails_enabled = reminder_emails_enabled;
	}

	public boolean hasProfilePicture() {
		return Boolean.TRUE.equals(hasProfilePicture);
	}

	/** In-memory only — @Formula fields are never written by Hibernate. Used after upload. */
	public void setHasProfilePicture(boolean hasProfilePicture) {
		this.hasProfilePicture = hasProfilePicture;
	}

	public GraphPreference getGraph_preference() {
		return graph_preference;
	}

	public void setGraph_preference(GraphPreference graph_preference) {
		this.graph_preference = graph_preference;
	}

	public Set<UserNotification> getNotifications() {
		return notifications;
	}

	public void setNotifications(Set<UserNotification> notifications) {
		this.notifications = notifications;
	}

	public String getExternalAuthId() {
		return externalAuthId;
	}

	public void setExternalAuthId(String externalAuthId) {
		this.externalAuthId = externalAuthId;
	}
}

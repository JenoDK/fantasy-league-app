package com.jeno.fantasyleague.backend.model;

import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.NaturalId;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.google.common.collect.Sets;
import com.jeno.fantasyleague.backend.model.audit.DateAudit;

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
	@Length(max = 40)
	private String name;

	@NotBlank
	@Length(max = 30)
	private String username;

	@NaturalId(mutable = true)
	@NotBlank
	@Length(max = 40)
	@Email
	private String email;

	@NotBlank
	@Length(max = 100)
	private String password;

	private boolean active = false;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "user_roles",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = Sets.newHashSet();

	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	private Set<UserNotification> notifications;

	@Lob
	@Basic(fetch = FetchType.LAZY)
	private byte[] profile_picture;

	@Enumerated(EnumType.STRING)
	private GraphPreference graph_preference = GraphPreference.COLUMN;

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

	public byte[] getProfile_picture() {
		return profile_picture;
	}

	public void setProfile_picture(byte[] profile_picture) {
		this.profile_picture = profile_picture;
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
}

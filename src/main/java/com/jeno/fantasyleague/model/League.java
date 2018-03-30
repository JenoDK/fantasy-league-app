package com.jeno.fantasyleague.model;

import com.google.common.collect.Sets;
import com.jeno.fantasyleague.model.audit.UserAudit;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
@Table(name = "league")
public class League extends UserAudit {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Size(max = 40)
	private String name;

	private String description;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "league_users",
			joinColumns = @JoinColumn(name = "league_id"),
			inverseJoinColumns = @JoinColumn(name = "user_id"))
	private Set<User> users = Sets.newHashSet();

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "league_owners",
			joinColumns = @JoinColumn(name = "league_id"),
			inverseJoinColumns = @JoinColumn(name = "user_id"))
	private Set<User> owners = Sets.newHashSet();

	public League() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	public Set<User> getOwners() {
		return owners;
	}

	public void setOwners(Set<User> owners) {
		this.owners = owners;
	}
}

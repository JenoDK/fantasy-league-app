package com.jeno.fantasyleague.model;

import com.google.common.collect.Sets;
import com.jeno.fantasyleague.model.audit.UserAudit;
import com.jeno.fantasyleague.model.enums.Template;

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
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "league")
public class League extends UserAudit {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Size(max = 32)
	private String name;

	private String description;

	@Lob
	@Basic(fetch = FetchType.LAZY)
	private byte[] league_picture;

	@NotNull
	@Enumerated(EnumType.STRING)
	private Template template;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "league_users",
			joinColumns = @JoinColumn(name = "league_id"),
			inverseJoinColumns = @JoinColumn(name = "user_id"))
	private Set<User> users = Sets.newHashSet();

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "league_owners",
			joinColumns = @JoinColumn(name = "league_id"),
			inverseJoinColumns = @JoinColumn(name = "user_id"))
	private Set<User> owners = Sets.newHashSet();

	private LocalDateTime league_starting_date;

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

	public byte[] getLeague_picture() {
		return league_picture;
	}

	public void setLeague_picture(byte[] league_picture) {
		this.league_picture = league_picture;
	}

	public Template getTemplate() {
		return template;
	}

	public void setTemplate(Template template) {
		this.template = template;
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

	public LocalDateTime getLeague_starting_date() {
		return league_starting_date;
	}

	public void setLeague_starting_date(LocalDateTime league_starting_date) {
		this.league_starting_date = league_starting_date;
	}
}

package com.jeno.fantasyleague.backend.model;

import com.google.common.collect.Sets;
import com.jeno.fantasyleague.backend.model.audit.UserAudit;
import com.jeno.fantasyleague.backend.model.enums.Template;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "league")
public class League extends UserAudit {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotEmpty
	@Length(max = 255)
	private String name;

	private String description;

	@Lob
	@Basic(fetch = FetchType.LAZY)
	private byte[] league_picture;

	@NotNull
	@Enumerated(EnumType.STRING)
	private Template template;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "league_owners",
			joinColumns = @JoinColumn(name = "league_id"),
			inverseJoinColumns = @JoinColumn(name = "user_id"))
	private Set<User> owners = Sets.newHashSet();

	private LocalDateTime league_starting_date;

	private Boolean active = true;

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

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}
}

package com.jeno.fantasyleague.model;

import com.jeno.fantasyleague.model.audit.UserAudit;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "contestant")
public class Contestant extends UserAudit {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Size(max = 40)
	private String name;

	@Lob
	@Basic(fetch = FetchType.LAZY)
	private byte[] icon;

	@ManyToOne(targetEntity = League.class, fetch = FetchType.EAGER)
	@JoinColumn(nullable = false, name = "league_id")
	private League league;

	private Contestant() {
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

	public byte[] getIcon() {
		return icon;
	}

	public void setIcon(byte[] icon) {
		this.icon = icon;
	}

	public League getLeague() {
		return league;
	}

	public void setLeague(League league) {
		this.league = league;
	}
}

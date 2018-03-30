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
	@Size(max = 32)
	private String name;

	@Lob
	@Basic(fetch = FetchType.LAZY)
	private byte[] icon;

	@Size(max = 128)
	private String icon_path;

	@ManyToOne(targetEntity = League.class, fetch = FetchType.EAGER)
	@JoinColumn(nullable = false, name = "league_id")
	private League league;

	@ManyToOne(targetEntity = ContestantGroup.class, fetch = FetchType.EAGER)
	@JoinColumn(name = "contestantgroup_id")
	private ContestantGroup contestant_group;

	public Contestant() {
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

	public String getIcon_path() {
		return icon_path;
	}

	public void setIcon_path(String icon_path) {
		this.icon_path = icon_path;
	}

	public League getLeague() {
		return league;
	}

	public void setLeague(League league) {
		this.league = league;
	}

	public ContestantGroup getContestant_group() {
		return contestant_group;
	}

	public void setContestant_group(ContestantGroup contestant_group) {
		this.contestant_group = contestant_group;
	}
}

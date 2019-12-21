package com.jeno.fantasyleague.backend.model;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.jeno.fantasyleague.backend.model.audit.UserAudit;

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

	@ManyToOne(targetEntity = League.class, fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "league_id")
	private League league;

	@ManyToOne(targetEntity = ContestantGroup.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "contestantgroup_id")
	private ContestantGroup contestant_group;

	private Integer power_index;

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

	public Integer getPower_index() {
		return power_index;
	}

	public void setPower_index(Integer power_index) {
		this.power_index = power_index;
	}

}
package com.jeno.fantasyleague.backend.model;

import javax.validation.constraints.Size;
import javax.validation.constraints.NotEmpty;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "contestantgroup")
public class ContestantGroup {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotEmpty
	@Size(max = 32)
	private String name;

	@ManyToOne(targetEntity = League.class, fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "league_id")
	private League league;

	@OneToMany(mappedBy = "contestant_group", fetch = FetchType.LAZY)
	private Set<Contestant> contestants;

	public ContestantGroup() {
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

	public League getLeague() {
		return league;
	}

	public void setLeague(League league) {
		this.league = league;
	}

	public Set<Contestant> getContestants() {
		return contestants;
	}
}

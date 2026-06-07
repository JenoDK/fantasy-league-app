package com.jeno.fantasyleague.backend.model;

import com.jeno.fantasyleague.backend.model.audit.DateAudit;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "weekly_winner")
public class WeeklyWinner extends DateAudit {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(targetEntity = League.class, fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "league_id")
	private League league;

	// The member(s) with the most points at the moment of the snapshot. More than one in case of a tie.
	@ManyToMany(targetEntity = User.class, fetch = FetchType.EAGER)
	@JoinTable(
			name = "weekly_winner_winners",
			joinColumns = @JoinColumn(name = "weekly_winner_id"),
			inverseJoinColumns = @JoinColumn(name = "user_id"))
	private Set<User> winners = new HashSet<>();

	@NotNull
	private LocalDateTime announcedAt;

	private double topScore;

	public WeeklyWinner() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public League getLeague() {
		return league;
	}

	public void setLeague(League league) {
		this.league = league;
	}

	public Set<User> getWinners() {
		return winners;
	}

	public void setWinners(Set<User> winners) {
		this.winners = winners;
	}

	public LocalDateTime getAnnouncedAt() {
		return announcedAt;
	}

	public void setAnnouncedAt(LocalDateTime announcedAt) {
		this.announcedAt = announcedAt;
	}

	public double getTopScore() {
		return topScore;
	}

	public void setTopScore(double topScore) {
		this.topScore = topScore;
	}
}

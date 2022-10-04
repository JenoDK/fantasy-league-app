package com.jeno.fantasyleague.backend.data.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jeno.fantasyleague.backend.model.EmailLeagueInvite;
import com.jeno.fantasyleague.backend.model.League;

@Repository
public interface EmailLeagueInviteRepository extends JpaRepository<EmailLeagueInvite, Long> {

	List<EmailLeagueInvite> findByEmail(String email);

	List<EmailLeagueInvite> findByLeague(League league);

}

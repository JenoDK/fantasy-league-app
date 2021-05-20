package com.jeno.fantasyleague.backend.data.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jeno.fantasyleague.backend.model.LeagueUser;
import com.jeno.fantasyleague.backend.model.User;

@Repository
public interface LeagueUserRepository extends JpaRepository<LeagueUser, Long> {

	@Query("SELECT l FROM LeagueUser l WHERE l.pk.user.id = :id")
	List<LeagueUser> findByUser(@Param("id") Long userId);

}

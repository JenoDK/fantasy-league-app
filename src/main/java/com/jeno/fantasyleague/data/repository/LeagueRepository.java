package com.jeno.fantasyleague.data.repository;

import com.jeno.fantasyleague.model.League;
import com.jeno.fantasyleague.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeagueRepository extends JpaRepository<League, Long> {

	List<League> findByUsers(User user);

}

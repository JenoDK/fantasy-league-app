package com.jeno.fantasyleague.data.repository;

import com.jeno.fantasyleague.model.Contestant;
import com.jeno.fantasyleague.model.League;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContestantRepository extends JpaRepository<Contestant, Long> {

	List<Contestant> findByLeague(League league);

}

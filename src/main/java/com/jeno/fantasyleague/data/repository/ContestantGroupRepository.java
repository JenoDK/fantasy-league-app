package com.jeno.fantasyleague.data.repository;

import com.jeno.fantasyleague.model.ContestantGroup;
import com.jeno.fantasyleague.model.League;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContestantGroupRepository extends JpaRepository<ContestantGroup, Long> {

	List<ContestantGroup> findByLeague(League league);

}

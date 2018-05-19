package com.jeno.fantasyleague.data.repository;

import java.util.List;
import java.util.Optional;

import com.jeno.fantasyleague.model.Contestant;
import com.jeno.fantasyleague.model.ContestantGroup;
import com.jeno.fantasyleague.model.League;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ContestantGroupRepository extends JpaRepository<ContestantGroup, Long> {

	List<ContestantGroup> findByLeague(League league);

	@Query("SELECT g.contestants FROM ContestantGroup g WHERE g.id = :id")
	List<Contestant> fetchGroupContestants(@Param("id") Long groupId);

	Optional<ContestantGroup> findByName(String name);
}

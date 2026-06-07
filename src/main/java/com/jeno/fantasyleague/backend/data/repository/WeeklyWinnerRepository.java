package com.jeno.fantasyleague.backend.data.repository;

import com.jeno.fantasyleague.backend.model.League;
import com.jeno.fantasyleague.backend.model.WeeklyWinner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WeeklyWinnerRepository extends JpaRepository<WeeklyWinner, Long> {

	Optional<WeeklyWinner> findTopByLeagueOrderByIdDesc(League league);

}

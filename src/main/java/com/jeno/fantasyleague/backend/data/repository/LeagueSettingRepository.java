package com.jeno.fantasyleague.backend.data.repository;

import com.jeno.fantasyleague.backend.model.League;
import com.jeno.fantasyleague.backend.model.LeagueSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LeagueSettingRepository extends JpaRepository<LeagueSetting, Long> {

	List<LeagueSetting> findByLeague(League league);

	Optional<LeagueSetting> findByLeagueAndName(League league, String name);

}

package com.jeno.fantasyleague.data.repository;

import java.util.List;
import java.util.Optional;

import com.jeno.fantasyleague.model.League;
import com.jeno.fantasyleague.model.LeagueSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeagueSettingRepository extends JpaRepository<LeagueSetting, Long> {

	List<LeagueSetting> findByLeague(League league);

	Optional<LeagueSetting> findByLeagueAndName(League league, String name);

}

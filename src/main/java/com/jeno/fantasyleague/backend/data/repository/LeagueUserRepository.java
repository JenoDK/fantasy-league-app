package com.jeno.fantasyleague.backend.data.repository;

import com.jeno.fantasyleague.backend.model.League;
import com.jeno.fantasyleague.backend.model.LeagueUser;
import com.jeno.fantasyleague.backend.model.User;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeagueUserRepository extends JpaRepository<LeagueUser, Long> {

	@Cacheable("findByUsers")
	List<LeagueUser> findByUser(User user);

	@Cacheable("findByLeague")
	List<LeagueUser> findByLeague(League league);

	@Override
	@CacheEvict(value = {"findByUsers", "findByLeague"})
	<S extends LeagueUser> S save(S leagueUser);

}

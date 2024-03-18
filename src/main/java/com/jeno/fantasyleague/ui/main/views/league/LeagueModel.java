package com.jeno.fantasyleague.ui.main.views.league;

import com.jeno.fantasyleague.backend.data.repository.LeagueRepository;
import com.jeno.fantasyleague.backend.data.repository.LeagueUserRepository;
import com.jeno.fantasyleague.backend.data.service.repo.league.LeagueService;
import com.jeno.fantasyleague.backend.model.League;
import com.jeno.fantasyleague.backend.model.LeagueUser;
import com.jeno.fantasyleague.backend.model.User;
import com.jeno.fantasyleague.ui.main.views.league.gridlayout.LeagueBean;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@SpringComponent
public class LeagueModel {

	@Autowired
	private LeagueRepository leagueRepo;
	@Autowired
	private LeagueUserRepository leagueUserRepository;
	@Autowired
	private LeagueService leagueService;

	public LeagueBean addLeague(League league, User user) {
		// Add as owner
		league.getOwners().add(user);
		return makeLeagueBean(leagueService.addLeague(league, user), user);
	}

	public List<LeagueBean> loadLeaguesForUser(User user) {
		return leagueUserRepository.findByUser(user).stream()
				.map(LeagueUser::getLeague)
				.filter(League::getActive)
				.sorted(Comparator.comparing(League::getLeague_starting_date).reversed())
				.map(league -> makeLeagueBean(league, user))
				.collect(Collectors.toList());
	}

	@Transactional
	public LeagueBean makeLeagueBean(League league, User user) {
		return new LeagueBean.Builder()
				.setLeague(league)
				.setLoggedInUser(user)
				.setLeagueUsers(leagueUserRepository.findByLeague(league))
				.setLeagueOwners(leagueRepo.fetchLeagueOwners(league.getId()))
				.createLeagueBean();
	}
}

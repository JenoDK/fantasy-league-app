package com.jeno.fantasyleague.ui.main.views.league;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.jeno.fantasyleague.backend.data.repository.LeagueRepository;
import com.jeno.fantasyleague.backend.data.service.repo.league.LeagueService;
import com.jeno.fantasyleague.backend.model.League;
import com.jeno.fantasyleague.backend.model.User;
import com.jeno.fantasyleague.security.SecurityHolder;
import com.jeno.fantasyleague.ui.main.views.league.gridlayout.LeagueBean;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.overview.UserTotalScoreBean;
import com.vaadin.flow.spring.annotation.SpringComponent;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

@SpringComponent
public class LeagueModel {

	@Autowired
	private LeagueRepository leagueRepo;
	@Autowired
	private LeagueService leagueService;
	@Autowired
	private SecurityHolder securityHolder;

	private final BehaviorSubject<List<LeagueBean>> leaguesForUser = BehaviorSubject.create();
	private final BehaviorSubject<LeagueBean> newLeague = BehaviorSubject.create();

	public void addLeague(League league) {
		// Add current logged in user as owner and user
		User user = securityHolder.getUser();
		// Add as owner
		league.getOwners().add(user);
		League addedLeague = leagueService.addLeague(league, user);
		this.newLeague.onNext(makeLeagueBean(addedLeague, user));
	}

	public Observable<LeagueBean> newLeague() {
		return newLeague;
	}

	public Observable<List<LeagueBean>> leaguesForUser() {
		return leaguesForUser;
	}

	public void loadLeaguesForUser() {
		User user = securityHolder.getUser();
		leaguesForUser.onNext(leagueRepo.findByUsers(user).stream()
				.sorted(Comparator.comparing(League::getLeague_starting_date).reversed())
				.map(league -> makeLeagueBean(league, user))
				.collect(Collectors.toList()));
	}

	@Transactional
	public LeagueBean makeLeagueBean(League league, User user) {
//		List<UserTotalScoreBean> userScores = UserTotalScoreBean.transfer(leagueService.getTotalLeagueScores(league));
		List<UserTotalScoreBean> userScores =  List.of();
		return new LeagueBean.Builder()
				.setLeague(league)
				.setScores(userScores)
				.setLoggedInUser(user)
				.setLeagueUsers(leagueRepo.fetchLeagueUsers(league.getId()))
				.setLeagueOwners(leagueRepo.fetchLeagueOwners(league.getId()))
				.createLeagueBean();
	}
}

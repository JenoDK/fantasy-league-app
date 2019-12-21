package com.jeno.fantasyleague.ui.main.views.league;

import java.util.List;

import com.jeno.fantasyleague.backend.data.repository.LeagueRepository;
import com.jeno.fantasyleague.security.SecurityHolder;
import com.jeno.fantasyleague.backend.data.service.repo.league.LeagueService;
import com.jeno.fantasyleague.backend.model.League;
import com.jeno.fantasyleague.backend.model.User;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@UIScope
public class LeagueModel {

	@Autowired
	private LeagueRepository leagueRepo;
	@Autowired
	private LeagueService leagueService;
	@Autowired
	private SecurityHolder securityHolder;

	private final BehaviorSubject<List<League>> leaguesForUser = BehaviorSubject.create();
	private final BehaviorSubject<League> newLeague = BehaviorSubject.create();

	public void addLeague(League league) {
		// Add current logged in user as owner and user
		User user = securityHolder.getUser();
		// Add as owner
		league.getOwners().add(user);
		this.newLeague.onNext(leagueService.addLeague(league, user));
	}

	public Observable<League> newLeague() {
		return newLeague;
	}

	public Observable<List<League>> leaguesForUser() {
		return leaguesForUser;
	}

	public void loadLeaguesForUser() {
		leaguesForUser.onNext(leagueRepo.findByUsers(securityHolder.getUser()));
	}
}

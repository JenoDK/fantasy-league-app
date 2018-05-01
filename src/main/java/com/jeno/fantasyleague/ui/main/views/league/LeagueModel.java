package com.jeno.fantasyleague.ui.main.views.league;

import com.jeno.fantasyleague.annotation.SpringUIScope;
import com.jeno.fantasyleague.data.repository.LeagueRepository;
import com.jeno.fantasyleague.data.security.SecurityHolder;
import com.jeno.fantasyleague.data.service.leaguetemplates.LeagueTemplateService;
import com.jeno.fantasyleague.data.service.repo.contestantweight.ContestantWeightService;
import com.jeno.fantasyleague.model.League;
import com.jeno.fantasyleague.model.User;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@SpringUIScope
public class LeagueModel {

	@Autowired
	private LeagueRepository leagueRepo;
	@Autowired
	private SecurityHolder securityHolder;
	@Autowired
	private ContestantWeightService contestantWeightService;

	@Autowired
	private BeanFactory beanFactory;

	private final BehaviorSubject<List<League>> leaguesForUser = BehaviorSubject.create();
	private final BehaviorSubject<League> newLeague = BehaviorSubject.create();

	public void addLeague(League league) {
		LeagueTemplateService templateServiceBean = beanFactory.getBean(league.getTemplate().getTemplateServiceBeanName(), LeagueTemplateService.class);

		// Add current logged in user as owner and user
		User user = securityHolder.getUser();
		league.getOwners().add(user);
		league.getUsers().add(user);
		League newLeague = leagueRepo.save(league);

		templateServiceBean.run(newLeague, user);
		contestantWeightService.addDefaultContestantWeights(newLeague, user);

		// Fetch again in order to get the possibly new games/contestants/...
		this.newLeague.onNext(leagueRepo.findById(newLeague.getId()).get());
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

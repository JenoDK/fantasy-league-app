package com.jeno.fantasyleague.ui.main.views.league;

import com.jeno.fantasyleague.annotation.SpringUIScope;
import com.jeno.fantasyleague.data.repository.LeagueRepository;
import com.jeno.fantasyleague.data.security.SecurityHolder;
import com.jeno.fantasyleague.data.service.leaguetemplates.LeagueTemplateService;
import com.jeno.fantasyleague.model.League;
import com.jeno.fantasyleague.model.User;
import com.jeno.fantasyleague.ui.main.views.league.gridlayout.LeagueBean;
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
	private BeanFactory beanFactory;

	private final BehaviorSubject<List<League>> leaguesForUser = BehaviorSubject.create();
	private final BehaviorSubject<League> newLeague = BehaviorSubject.create();

	public void addLeague(LeagueBean leagueBean) {
		LeagueTemplateService templateServiceBean = beanFactory.getBean(leagueBean.getTemplate().getTemplateServiceBeanName(), LeagueTemplateService.class);

		League league = leagueBean.createLeagueObject();
		// Add current logged in user as owner and user
		User user = securityHolder.getUser();
		league.getOwners().add(user);
		league.getUsers().add(user);
		League newLeague = leagueRepo.save(league);

		templateServiceBean.run(newLeague);

		this.newLeague.onNext(newLeague);
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

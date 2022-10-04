package com.jeno.fantasyleague.ui.main.views.league;

import org.springframework.beans.factory.annotation.Autowired;

import com.jeno.fantasyleague.backend.model.User;
import com.jeno.fantasyleague.security.SecurityHolder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

@SpringComponent
@UIScope
public class LeaguePresenter {

	@Autowired
	private LeagueView view;
	@Autowired
	private LeagueModel model;
	@Autowired
	private SecurityHolder securityHolder;

	public void setupModule(LeagueModule module) {
		module.removeAll();
		module.add(view.getLayout());
		User user = securityHolder.getUser();
		view.newLeague().subscribe(league -> view.addLeague(model.addLeague(league, user)));
		view.leagueAccepted().subscribe(ignored -> view.setLeagues(model.loadLeaguesForUser(user)));
		view.setLeagues(model.loadLeaguesForUser(user));
	}

	public void showMainLeagueScreen() {
		// I want to keep the state in the league view
		// view.showLeagueGridLayout();
	}
}

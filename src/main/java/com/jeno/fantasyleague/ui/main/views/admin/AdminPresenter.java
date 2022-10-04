package com.jeno.fantasyleague.ui.main.views.admin;

import org.springframework.beans.factory.annotation.Autowired;

import com.jeno.fantasyleague.backend.model.User;
import com.jeno.fantasyleague.security.SecurityHolder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

@SpringComponent
@UIScope
public class AdminPresenter {

	@Autowired
	private AdminView view;
	@Autowired
	private AdminModel model;
	@Autowired
	private SecurityHolder securityHolder;

	public void setupModule(AdminModule module) {
		User user = securityHolder.getUser();
		module.removeAll();
		module.add(view.getLayout(model.getSingleLeagueForLoggedInUser(user)));
		view.scoreChanged().subscribe(model::updateGameScoresGlobally);
//		view.newLeague().subscribe(league -> view.addLeague(model.addLeague(league, user)));
//		view.leagueAccepted().subscribe(ignored -> view.setLeagues(model.loadLeaguesForUser(user)));
	}

}

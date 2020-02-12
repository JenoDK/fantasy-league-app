package com.jeno.fantasyleague.ui.main.views.league;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

@SpringComponent
@UIScope
public class LeaguePresenter {

	@Autowired
	private LeagueView view;
	@Autowired
	private LeagueModel model;

	public void setupModule(LeagueModule module) {
		module.removeAll();
		module.add(view.getLayout());

		view.newLeague().subscribe(model::addLeague);

		model.newLeague().subscribe(view::addLeague);
		model.leaguesForUser().subscribe(view::setLeagues);

		model.loadLeaguesForUser();
	}

}

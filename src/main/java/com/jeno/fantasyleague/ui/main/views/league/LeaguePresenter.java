package com.jeno.fantasyleague.ui.main.views.league;

import com.jeno.fantasyleague.annotation.SpringUIScope;
import org.springframework.beans.factory.annotation.Autowired;

@SpringUIScope
public class LeaguePresenter {

	@Autowired
	private LeagueView view;
	@Autowired
	private LeagueModel model;

	public void setupModule(LeagueModule module) {
		module.removeAllComponents();
		module.addComponent(view.getLayout());

		view.newLeague().subscribe(model::addLeague);

		model.newLeague().subscribe(view::addLeague);
		model.leaguesForUser().subscribe(view::setLeagues);

		model.loadLeaguesForUser();
	}

}

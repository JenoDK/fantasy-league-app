package com.jeno.fantasyleague.ui.main.views.league;

import javax.annotation.PostConstruct;

import com.jeno.fantasyleague.ui.main.views.state.State;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = State.StateUrlConstants.LEAGUE)
public class LeagueModule extends VerticalLayout {

	@Autowired
	private LeaguePresenter presenter;

	@PostConstruct
	public void init() {
		presenter.setupModule(this);
	}

}

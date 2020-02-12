package com.jeno.fantasyleague.ui.main.views.league;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.jeno.fantasyleague.ui.main.MainView;
import com.jeno.fantasyleague.ui.main.views.state.State;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

@Tag("league-view")
@Route(value = State.StateUrlConstants.LEAGUE, layout = MainView.class)
@RouteAlias(value = State.StateUrlConstants.ROOT, layout = MainView.class)
public class LeagueModule extends VerticalLayout {

	@Autowired
	private LeaguePresenter presenter;

	@PostConstruct
	public void init() {
		presenter.setupModule(this);
	}

}

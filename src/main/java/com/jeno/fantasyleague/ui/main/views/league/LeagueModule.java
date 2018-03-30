package com.jeno.fantasyleague.ui.main.views.league;

import com.jeno.fantasyleague.annotation.SpringUIScope;
import com.jeno.fantasyleague.ui.main.views.state.State;
import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

@SpringUIScope
@SpringView(name = State.StateUrlConstants.LEAGUE)
public class LeagueModule extends VerticalLayout implements View {

	@Autowired
	private LeaguePresenter presenter;

	@PostConstruct
	public void init() {
		presenter.setupModule(this);
	}

}

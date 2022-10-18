package com.jeno.fantasyleague.ui.main.views.league;

import org.springframework.beans.factory.annotation.Autowired;

import com.jeno.fantasyleague.ui.main.MainView;
import com.jeno.fantasyleague.ui.main.views.state.State;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.NavigationTrigger;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.router.RouterLayout;

@Tag("league-view")
@Route(value = State.StateUrlConstants.LEAGUE, layout = MainView.class)
@RouteAlias(value = State.StateUrlConstants.ROOT, layout = MainView.class)
public class LeagueModule extends VerticalLayout implements BeforeEnterObserver, RouterLayout {

	private LeaguePresenter presenter;

	public LeagueModule(@Autowired LeaguePresenter presenter) {
		this.presenter = presenter;
		presenter.setupModule(this);
	}

	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		if (NavigationTrigger.ROUTER_LINK.equals(event.getTrigger())) {
			presenter.showMainLeagueScreen();
		}
	}
}

package com.jeno.fantasyleague.ui.main.views.league.gridlayout;

import com.jeno.fantasyleague.backend.model.League;
import com.vaadin.flow.component.html.Label;
import io.reactivex.Observable;

public class NewLeagueGridComponent extends AbstractLeagueGridComponent {

	private LeagueForm leagueForm;

	public NewLeagueGridComponent() {
		super();
		createNewLeagueComponent();
	}

	private void createNewLeagueComponent() {
		Label newLeague = new Label("New League");

		leagueForm = new LeagueForm();

		add(newLeague);
		add(leagueForm);
//		setExpandRatio(newLeague, 0.2f);
//		setExpandRatio(leagueForm, 0.8f);
	}

	public Observable<League> newLeague() {
		return leagueForm.validSubmit();
	}

	public void reset() {
		leagueForm.reset();
	};

}

package com.jeno.fantasyleague.ui.main.views.league.gridlayout;

import com.jeno.fantasyleague.model.League;
import com.jeno.fantasyleague.util.RxUtil;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Label;
import io.reactivex.Observable;

public class ExistingLeagueGridComponent extends AbstractLeagueGridComponent {

	private final League league;

	public ExistingLeagueGridComponent(League league) {
		super();
		this.league = league;

		createLeagueComponent(league);
	}

	private void createLeagueComponent(League league) {
		addStyleName("existing");
		Label name = new Label(league.getName(), ContentMode.HTML);
		Label description = new Label(league.getDescription(), ContentMode.HTML);
		addComponents(name, description);
	}

	public Observable<League> click() {
		return RxUtil.clicks(this).map(ignored -> league);
	}

}

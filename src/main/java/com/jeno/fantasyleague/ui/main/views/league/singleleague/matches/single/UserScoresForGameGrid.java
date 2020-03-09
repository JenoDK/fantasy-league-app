package com.jeno.fantasyleague.ui.main.views.league.singleleague.matches.single;

import java.util.List;

import com.jeno.fantasyleague.backend.model.User;
import com.jeno.fantasyleague.ui.common.grid.CustomGrid;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.matches.MatchBean;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.data.renderer.ComponentRenderer;

import io.reactivex.subjects.BehaviorSubject;

public class UserScoresForGameGrid extends CustomGrid<UserScoresForGameBean> {

	private final User loggedInUser;
	private final MatchBean match;

	public UserScoresForGameGrid(
			MatchBean match,
			List<UserScoresForGameBean> items,
			User loggedInUser) {
		super();
		this.match = match;
		this.loggedInUser = loggedInUser;
		initColumns();

		setItems(items);
	}

	private void initColumns() {
		setSelectionMode(SelectionMode.NONE);
		setHeightByRows(true);

		addColumn(new ComponentRenderer<>(bean -> new UserScoreForGameCard(bean, match.getHomeTeam(), match.getAwayTeam(), BehaviorSubject.create())));
		addThemeNames("card-grid", "no-row-borders");
		removeThemeVariants(GridVariant.LUMO_ROW_STRIPES);
		getStyle().set("border", "none");
	}

}

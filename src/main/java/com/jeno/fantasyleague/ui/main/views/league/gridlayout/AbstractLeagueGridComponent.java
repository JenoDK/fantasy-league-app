package com.jeno.fantasyleague.ui.main.views.league.gridlayout;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public abstract class AbstractLeagueGridComponent extends VerticalLayout {

	public AbstractLeagueGridComponent() {
		super();
		initLayout();
	}

	private void initLayout() {
		setSpacing(false);
		setWidth("100%");
		addClassName("league-grid-component");
	}

}

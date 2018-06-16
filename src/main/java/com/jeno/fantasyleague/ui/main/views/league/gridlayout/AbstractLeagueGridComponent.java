package com.jeno.fantasyleague.ui.main.views.league.gridlayout;

import com.vaadin.ui.VerticalLayout;

public abstract class AbstractLeagueGridComponent extends VerticalLayout {

	public AbstractLeagueGridComponent() {
		super();
		initLayout();
	}

	private void initLayout() {
		setSpacing(false);
		setWidth("100%");
		addStyleName("league-grid-component");
	}

}

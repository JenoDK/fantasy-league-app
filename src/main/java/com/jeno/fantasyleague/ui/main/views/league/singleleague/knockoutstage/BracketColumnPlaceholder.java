package com.jeno.fantasyleague.ui.main.views.league.singleleague.knockoutstage;

import com.vaadin.ui.VerticalLayout;

public class BracketColumnPlaceholder extends VerticalLayout {

	public BracketColumnPlaceholder(String style, boolean starterLine) {
		super();
		setMargin(false);
		setSpacing(false);
		setWidth(50f, Unit.PIXELS);
		if (starterLine) {
			setHeight(80f, Unit.PERCENTAGE);
		} else {
			setHeight(100f, Unit.PERCENTAGE);
		}

		addStyleName("bracketplaceholder-" + style);
	}
}

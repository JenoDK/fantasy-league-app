package com.jeno.fantasyleague.ui.main.views.league.singleleague.knockoutstage;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class BracketColumnPlaceholder extends VerticalLayout {

	public BracketColumnPlaceholder(String style, boolean starterLine) {
		super();
		setMargin(false);
		setSpacing(false);
		setWidth("50px");
		if (starterLine) {
			setHeight("80%");
		} else {
			setHeight("100%");
		}

		addClassName("bracketplaceholder-" + style);
	}
}

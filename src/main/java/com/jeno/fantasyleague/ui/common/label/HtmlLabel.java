package com.jeno.fantasyleague.ui.common.label;

import com.vaadin.flow.component.html.Label;

public class HtmlLabel extends Label {
	public HtmlLabel(String text) {
		super();
		getElement().setProperty("innerHTML", text);
	}
}

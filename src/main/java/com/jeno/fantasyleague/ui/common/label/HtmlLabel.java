package com.jeno.fantasyleague.ui.common.label;

import com.vaadin.flow.component.html.Label;

public class HtmlLabel extends Label {

	public HtmlLabel() {
		super();
	}

	public HtmlLabel(String text) {
		super();
		setText(text);
	}

	@Override
	public void setText(String text) {
		getElement().setProperty("innerHTML", text);
	}
}

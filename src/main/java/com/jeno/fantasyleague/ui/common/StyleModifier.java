package com.jeno.fantasyleague.ui.common;

import com.vaadin.flow.dom.Style;

@FunctionalInterface
public interface StyleModifier {

	void modify(Style style);

}

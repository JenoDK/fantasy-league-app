package com.jeno.fantasyleague.ui.common.tabsheet;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public abstract class LazyTabComponent extends VerticalLayout {

	protected void hide() {
		if (isVisible()) {
			setVisible(false);
		}
	}

	protected void show() {
		setVisible(true);
	}

	public interface ComponentCreationFunction {
		LazyTabComponent createComponent();
	}

}

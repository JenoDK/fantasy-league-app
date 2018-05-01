package com.jeno.fantasyleague.util;

import com.jeno.fantasyleague.model.Contestant;
import com.vaadin.server.Sizeable;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;

public class GridUtil {

	private GridUtil() {
	}

	public static HorizontalLayout createTeamLayout(Contestant contestant) {
		HorizontalLayout layout = new HorizontalLayout();
		Image icon = new Image();
		icon.setWidth(42f, Sizeable.Unit.PIXELS);
		icon.setHeight(28f, Sizeable.Unit.PIXELS);
		icon.setSource(new ThemeResource(contestant.getIcon_path()));
		Label teamName = new Label(contestant.getName());
		layout.addComponents(icon, teamName);
		return layout;
	}
}

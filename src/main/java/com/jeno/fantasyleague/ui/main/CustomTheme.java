package com.jeno.fantasyleague.ui.main;

import com.vaadin.addon.charts.model.style.SolidColor;
import com.vaadin.addon.charts.themes.VaadinTheme;

public class CustomTheme extends VaadinTheme {

	public CustomTheme() {
		setColors(COLOR1, COLOR2, COLOR3, COLOR4, COLOR5, COLOR6, COLOR7,
				COLOR8,
				new SolidColor("#058DC7"), new SolidColor("#50B432"),
				new SolidColor("#ED561B"), new SolidColor("#DDDF00"),
				new SolidColor("#24CBE5"), new SolidColor("#64E572"),
				new SolidColor("#FF9655"), new SolidColor("#FFF263"),
				new SolidColor("#6AF9C4"));
	}
}

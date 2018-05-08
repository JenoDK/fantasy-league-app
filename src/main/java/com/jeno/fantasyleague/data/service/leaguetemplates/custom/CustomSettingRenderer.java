package com.jeno.fantasyleague.data.service.leaguetemplates.custom;

import com.jeno.fantasyleague.data.service.leaguetemplates.LeagueSettingRenderer;
import com.jeno.fantasyleague.model.League;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

@org.springframework.stereotype.Component
public class CustomSettingRenderer implements LeagueSettingRenderer {

	@Override
	public Component render(League league) {
		return new VerticalLayout();
	}

}

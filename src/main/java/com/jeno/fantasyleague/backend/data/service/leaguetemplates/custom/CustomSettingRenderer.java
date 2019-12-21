package com.jeno.fantasyleague.backend.data.service.leaguetemplates.custom;

import com.jeno.fantasyleague.backend.data.service.leaguetemplates.LeagueSettingRenderer;
import com.jeno.fantasyleague.backend.model.League;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

@org.springframework.stereotype.Component
public class CustomSettingRenderer implements LeagueSettingRenderer {

	@Override
	public Component render(League league) {
		return new VerticalLayout();
	}

}

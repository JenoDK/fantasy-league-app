package com.jeno.fantasyleague.ui.main.views.league.gridlayout;

import com.jeno.fantasyleague.model.League;
import com.jeno.fantasyleague.model.leaguetemplates.Template;

import javax.validation.constraints.NotNull;

public class LeagueBean extends League {

	@NotNull
	private Template template;

	public Template getTemplate() {
		return template;
	}

	public void setTemplate(Template template) {
		this.template = template;
	}

	public League createLeagueObject() {
		League league = new League();
		league.setName(getName());
		return league;
	}

}

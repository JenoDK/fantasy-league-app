package com.jeno.fantasyleague.ui.main.views.league.singleleague.matches;

import com.vaadin.flow.templatemodel.TemplateModel;

public interface MatchBindingModel extends TemplateModel {
	void setMatch(MatchCardBean match);
	MatchCardBean getMatch();
}

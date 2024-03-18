package com.jeno.fantasyleague.ui.main.views.league.singleleague.faq;

import com.jeno.fantasyleague.resources.Resources;
import com.vaadin.flow.component.grid.Grid;

import java.util.List;

public class LeagueSettingsGameScoresGrid extends Grid<LeagueSettingsGameScoreBean> {

	public LeagueSettingsGameScoresGrid(List<LeagueSettingsGameScoreBean> items) {
		super();
		initColumns();
		setItems(items);
		setWidth("100%");
	}

	private void initColumns() {
		addColumn(LeagueSettingsGameScoreBean::getStage)
				.setAutoWidth(true)
				.setHeader(Resources.getMessage("stage"))
				.setId("stageColumn");
		addColumn(LeagueSettingsGameScoreBean::getAllCorrect)
				.setAutoWidth(true)
				.setHeader(Resources.getMessage("AllCorrect"))
				.setId("allCorrectColumn");
		addColumn(LeagueSettingsGameScoreBean::getCorrectResultAndWrongScore)
				.setAutoWidth(true)
				.setHeader(Resources.getMessage("WrongScore"))
				.setId("wrongScoreColumn");
		addColumn(LeagueSettingsGameScoreBean::getAllWrong)
				.setAutoWidth(true)
				.setHeader(Resources.getMessage("AllWrong"))
				.setId("allWrongColumn");
	}
}

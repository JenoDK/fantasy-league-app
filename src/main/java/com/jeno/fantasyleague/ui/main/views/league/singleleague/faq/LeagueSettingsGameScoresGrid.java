package com.jeno.fantasyleague.ui.main.views.league.singleleague.faq;

import java.util.List;

import com.jeno.fantasyleague.resources.Resources;
import com.jeno.fantasyleague.ui.common.grid.CustomGrid;

public class LeagueSettingsGameScoresGrid extends CustomGrid<LeagueSettingsGameScoreBean> {

	public LeagueSettingsGameScoresGrid(List<LeagueSettingsGameScoreBean> items) {
		super();

		initColumns();
		setItems(items);
		setWidth("100%");
	}

	private void initColumns() {
		addColumn(LeagueSettingsGameScoreBean::getStage)
				.setHeader(Resources.getMessage("stage"))
				.setId("stageColumn");
		addColumn(LeagueSettingsGameScoreBean::getAllCorrect)
				.setHeader(Resources.getMessage("AllCorrect"))
				.setId("allCorrectColumn");
		addColumn(LeagueSettingsGameScoreBean::getCorrectResultAndWrongScore)
				.setHeader(Resources.getMessage("WrongScore"))
				.setId("wrongScoreColumn");
		addColumn(LeagueSettingsGameScoreBean::getAllWrong)
				.setHeader(Resources.getMessage("AllWrong"))
				.setId("allWrongColumn");
	}
}

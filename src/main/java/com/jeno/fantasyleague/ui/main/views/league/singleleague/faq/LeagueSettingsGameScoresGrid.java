package com.jeno.fantasyleague.ui.main.views.league.singleleague.faq;

import java.util.List;

import com.jeno.fantasyleague.resources.Resources;
import com.jeno.fantasyleague.ui.common.grid.CustomGrid;

public class LeagueSettingsGameScoresGrid extends CustomGrid<LeagueSettingsGameScoreBean> {

	public LeagueSettingsGameScoresGrid(List<LeagueSettingsGameScoreBean> items) {
		super();

		initColumns();
		setItems(items);
		setWidth(100, Unit.PERCENTAGE);
	}

	private void initColumns() {
		addColumn(LeagueSettingsGameScoreBean::getStage)
				.setCaption(Resources.getMessage("stage"))
				.setId("stageColumn");
		addColumn(LeagueSettingsGameScoreBean::getAllCorrect)
				.setCaption(Resources.getMessage("AllCorrect"))
				.setId("allCorrectColumn");
		addColumn(LeagueSettingsGameScoreBean::getCorrectResultAndWrongScore)
				.setCaption(Resources.getMessage("WrongScore"))
				.setId("wrongScoreColumn");
		addColumn(LeagueSettingsGameScoreBean::getAllWrong)
				.setCaption(Resources.getMessage("AllWrong"))
				.setId("allWrongColumn");
	}
}

package com.jeno.fantasyleague.ui.main.views.league.singleleague.groupstage.standings;

import com.jeno.fantasyleague.resources.Resources;
import com.jeno.fantasyleague.ui.common.grid.CustomGrid;
import com.jeno.fantasyleague.util.GridUtil;
import com.vaadin.data.provider.GridSortOrderBuilder;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.renderers.ComponentRenderer;

public class GroupStandingsGrid extends CustomGrid<GroupTeamBean> {

	public GroupStandingsGrid() {
		super();
		initGrid();
	}

	private void initGrid() {
		Column<GroupTeamBean, HorizontalLayout> teamColumn =
			addColumn(teamBean -> GridUtil.createTeamLayout(teamBean.getContestant()), new ComponentRenderer())
				.setCaption(Resources.getMessage("team"))
				.setId("team");
		Column<GroupTeamBean, Integer> pointsColumn =
			addColumn(teamBean -> teamBean.getPointsInGroup())
				.setCaption(Resources.getMessage("points"))
				.setId("points");
		Column<GroupTeamBean, Integer> totalGoalsColumn =
			addColumn(teamBean -> teamBean.getGoalsInGroup())
				.setCaption(Resources.getMessage("totalGoals"))
				.setId("goals");
		setSortOrder(new GridSortOrderBuilder().thenDesc(pointsColumn));
	}
}

package com.jeno.fantasyleague.ui.main.views.league.singleleague.groups;

import com.google.common.collect.Lists;
import com.jeno.fantasyleague.resources.Resources;
import com.jeno.fantasyleague.ui.common.grid.CustomGrid;
import com.jeno.fantasyleague.util.LayoutUtil;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.data.renderer.ComponentRenderer;

public class GroupStandingsGrid extends CustomGrid<GroupTeamBean> {

	public GroupStandingsGrid() {
		super();

		initGrid();
	}

	private void initGrid() {
		addColumn(new ComponentRenderer<>(teamBean -> LayoutUtil.createTeamLayout(teamBean.getContestant())))
				.setAutoWidth(true)
				.setHeader(Resources.getMessage("team"))
				.setId("team");
		Column<GroupTeamBean> pointsColumn = addColumn(GroupTeamBean::getPointsInGroup);
		pointsColumn
				.setAutoWidth(true)
				.setHeader(Resources.getMessage("points"))
				.setId("points");
		addColumn(GroupTeamBean::getGoalsInGroup)
				.setAutoWidth(true)
				.setHeader(Resources.getMessage("totalGoals"))
				.setId("totalGoals");
		Column<GroupTeamBean> predictedPointsColumn = addColumn(GroupTeamBean::getPredictedPointsInGroup);
		predictedPointsColumn
				.setAutoWidth(true)
				.setHeader("Predicted points")
				.setId("predictedPoints");
		addColumn(GroupTeamBean::getPredictedGoalsInGroup)
				.setAutoWidth(true)
				.setHeader("Predicted goals")
				.setId("predictedGoals");
		sort(Lists.newArrayList(new GridSortOrder<>(pointsColumn, SortDirection.DESCENDING), new GridSortOrder<>(predictedPointsColumn, SortDirection.DESCENDING)));
	}
}

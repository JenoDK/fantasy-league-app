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
				.setId("stageColumn");
		Column<GroupTeamBean> pointsColumn = addColumn(GroupTeamBean::getPointsInGroup);
		pointsColumn
				.setAutoWidth(true)
				.setHeader(Resources.getMessage("points"))
				.setId("stageColumn");
		addColumn(GroupTeamBean::getGoalsInGroup)
				.setAutoWidth(true)
				.setHeader(Resources.getMessage("totalGoals"))
				.setId("stageColumn");
		sort(Lists.newArrayList(new GridSortOrder<>(pointsColumn, SortDirection.DESCENDING)));
	}
}

package com.jeno.fantasyleague.ui.main.views.league.singleleague.groupstage.standings;

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
		Column<GroupTeamBean> teamColumn = addColumn(new ComponentRenderer<>(teamBean -> LayoutUtil.createTeamLayout(teamBean.getContestant())))
				.setHeader(Resources.getMessage("team"));
		Column<GroupTeamBean> pointsColumn = addColumn(GroupTeamBean::getPointsInGroup)
				.setHeader(Resources.getMessage("points"));
		Column<GroupTeamBean> totalGoalsColumn = addColumn(GroupTeamBean::getGoalsInGroup)
				.setHeader(Resources.getMessage("totalGoals"));
		pointsColumn.setId("points");
		teamColumn.setId("team");
		totalGoalsColumn.setId("goals");
		sort(Lists.newArrayList(new GridSortOrder<>(pointsColumn, SortDirection.DESCENDING)));
	}
}

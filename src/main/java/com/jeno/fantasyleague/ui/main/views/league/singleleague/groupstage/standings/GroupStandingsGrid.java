package com.jeno.fantasyleague.ui.main.views.league.singleleague.groupstage.standings;

import com.jeno.fantasyleague.ui.common.grid.CustomGrid;
import com.jeno.fantasyleague.util.GridUtil;
import com.vaadin.data.provider.GridSortOrderBuilder;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.renderers.ComponentRenderer;

public class GroupStandingsGrid extends CustomGrid<GroupTeamBean> {

	public GroupStandingsGrid() {
		super();
		initGrid();
	}

	private void initGrid() {
		Column<GroupTeamBean, HorizontalLayout> teamColumn =
			addColumn(teamBean -> GridUtil.createTeamLayout(teamBean.getContestant()), new ComponentRenderer())
				.setCaption("Team")
				.setId("team");
		Column<GroupTeamBean, Integer> pointsColumn =
			addColumn(teamBean -> teamBean.getContestant().getPoints_in_group())
				.setCaption("Points")
				.setId("points");
		setSortOrder(new GridSortOrderBuilder().thenDesc(pointsColumn).thenDesc(teamColumn));
	}
}

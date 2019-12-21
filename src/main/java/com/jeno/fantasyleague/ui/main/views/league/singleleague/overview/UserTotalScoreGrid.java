package com.jeno.fantasyleague.ui.main.views.league.singleleague.overview;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import com.google.common.collect.Lists;
import com.jeno.fantasyleague.backend.data.service.leaguetemplates.worldcup2018.FifaWorldCup2018Stages;
import com.jeno.fantasyleague.backend.model.User;
import com.jeno.fantasyleague.resources.Resources;
import com.jeno.fantasyleague.ui.common.grid.CustomGrid;
import com.jeno.fantasyleague.ui.common.grid.CustomGridBuilder;
import com.jeno.fantasyleague.util.ImageUtil;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.data.provider.SortDirection;

public class UserTotalScoreGrid extends CustomGrid<UserTotalScoreBean> {

	private final User loggedInUser;

	public UserTotalScoreGrid(List<UserTotalScoreBean> items, User loggedInUser) {
		super();
		this.loggedInUser = loggedInUser;
		initColumns();

		setItems(items);
	}

	private void initColumns() {
		addColumn(bean -> bean.getPosition())
				.setWidth("60px");
		addIconColumn(
				new CustomGridBuilder.ColumnProvider<>(
						"userIcon",
						userTotalScoreBean -> new CustomGridBuilder.IconColumnValue(ImageUtil.getUserProfilePictureResource((User) userTotalScoreBean.getUser())),
						""));
		addColumn(userTotalScoreBean -> getUserInfoColumn(userTotalScoreBean))
				.setHeader(Resources.getMessage("username"))
				.setId("userName");
		Arrays.stream(FifaWorldCup2018Stages.values())
				.sorted(Comparator.comparingInt(FifaWorldCup2018Stages::getSeq))
				.forEach(stage -> {
					addColumn(bean -> OverviewUtil.getScoreFormatted(bean.getScore(stage)))
							.setHeader(Resources.getMessage(stage.getName()))
							.setId(stage.getName());
				});
		Column<UserTotalScoreBean> userScoreColumn =
				addColumn(bean -> OverviewUtil.getScoreFormatted(bean.getTotalScore()))
						.setHeader(Resources.getMessage("totalScore"));
		userScoreColumn.setId("totalScore");
		sort(Lists.newArrayList(new GridSortOrder<>(userScoreColumn, SortDirection.DESCENDING)));
	}

	private String getUserInfoColumn(UserTotalScoreBean userTotalScoreBean) {
		return userTotalScoreBean.getUser().getUsername() +
				(userTotalScoreBean.getUser().getId().equals(loggedInUser.getId()) ? " (You)" : "") +
				" - " + userTotalScoreBean.getUser().getName();
	}

}

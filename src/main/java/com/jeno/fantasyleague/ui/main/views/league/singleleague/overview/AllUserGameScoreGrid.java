package com.jeno.fantasyleague.ui.main.views.league.singleleague.overview;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import com.jeno.fantasyleague.model.User;
import com.jeno.fantasyleague.resources.Resources;
import com.jeno.fantasyleague.ui.common.grid.CustomGrid;
import com.jeno.fantasyleague.ui.common.grid.CustomGridBuilder;
import com.jeno.fantasyleague.util.ImageUtil;
import com.vaadin.data.provider.GridSortOrderBuilder;

public class AllUserGameScoreGrid extends CustomGrid<UserPredictionForGameBean> {

	private final User loggedInUser;

	public AllUserGameScoreGrid(List<UserPredictionForGameBean> items, User loggedInUser) {
		super();
		this.loggedInUser = loggedInUser;
		initColumns();

		setItems(items);
	}

	private void initColumns() {
		addIconColumn(
				new CustomGridBuilder.ColumnProvider<>(
						"userIcon",
						bean -> new CustomGridBuilder.IconColumnValue(ImageUtil.getUserProfilePictureResource(bean.getUser())),
						""));
		addColumn(bean -> bean.getUser().getUsername() + (bean.getUser().getId().equals(loggedInUser.getId()) ? " (You)" : ""))
				.setCaption(Resources.getMessage("username"))
				.setId("userName");
		addColumn(bean -> OverviewUtil.getPredictionColumn(
						bean.getHomePrediction(),
						bean.getAwayPrediction(),
						bean.getHomeTeamWon(),
						bean.isPredictionIsHiddenForUser(),
						bean.getPredictionHiddenUntil()))
				.setCaption("Prediction")
				.setStyleGenerator(item -> {
					String baseStyle = "v-align-center";
					if (item.isPredictionIsHiddenForUser() ||
							(Objects.isNull(item.getHomePrediction()) || Objects.isNull(item.getAwayPrediction()))) {
						baseStyle = baseStyle + " grid-cell-tiny-text";
					}
					return baseStyle;
				});
		Column<UserPredictionForGameBean, BigDecimal> userScoreColumn =
				addColumn(bean -> OverviewUtil.getScoreFormatted(bean.getScore()))
						.setCaption(Resources.getMessage("totalScore"))
						.setId("totalScore");
		setSortOrder(new GridSortOrderBuilder().thenDesc(userScoreColumn));
	}

}

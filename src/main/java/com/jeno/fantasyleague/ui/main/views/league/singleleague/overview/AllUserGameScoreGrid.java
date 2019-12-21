package com.jeno.fantasyleague.ui.main.views.league.singleleague.overview;

import java.util.List;
import java.util.Objects;

import com.google.common.collect.Lists;
import com.jeno.fantasyleague.backend.model.Contestant;
import com.jeno.fantasyleague.backend.model.User;
import com.jeno.fantasyleague.resources.Resources;
import com.jeno.fantasyleague.ui.common.grid.CustomGrid;
import com.jeno.fantasyleague.ui.common.grid.CustomGridBuilder;
import com.jeno.fantasyleague.util.ImageUtil;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.provider.SortDirection;

public class AllUserGameScoreGrid extends CustomGrid<UserPredictionForGameBean> {

	private final User loggedInUser;
	private final Contestant home_team;
	private final Contestant away_team;

	public AllUserGameScoreGrid(
			Contestant home_team,
			Contestant away_team,
			List<UserPredictionForGameBean> items,
			User loggedInUser) {
		super();
		this.loggedInUser = loggedInUser;
		this.home_team = home_team;
		this.away_team = away_team;
		initColumns();

		setItems(items);
	}

	private void initColumns() {
		addIconColumn(
				new CustomGridBuilder.ColumnProvider<>(
						"userIcon",
						bean -> new CustomGridBuilder.IconColumnValue(ImageUtil.getUserProfilePictureResource((User) bean.getUser())),
						""));
		addColumn(bean -> bean.getUser().getUsername() + (bean.getUser().getId().equals(loggedInUser.getId()) ? " (You)" : ""))
				.setHeader(Resources.getMessage("username"))
				.setId("userName");
		addColumn(bean -> OverviewUtil.getPredictionColumn(
						bean.getHomePrediction(),
						bean.getAwayPrediction(),
						bean.getHomeTeamWon(),
						bean.isPredictionIsHiddenForUser(),
						bean.getPredictionHiddenUntil()))
				.setHeader("Prediction")
				.setClassNameGenerator(item -> {
					String baseStyle = "v-align-center";
					if (item.isPredictionIsHiddenForUser() ||
							(Objects.isNull(item.getHomePrediction()) || Objects.isNull(item.getAwayPrediction()))) {
						baseStyle = baseStyle + " grid-cell-tiny-text";
					}
					return baseStyle;
				});
		Column<UserPredictionForGameBean> userScoreColumn =
				addColumn(bean -> OverviewUtil.getScoreFormatted(bean.getScore()))
						.setHeader(Resources.getMessage("totalScore"));
		userScoreColumn.setId("totalScore");

		if (home_team != null && away_team != null) {
			Column<UserPredictionForGameBean> homeTeamWeightColumn = addColumn(bean -> bean.getHomeTeamWeight());
			homeTeamWeightColumn.setId("homeTeamWeightColumn");
			Column<UserPredictionForGameBean> awayTeamWeightColumn = addColumn(bean -> bean.getAwayTeamWeight());
			awayTeamWeightColumn.setId("awayTeamWeightColumn");
			getHeaderRows().get(0).getCell(homeTeamWeightColumn)
					.setComponent(createStocksForTeamHeader(home_team));
			getHeaderRows().get(0).getCell(awayTeamWeightColumn)
					.setComponent(createStocksForTeamHeader(away_team));
		}
		sort(Lists.newArrayList(new GridSortOrder<>(userScoreColumn, SortDirection.DESCENDING)));
	}

	private HorizontalLayout createStocksForTeamHeader(Contestant contestant) {
		HorizontalLayout layout = new HorizontalLayout();
		Image icon = new Image();
		icon.setWidth("42px");
		icon.setHeight("28px");
		icon.setSrc(contestant.getIcon_path());
		Label teamName = new Label(" Stocks");
		layout.add(icon, teamName);
		return layout;
	}

}

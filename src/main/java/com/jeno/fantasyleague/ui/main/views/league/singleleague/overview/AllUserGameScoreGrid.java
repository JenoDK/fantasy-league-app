package com.jeno.fantasyleague.ui.main.views.league.singleleague.overview;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import com.jeno.fantasyleague.model.Contestant;
import com.jeno.fantasyleague.model.User;
import com.jeno.fantasyleague.resources.Resources;
import com.jeno.fantasyleague.ui.common.grid.CustomGrid;
import com.jeno.fantasyleague.ui.common.grid.CustomGridBuilder;
import com.jeno.fantasyleague.util.ImageUtil;
import com.vaadin.data.provider.GridSortOrderBuilder;
import com.vaadin.server.Sizeable;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;

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

		if (home_team != null && away_team != null) {
			addColumn(bean -> bean.getHomeTeamWeight())
					.setId("homeTeamWeightColumn");
			addColumn(bean -> bean.getAwayTeamWeight())
					.setId("awayTeamWeightColumn");
			getHeader().getDefaultRow().getCell("homeTeamWeightColumn")
					.setComponent(createStocksForTeamHeader(home_team));
			getHeader().getDefaultRow().getCell("awayTeamWeightColumn")
					.setComponent(createStocksForTeamHeader(away_team));
		}
		setSortOrder(new GridSortOrderBuilder().thenDesc(userScoreColumn));
	}

	private HorizontalLayout createStocksForTeamHeader(Contestant contestant) {
		HorizontalLayout layout = new HorizontalLayout();
		Image icon = new Image();
		icon.setWidth(42f, Sizeable.Unit.PIXELS);
		icon.setHeight(28f, Sizeable.Unit.PIXELS);
		icon.setSource(new ThemeResource(contestant.getIcon_path()));
		Label teamName = new Label(" Stocks");
		layout.addComponents(icon, teamName);
		return layout;
	}

}

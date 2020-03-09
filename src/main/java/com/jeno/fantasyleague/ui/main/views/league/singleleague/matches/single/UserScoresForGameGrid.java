package com.jeno.fantasyleague.ui.main.views.league.singleleague.matches.single;

import java.util.List;

import com.jeno.fantasyleague.backend.model.Contestant;
import com.jeno.fantasyleague.backend.model.User;
import com.jeno.fantasyleague.ui.common.grid.CustomGrid;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.matches.MatchBean;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;

import io.reactivex.subjects.BehaviorSubject;

public class UserScoresForGameGrid extends CustomGrid<UserScoresForGameBean> {

	private final User loggedInUser;
	private final MatchBean match;

	public UserScoresForGameGrid(
			MatchBean match,
			List<UserScoresForGameBean> items,
			User loggedInUser) {
		super();
		this.match = match;
		this.loggedInUser = loggedInUser;
		initColumns();

		setItems(items);
	}

	private void initColumns() {
		setSelectionMode(SelectionMode.NONE);
		setHeightByRows(true);

		addColumn(new ComponentRenderer<>(bean -> new UserScoreForGameCard(bean, match.getHomeTeam(), match.getAwayTeam(), BehaviorSubject.create())));
		addThemeNames("card-grid", "no-row-borders");
		removeThemeVariants(GridVariant.LUMO_ROW_STRIPES);
		getStyle().set("border", "none");
//		addIconColumn(
//				new CustomGridBuilder.ColumnProvider<>(
//						"userIcon",
//						bean -> LayoutUtil.getUserIconColumnValue(bean.getUser()),
//						""))
//				.setWidth("70px");
//		addColumn(bean -> bean.getUser().getUsername() + (bean.getUser().getId().equals(loggedInUser.getId()) ? " (You)" : ""))
//				.setHeader(Resources.getMessage("username"))
//				.setId("userName");
//		addColumn(bean -> OverviewUtil.getPredictionColumn(
//						bean.getHomePrediction(),
//						bean.getAwayPrediction(),
//						bean.getHomeTeamWon(),
//						bean.isPredictionIsHiddenForUser(),
//						bean.getPredictionHiddenUntil()))
//				.setHeader("Prediction");
//		Column<UserScoresForGameBean> userScoreColumn =
//				addColumn(bean -> OverviewUtil.getScoreFormatted(bean.getScore()))
//						.setHeader(Resources.getMessage("totalScore"));
//		userScoreColumn.setId("totalScore");
//
//		if (home_team != null && away_team != null) {
//			Column<UserScoresForGameBean> homeTeamWeightColumn = addColumn(bean -> bean.getHomeTeamWeight());
//			homeTeamWeightColumn.setId("homeTeamWeightColumn");
//			Column<UserScoresForGameBean> awayTeamWeightColumn = addColumn(bean -> bean.getAwayTeamWeight());
//			awayTeamWeightColumn.setId("awayTeamWeightColumn");
//			getHeaderRows().get(0).getCell(homeTeamWeightColumn)
//					.setComponent(createStocksForTeamHeader(home_team));
//			getHeaderRows().get(0).getCell(awayTeamWeightColumn)
//					.setComponent(createStocksForTeamHeader(away_team));
//		}
//		sort(Lists.newArrayList(new GridSortOrder<>(userScoreColumn, SortDirection.DESCENDING)));
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

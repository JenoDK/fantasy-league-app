package com.jeno.fantasyleague.ui.main.views.league.singleleague.overview.usertotalscore;

import java.util.List;

import com.jeno.fantasyleague.backend.model.User;
import com.jeno.fantasyleague.ui.common.grid.CustomGrid;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.overview.chart.UserScoreBean;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.data.renderer.ComponentRenderer;

import io.reactivex.subjects.BehaviorSubject;

public class UserTotalScoreGrid extends CustomGrid<UserScoreBean> {

	private final User loggedInUser;
	private final boolean minimalistic;

	public UserTotalScoreGrid(List<UserScoreBean> items, boolean minimalistic, User loggedInUser) {
		super();
		this.loggedInUser = loggedInUser;
		this.minimalistic = minimalistic;
		initColumns();

		setItems(items);
	}

	private void initColumns() {
		setSelectionMode(SelectionMode.NONE);
		setHeightByRows(true);

		addColumn(new ComponentRenderer<>(bean -> new UserTotalScoreCard(bean, BehaviorSubject.create())));
		addThemeNames("card-grid", "no-row-borders");
		removeThemeVariants(GridVariant.LUMO_ROW_STRIPES);
		getStyle().set("border", "none");

//		addColumn(UserTotalScoreBean::getPosition)
//				.setWidth("60px");
//		addIconColumn(
//				new CustomGridBuilder.ColumnProvider<>(
//						"userIcon",
//						userTotalScoreBean -> LayoutUtil.getUserIconColumnValue(userTotalScoreBean.getUser()),
//						""));
//		addColumn(userTotalScoreBean -> getUserInfoColumn(userTotalScoreBean))
//				.setHeader(Resources.getMessage("username"))
//				.setId("userName");
//		if (!minimalistic) {
//			Arrays.stream(SoccerCupStages.values())
//					.sorted(Comparator.comparingInt(SoccerCupStages::getSeq))
//					.forEach(stage -> {
//						addColumn(bean -> OverviewUtil.getScoreFormatted(bean.getScore(stage)))
//								.setHeader(Resources.getMessage(stage.getName()))
//								.setId(stage.getName());
//					});
//		}
//		Column<UserTotalScoreBean> userScoreColumn =
//				addColumn(bean -> OverviewUtil.getScoreFormatted(bean.getTotalScore()))
//						.setHeader(Resources.getMessage("totalScore"));
//		userScoreColumn.setId("totalScore");
//		sort(Lists.newArrayList(new GridSortOrder<>(userScoreColumn, SortDirection.DESCENDING)));
	}

	private String getUserInfoColumn(UserScoreBean userScoreBean) {
		return userScoreBean.getUser().getUsername() +
				(userScoreBean.getUser().getId().equals(loggedInUser.getId()) ? " (You)" : "") +
				" - " + userScoreBean.getUser().getName();
	}

}

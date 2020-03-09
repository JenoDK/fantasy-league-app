package com.jeno.fantasyleague.ui.main.views.league.gridlayout;

import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import com.jeno.fantasyleague.backend.model.User;
import com.jeno.fantasyleague.resources.Resources;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.overview.OverviewUtil;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.overview.usertotalscore.UserTotalScoreBean;
import com.jeno.fantasyleague.util.ImageUtil;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.templatemodel.TemplateModel;

import io.reactivex.subjects.BehaviorSubject;

@Tag("league-card")
@JsModule("./src/views/league/league-card.js")
public class LeagueCard extends PolymerTemplate<TemplateModel> {

	private final LeagueBean league;
	private final BehaviorSubject<LeagueBean> clickedLeague;

	@Id("content")
	private Div content;

	@Id("wrapper")
	private VerticalLayout wrapper;

	@Id("leagueImage_div")
	private Div imageDiv;

	@Id("leagueName")
	private H2 name;

	@Id("adminName")
	private Span adminName;

	@Id("membersCount")
	private Span membersCount;

	public LeagueCard(LeagueBean league, BehaviorSubject<LeagueBean> clickedLeague) {
		this.league = league;
		this.clickedLeague = clickedLeague;

		initLayout();
	}

	private void initLayout() {
		wrapper.addClickListener(ignored -> clickedLeague.onNext(league));
		name.setText(league.getLeague().getName());
		adminName.setText("Owners: " + league.getLeagueOwners().stream().map(User::getUsername).collect(Collectors.joining(", ")));
		membersCount.setText("Members: " + league.getLeagueUsers().size());
		imageDiv.add(ImageUtil.getLeaguePictureImage(league.getLeague()));
		wrapper.setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER, imageDiv);
//		wrapper.add(new TopThreeGrid(league.getScores().stream().limit(3).collect(Collectors.toList()), league.getLoggedInUser()));
	}

	private class TopThreeGrid extends Grid<UserTotalScoreBean> {

		private final User loggedInUser;

		public TopThreeGrid(List<UserTotalScoreBean>items, User loggedInUser) {
			super();
			this.loggedInUser = loggedInUser;

			initGrid();
			setItems(items);
		}

		private void initGrid() {
			setHeightByRows(true);
			setSelectionMode(SelectionMode.NONE);
			addThemeNames("orders", "no-row-borders");
			addClassName("top-3-grid");

			addColumn(UserTotalScoreBean::getPosition)
					.setWidth("60px");
			addColumn(userTotalScoreBean -> getUserInfoColumn(userTotalScoreBean))
					.setHeader(Resources.getMessage("username"))
					.setId("userName");
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
}

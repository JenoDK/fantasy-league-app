package com.jeno.fantasyleague.ui.main.views.league.singleleague.overview;

import static com.jeno.fantasyleague.ui.main.views.league.singleleague.overview.chart.ScoreChart.DEFAULT_TOP_SHOWN;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.Maps;
import com.jeno.fantasyleague.backend.model.Contestant;
import com.jeno.fantasyleague.backend.model.League;
import com.jeno.fantasyleague.backend.model.User;
import com.jeno.fantasyleague.ui.common.tabsheet.LazyTabComponent;
import com.jeno.fantasyleague.ui.main.views.league.SingleLeagueServiceProvider;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.LeagueMenuBar;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.overview.chart.ScoreChart;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.overview.chart.UserScoreBean;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.overview.usertotalscore.UserTotalScoreGrid;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.NumberField;

public class OverviewTab extends LazyTabComponent {

	private final SingleLeagueServiceProvider singleLeagueServiceprovider;
	private final League league;
	private final LeagueMenuBar menuBar;

	private List<MenuItem> extraMenuItems;
	private User loggedInUser;
	private User.GraphPreference graphPreference;

	public OverviewTab(League league, SingleLeagueServiceProvider singleLeagueServiceprovider, LeagueMenuBar menuBar) {
		super();
		addClassName("overview-tab");
		setMargin(false);
		setPadding(false);
		setSpacing(false);

		User loggedInUser = singleLeagueServiceprovider.getLoggedInUser();

		this.singleLeagueServiceprovider = singleLeagueServiceprovider;
		this.league = league;
		this.menuBar = menuBar;
		this.loggedInUser = loggedInUser;
		this.graphPreference = loggedInUser.getGraph_preference();

		List<UserScoreBean> scoreBeans = fetchTotalScores();
		UserTotalScoreGrid totalScoreGrid = new UserTotalScoreGrid(scoreBeans, false, loggedInUser);
		totalScoreGrid.setWidth("100%");
		Set<String> iconPaths = singleLeagueServiceprovider.getContestantRepository().findAll().stream()
				.map(Contestant::getIcon_path)
				.filter(Objects::nonNull)
				.collect(Collectors.toSet());
		ScoreChart scoreChart = new ScoreChart(fetchTotalScores(), generateColorMap(iconPaths, COLORS), loggedInUser);
		NumberField numberField = new NumberField();
		numberField.setValue(DEFAULT_TOP_SHOWN);
		numberField.setMax(scoreBeans.size());
		numberField.setMin(1);
		numberField.addValueChangeListener(event -> {
			switch (graphPreference) {
				case COLUMN:
					scoreChart.showHBarChart(event.getValue());
					break;
				case COLUMN_FLAGS:
					scoreChart.showVBarChart(event.getValue());
					break;
				case LINE:
					scoreChart.showLineChart(event.getValue());
					break;
			}
		});
		numberField.setHasControls(true);

		add(numberField);
		add(scoreChart);
		add(totalScoreGrid);

		MenuItem hBarChartMenuItem = this.menuBar.addItem(VaadinIcon.BAR_CHART_H.create(), event -> {
			setGraphPreference(User.GraphPreference.COLUMN);
			scoreChart.showHBarChart(numberField.getValue());
		});
		MenuItem vBarChartMenuItem = this.menuBar.addItem(VaadinIcon.BAR_CHART.create(), event -> {
			setGraphPreference(User.GraphPreference.COLUMN_FLAGS);
			scoreChart.showVBarChart(numberField.getValue());
		});
		MenuItem lineChartMenuItem = this.menuBar.addItem(VaadinIcon.LINE_CHART.create(), event -> {
			setGraphPreference(User.GraphPreference.LINE);
			scoreChart.showLineChart(numberField.getValue());
		});
		extraMenuItems = List.of(hBarChartMenuItem, vBarChartMenuItem, lineChartMenuItem);
	}

	private void setGraphPreference(User.GraphPreference graphPref) {
		graphPreference = graphPref;
		if (!loggedInUser.getGraph_preference().equals(graphPreference)) {
			loggedInUser.setGraph_preference(graphPreference);
			loggedInUser = singleLeagueServiceprovider.getUserDao().update(loggedInUser);
		}
	}

	@Override
	protected void hide() {
		super.hide();
		extraMenuItems.stream()
				.filter(Objects::nonNull)
				.forEach(i -> i.setVisible(false));
	}

	@Override
	protected void show() {
		super.show();
		extraMenuItems.stream()
				.filter(Objects::nonNull)
				.forEach(i -> i.setVisible(true));
	}

	private static String[] COLORS = new String[] {
			"#FF0000", "#00FF00", "#0000FF", "#FFFF00", "#FF00FF", "#00FFFF", "#000000",
			"#800000", "#008000", "#000080", "#808000", "#800080", "#008080", "#808080",
			"#C00000", "#00C000", "#0000C0", "#C0C000", "#C000C0", "#00C0C0", "#C0C0C0",
			"#400000", "#004000", "#000040", "#404000", "#400040", "#004040", "#404040",
			"#200000", "#002000", "#000020", "#202000", "#200020", "#002020", "#202020",
			"#600000", "#006000", "#000060", "#606000", "#600060", "#006060", "#606060",
			"#A00000", "#00A000", "#0000A0", "#A0A000", "#A000A0", "#00A0A0", "#A0A0A0",
			"#E00000", "#00E000", "#0000E0", "#E0E000", "#E000E0", "#00E0E0", "#E0E0E0",
	};

	public static Map<String, String> generateColorMap(Set<String> iconPaths, String[] colors) {
		int i = 0;
		Map<String, String> colorMap = Maps.newHashMap();
		for (String iconPath : iconPaths) {
			colorMap.put(iconPath, colors[i]);
			i++;
		}
		return colorMap;
	}

	public List<UserScoreBean> fetchTotalScores() {
		return UserScoreBean.transfer(singleLeagueServiceprovider.getUserLeagueScores(league));
	}

	public enum ChartType {
		HBAR, VBAR, LINE
	}

}

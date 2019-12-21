package com.jeno.fantasyleague.ui.main.views.league.singleleague.overview.charts;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.jeno.fantasyleague.backend.data.service.leaguetemplates.worldcup2018.FifaWorldCup2018Stages;
import com.jeno.fantasyleague.backend.model.User;
import com.jeno.fantasyleague.resources.Resources;
import com.jeno.fantasyleague.ui.common.charts.AbstractChart;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.overview.OverviewUtil;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.overview.UserTotalScoreBean;
import com.vaadin.flow.component.charts.model.AxisTitle;
import com.vaadin.flow.component.charts.model.ChartType;
import com.vaadin.flow.component.charts.model.DataLabels;
import com.vaadin.flow.component.charts.model.ListSeries;
import com.vaadin.flow.component.charts.model.PlotOptionsColumn;
import com.vaadin.flow.component.charts.model.Series;
import com.vaadin.flow.component.charts.model.StackLabels;
import com.vaadin.flow.component.charts.model.Stacking;
import com.vaadin.flow.component.charts.model.Title;
import com.vaadin.flow.component.charts.model.Tooltip;
import com.vaadin.flow.component.charts.model.XAxis;
import com.vaadin.flow.component.charts.model.YAxis;

public class TotalUserScoresChart extends AbstractChart {

	private List<UserTotalScoreBean> scoreBeans;
	private User loggedInUser;
	private List<String> userNames = Lists.newArrayList();

	public TotalUserScoresChart(List<UserTotalScoreBean> scoreBeans, User loggedInUser) {
		super(ChartType.COLUMN);
		this.scoreBeans = scoreBeans;
		this.loggedInUser = loggedInUser;

		initChartConfig();
	}

	private void initChartConfig() {
		conf.setTitle(new Title("Scores per phase"));

		XAxis xAxis = new XAxis();
		conf.addxAxis(xAxis);

		YAxis yAxis = new YAxis();
		yAxis.setMin(0);
		yAxis.setTitle(new AxisTitle("Points"));
		StackLabels sLabels = new StackLabels(true);
		yAxis.setStackLabels(sLabels);
		conf.addyAxis(yAxis);

		Tooltip tooltip = new Tooltip();
		tooltip.setFormatter("function() { return '<b>'+ this.x +'</b><br/>"
				+ "'+this.series.name +': '+ this.y +'<br/>'+'Total: '+ this.point.stackTotal;}");
		conf.setTooltip(tooltip);

		PlotOptionsColumn plotOptions = new PlotOptionsColumn();
		plotOptions.setStacking(Stacking.NORMAL);
		DataLabels labels = new DataLabels(true);
		// TODO
//		Style style= new Style();
//		style.setTextShadow("0 0 3px black");
//		labels.setStyle(style);
//		labels.setColor(new SolidColor("white"));
		plotOptions.setDataLabels(labels);
		conf.setPlotOptions(plotOptions);

		initData();
	}

	public void refresh(List<UserTotalScoreBean> beans) {
		this.scoreBeans = beans;
		initData();
	}

	private void initData() {
		userNames = scoreBeans.stream()
				.sorted(Comparator.comparingDouble(UserTotalScoreBean::getTotalScore).reversed())
				.map(UserTotalScoreBean::getUser)
				.map(User::getUsername)
				.collect(Collectors.toList());
		userNames.forEach(name -> seriesShownPerId.put(name, true));
		setData();
	}

	@Override
	protected void setData() {
		List<String> namesToShow = userNames.stream()
				.filter(this::needsShowing)
				.collect(Collectors.toList());
		conf.getxAxis().setCategories(namesToShow.stream().toArray(String[]::new));

		ArrayListMultimap<FifaWorldCup2018Stages, Number> perStageUserScoresMap = ArrayListMultimap.create();
		scoreBeans.stream()
				.filter(bean -> namesToShow.contains(bean.getUser().getUsername()))
				.sorted(Comparator.comparingDouble(UserTotalScoreBean::getTotalScore).reversed())
				.forEach(bean -> {
					Arrays.stream(FifaWorldCup2018Stages.values())
							.forEach(stage -> perStageUserScoresMap.put(stage, OverviewUtil.getScoreFormatted(bean.getScore(stage))));
				});
		List<Series> series = perStageUserScoresMap.keySet().stream()
				.sorted(Comparator.comparingInt(FifaWorldCup2018Stages::getSeq).reversed())
				.map(stage -> {
					Number[] numberArray = perStageUserScoresMap.get(stage).stream()
							.toArray(Number[]::new);
					return new ListSeries(Resources.getMessage(stage.getName()), numberArray);
				})
				.collect(Collectors.toList());
		conf.setSeries(series);

		// TODO check
		drawChart(true);
	}
}

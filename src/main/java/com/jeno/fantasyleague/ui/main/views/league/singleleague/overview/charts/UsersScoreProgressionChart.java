package com.jeno.fantasyleague.ui.main.views.league.singleleague.overview.charts;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.collect.Maps;
import com.jeno.fantasyleague.model.User;
import com.jeno.fantasyleague.ui.common.charts.AbstractChart;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.overview.OverviewUtil;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.overview.UserTotalScoreBean;
import com.vaadin.addon.charts.model.AxisTitle;
import com.vaadin.addon.charts.model.AxisType;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.DataSeries;
import com.vaadin.addon.charts.model.DataSeriesItem;
import com.vaadin.addon.charts.model.Hover;
import com.vaadin.addon.charts.model.Marker;
import com.vaadin.addon.charts.model.MarkerSymbolEnum;
import com.vaadin.addon.charts.model.PlotOptionsArea;
import com.vaadin.addon.charts.model.Series;
import com.vaadin.addon.charts.model.States;
import com.vaadin.addon.charts.model.Title;
import com.vaadin.addon.charts.model.XAxis;
import com.vaadin.addon.charts.model.YAxis;

public class UsersScoreProgressionChart extends AbstractChart {

	private final User loggedInUser;
	private List<UserTotalScoreBean> beans;
	private Map<String, Series> seriesPerId = Maps.newHashMap();

	public UsersScoreProgressionChart(
			List<UserTotalScoreBean> beans,
			User loggedInUser) {
		super(ChartType.AREA);
		this.beans = beans;
		this.loggedInUser = loggedInUser;

		initChartConfig();
	}

	private void initChartConfig() {
		conf = getConfiguration();
		conf.setTitle(new Title("User scores"));

		PlotOptionsArea plotOptions = new PlotOptionsArea();
		Marker marker = new Marker();
		marker.setEnabled(false);
		marker.setSymbol(MarkerSymbolEnum.CIRCLE);
		marker.setRadius(2);
		States states = new States();
		states.setHover(new Hover(true));
		marker.setStates(states);
		plotOptions.setMarker(marker);
		conf.setPlotOptions(plotOptions);

		XAxis xAxis = new XAxis();
		xAxis.setType(AxisType.DATETIME);
		xAxis.getDateTimeLabelFormats().setDay("%e %b %Y");
		conf.addxAxis(xAxis);

		YAxis yAxis = new YAxis();
		yAxis.setMin(0);
		yAxis.setTitle(new AxisTitle("Points"));
		conf.addyAxis(yAxis);

		initData();
	}

	public void refresh(List<UserTotalScoreBean> beans) {
		this.beans = beans;
		initData();
	}

	private void initData() {
		beans.stream()
				.forEach(bean -> {
					String username = bean.getUser().getUsername();
					DataSeries serie = new DataSeries(username);
					Map<LocalDateTime, Double> scoresPerDate = bean.getScoresPerDate();
					List<DataSeriesItem> items = scoresPerDate.keySet().stream()
							.filter(localDateTime -> LocalDateTime.now().isAfter(localDateTime))
							.sorted(LocalDateTime::compareTo)
							.map(localDateTime -> new DataSeriesItem(
									localDateTime.atZone(ZoneId.systemDefault()).toInstant(),
									OverviewUtil.getScoreFormatted(scoresPerDate.get(localDateTime))))
							.collect(Collectors.toList());
					serie.setData(items);
					seriesPerId.put(username, serie);
					seriesShownPerId.put(username, loggedInUser.getId().equals(bean.getUser().getId()));
				});
		setData();
	}

	@Override
	protected void setData() {
		conf.setSeries(seriesPerId.values().stream()
				.filter(serie -> seriesShownPerId.get(serie.getName()))
				.collect(Collectors.toList()));
		drawChart(conf);
	}

}

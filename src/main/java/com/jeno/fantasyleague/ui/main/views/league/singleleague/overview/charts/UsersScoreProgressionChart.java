package com.jeno.fantasyleague.ui.main.views.league.singleleague.overview.charts;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.collect.Maps;
import com.jeno.fantasyleague.backend.model.User;
import com.jeno.fantasyleague.ui.common.charts.AbstractChart;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.overview.OverviewUtil;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.overview.UserTotalScoreBean;
import com.vaadin.flow.component.charts.model.AxisTitle;
import com.vaadin.flow.component.charts.model.AxisType;
import com.vaadin.flow.component.charts.model.ChartType;
import com.vaadin.flow.component.charts.model.DataSeries;
import com.vaadin.flow.component.charts.model.DataSeriesItem;
import com.vaadin.flow.component.charts.model.HorizontalAlign;
import com.vaadin.flow.component.charts.model.LayoutDirection;
import com.vaadin.flow.component.charts.model.Legend;
import com.vaadin.flow.component.charts.model.PlotOptionsLine;
import com.vaadin.flow.component.charts.model.Series;
import com.vaadin.flow.component.charts.model.Title;
import com.vaadin.flow.component.charts.model.VerticalAlign;
import com.vaadin.flow.component.charts.model.XAxis;
import com.vaadin.flow.component.charts.model.YAxis;

public class UsersScoreProgressionChart extends AbstractChart {

	private final User loggedInUser;
	private List<UserTotalScoreBean> beans;
	private Map<String, Series> seriesPerId = Maps.newHashMap();

	public UsersScoreProgressionChart(
			List<UserTotalScoreBean> beans,
			User loggedInUser) {
		super(ChartType.LINE);
		this.beans = beans;
		this.loggedInUser = loggedInUser;

		initChartConfig();
	}

	private void initChartConfig() {
		conf = getConfiguration();
		conf.setTitle(new Title("User scores"));
		conf.getTooltip()
				.setFormatter(
						"'<b>'+ this.series.name +'</b><br/>'+ Highcharts.dateFormat('%e. %b %H:%M', this.x) +': '+ this.y +' points'");

		PlotOptionsLine plotOptions = new PlotOptionsLine();
		plotOptions.getDataLabels().setEnabled(true);
		conf.setPlotOptions(plotOptions);

		XAxis xAxis = new XAxis();
		xAxis.setType(AxisType.DATETIME);
		xAxis.getDateTimeLabelFormats().setDay("%e %b %Y");
		conf.addxAxis(xAxis);

		YAxis yAxis = new YAxis();
		yAxis.setMin(0);
		yAxis.setTitle(new AxisTitle("Points"));
		conf.addyAxis(yAxis);

		Legend legend = conf.getLegend();
		legend.setLayout(LayoutDirection.VERTICAL);
		legend.setAlign(HorizontalAlign.RIGHT);
		legend.setVerticalAlign(VerticalAlign.TOP);
		legend.setX(-10d);
		legend.setY(100d);
		legend.setBorderRadius(0);

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
		drawChart(true);
	}

}

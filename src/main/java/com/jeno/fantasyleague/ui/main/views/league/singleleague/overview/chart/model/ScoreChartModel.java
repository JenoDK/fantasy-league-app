package com.jeno.fantasyleague.ui.main.views.league.singleleague.overview.chart.model;

import com.vaadin.flow.templatemodel.TemplateModel;

import java.util.List;

public interface ScoreChartModel extends TemplateModel {
	List<IconColor> iconColors();
	void setIconColors(List<IconColor> iconColors);

	List<ScoreChartSerieData> seriesData();
	void setSeriesData(List<ScoreChartSerieData> seriesData);

	List<ScoreChartData> getScoresPerUser();
	void setScoresPerUser(List<ScoreChartData> scoresPerUser);

	List<String> chartHeaders();
	void setChartHeaders(List<String> chartHeaders);

	List<DataRole> dataRoles();
	void setDataRoles(List<DataRole> dataRoles);

	List<String> colors();
	void setColors(List<String> colors);

	double getHighestScore();
	void setHighestScore(double highestScore);

	List<ScoreChartDataPerDate> getScoresPerDate();
	void setScoresPerDate(List<ScoreChartDataPerDate> scoresPerDate);
}

package com.jeno.fantasyleague.ui.common.charts;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.Maps;
import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.Configuration;

public abstract class AbstractChart extends Chart {

	protected Configuration conf;
	protected Map<String, Boolean> seriesShownPerId;

	public AbstractChart(ChartType type) {
		super(type);

		conf = getConfiguration();
		seriesShownPerId = Maps.newHashMap();
	}

	public void removeSeries(String id) {
		seriesShownPerId.put(id, false);
		setData();
	}

	public void addSeries(String id) {
		seriesShownPerId.put(id, true);
		setData();
	}

	public boolean needsShowing(String name) {
		return seriesShownPerId.containsKey(name) && seriesShownPerId.get(name);
	}

	public Set<String> getIdsWithSeriesShown() {
		return seriesShownPerId.entrySet().stream()
				.filter(entry -> entry.getValue())
				.map(Map.Entry::getKey)
				.collect(Collectors.toSet());
	}

	protected abstract void setData();

}

package com.jeno.fantasyleague.ui.main.views.league.singleleague.overview.charts;

import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;
import com.jeno.fantasyleague.ui.common.charts.AbstractChart;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class UsersChartLayout extends VerticalLayout {

	private Set<String> userNames;
	private Set<String> userNamesToSelectByDefault;
	private AbstractChart chart;

	private HorizontalLayout userSelectionLayout;
	private Map<String, Checkbox> userSelectionsCheckboxes;

	public UsersChartLayout(Set<String> userNames, Set<String> userNamesToSelectByDefault, AbstractChart chart) {
		this.userNames = userNames;
		this.userNamesToSelectByDefault = userNamesToSelectByDefault;
		this.chart = chart;
		chart.addClassName("has-selection-section");

		setMargin(false);
		setSpacing(false);

		userSelectionLayout = new HorizontalLayout();
		userSelectionLayout.addClassName("chart-user-selection");
		userSelectionsCheckboxes = Maps.newHashMap();
		initUserSelectionLayout();

		add(userSelectionLayout);
		add(chart);
	}

	private void initUserSelectionLayout() {
		userSelectionLayout.removeAll();
		userSelectionsCheckboxes.clear();
		userNames.stream()
				.forEach(userName -> {
					Checkbox Checkbox = new Checkbox(userName);
					Checkbox.setValue(userNamesToSelectByDefault.contains(userName));
					Checkbox.addValueChangeListener(event -> {
						if (event.getValue()) {
							chart.addSeries(userName);
						} else {
							chart.removeSeries(userName);
						}
					});
					userSelectionsCheckboxes.put(userName, Checkbox);
				});
		userSelectionsCheckboxes.values()
				.forEach(userSelectionLayout::add);
	}

	public void refresh(Set<String> userNames) {
		this.userNames = userNames;
		initUserSelectionLayout();
	}
}

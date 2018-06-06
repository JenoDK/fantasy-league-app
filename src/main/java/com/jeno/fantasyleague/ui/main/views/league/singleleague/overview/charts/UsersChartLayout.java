package com.jeno.fantasyleague.ui.main.views.league.singleleague.overview.charts;

import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;
import com.jeno.fantasyleague.ui.common.charts.AbstractChart;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

public class UsersChartLayout extends VerticalLayout {

	private Set<String> userNames;
	private Set<String> userNamesToSelectByDefault;
	private AbstractChart chart;

	private HorizontalLayout userSelectionLayout;
	private Map<String, CheckBox> userSelectionsCheckboxes;

	public UsersChartLayout(Set<String> userNames, Set<String> userNamesToSelectByDefault, AbstractChart chart) {
		this.userNames = userNames;
		this.userNamesToSelectByDefault = userNamesToSelectByDefault;
		this.chart = chart;
		chart.addStyleName("has-selection-section");

		setMargin(false);
		setSpacing(false);

		userSelectionLayout = new HorizontalLayout();
		userSelectionLayout.addStyleName("chart-user-selection");
		userSelectionsCheckboxes = Maps.newHashMap();
		initUserSelectionLayout();

		addComponent(userSelectionLayout);
		addComponent(chart);
	}

	private void initUserSelectionLayout() {
		userSelectionLayout.removeAllComponents();
		userSelectionsCheckboxes.clear();
		userNames.stream()
				.forEach(userName -> {
					CheckBox checkBox = new CheckBox(userName);
					checkBox.setValue(userNamesToSelectByDefault.contains(userName));
					checkBox.addValueChangeListener(event -> {
						if (event.getValue()) {
							chart.addSeries(userName);
						} else {
							chart.removeSeries(userName);
						}
					});
					userSelectionsCheckboxes.put(userName, checkBox);
				});
		userSelectionsCheckboxes.values()
				.forEach(userSelectionLayout::addComponent);
	}

	public void refresh(Set<String> userNames) {
		this.userNames = userNames;
		initUserSelectionLayout();
	}
}

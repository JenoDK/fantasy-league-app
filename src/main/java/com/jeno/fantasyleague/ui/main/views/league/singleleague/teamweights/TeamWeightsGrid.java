package com.jeno.fantasyleague.ui.main.views.league.singleleague.teamweights;

import java.time.LocalDateTime;

import com.jeno.fantasyleague.backend.model.League;
import com.jeno.fantasyleague.ui.common.field.StringToPositiveIntegerConverter;
import com.jeno.fantasyleague.util.DecimalUtil;
import com.jeno.fantasyleague.util.LayoutUtil;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

public class TeamWeightsGrid extends Grid<TeamWeightBean> {

	private final BehaviorSubject<TeamWeightChangedBean> weightChanged = BehaviorSubject.create();

	public TeamWeightsGrid(League league, ListDataProvider<TeamWeightBean> dataProvider) {
		super();
		setDataProvider(dataProvider);

		setWidth("100%");
		setHeightByRows(true);
		setHeight((38f * (dataProvider.getItems().size() + 1)) + "px");

		initGrid(league);
	}

	private void initGrid(League league) {
		addColumn(new ComponentRenderer<>(teamWeight -> LayoutUtil.createTeamLayout(teamWeight.getContestant())))
				.setHeader("Team");
		addColumn(TeamWeightBean::getPowerIndex)
				.setHeader("Power Index");
		addColumn(bean -> "$" + DecimalUtil.getTwoDecimalsThousandSeperator(bean.getShareCost()))
				.setHeader("Stock price");
		addColumn(new ComponentRenderer<>(teamWeight -> createWeightField(league, teamWeight)))
				.setWidth("150px")
				.setHeader("Stocks purchased");
	}

	private TextField createWeightField(League league, TeamWeightBean weight) {
		TextField field = new TextField();
		field.setWidth("100px");
		field.setReadOnly(!LocalDateTime.now().isBefore(league.getLeague_starting_date()));
		Binder<TeamWeightBean> binder = new Binder<>(TeamWeightBean.class);
		binder.forField(field)
				.withConverter(new StringToPositiveIntegerConverter(0, "Must enter a positive number"))
//				.withValidator(new IntegerRangeValidator("Must enter a vlaue between 0 and 10", 0, 10))
				.bind(TeamWeightBean::getStocksPurchased, TeamWeightBean::setStocksPurchased);
		binder.setBean(weight);
		binder.addStatusChangeListener(status -> weightChanged.onNext(new TeamWeightChangedBean(weight, status.getBinder().isValid())));
		weight.changes().subscribe(binder::setBean);
//		field.addClassName(ValoTheme.TEXTFIELD_TINY);
		return field;
	}

	public Observable<TeamWeightChangedBean> weightChanged() {
		return weightChanged;
	}

	public class TeamWeightChangedBean {

		private final TeamWeightBean bean;
		private final boolean valid;

		public TeamWeightChangedBean(TeamWeightBean bean, boolean valid) {
			this.bean = bean;
			this.valid = valid;
		}

		public TeamWeightBean getBean() {
			return bean;
		}

		public boolean isValid() {
			return valid;
		}
	}
}

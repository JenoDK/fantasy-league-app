package com.jeno.fantasyleague.ui.main.views.league.singleleague.teamweights;

import java.time.LocalDateTime;

import com.jeno.fantasyleague.model.League;
import com.jeno.fantasyleague.ui.common.field.StringToPositiveIntegerConverter;
import com.jeno.fantasyleague.util.DecimalUtil;
import com.jeno.fantasyleague.util.GridUtil;
import com.vaadin.data.Binder;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.data.validator.IntegerRangeValidator;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.ui.Grid;
import com.vaadin.ui.TextField;
import com.vaadin.ui.renderers.ComponentRenderer;
import com.vaadin.ui.themes.ValoTheme;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

public class TeamWeightsGrid extends Grid<TeamWeightBean> {

	private final BehaviorSubject<TeamWeightChangedBean> weightChanged = BehaviorSubject.create();

	public TeamWeightsGrid(League league, ListDataProvider<TeamWeightBean> dataProvider) {
		super();
		setDataProvider(dataProvider);

		setWidth("100%");
		setHeightMode(HeightMode.ROW);
		setHeight(38f * (dataProvider.getItems().size() + 1), Unit.PIXELS);

		initGrid(league);
	}

	private void initGrid(League league) {
		addColumn(teamWeight -> GridUtil.createTeamLayout(teamWeight.getContestant()), new ComponentRenderer())
				.setCaption("Team");
		addColumn(TeamWeightBean::getPowerIndex)
				.setCaption("Power Index");
		addColumn(bean -> "$" + DecimalUtil.getTwoDecimalsThousandSeperator(bean.getShareCost()))
				.setCaption("Stock price");
		addColumn(teamWeight -> createWeightField(league, teamWeight), new ComponentRenderer())
				.setWidth(150)
				.setCaption("Stocks purchased");
	}

	private TextField createWeightField(League league, TeamWeightBean weight) {
		TextField field = new TextField();
		field.setWidth(100, Unit.PIXELS);
		field.setReadOnly(!LocalDateTime.now().isBefore(league.getLeague_starting_date()));
		Binder<TeamWeightBean> binder = new Binder<>(TeamWeightBean.class);
		binder.forField(field)
				.withConverter(new StringToPositiveIntegerConverter(0, "Must enter a positive number"))
//				.withValidator(new IntegerRangeValidator("Must enter a vlaue between 0 and 10", 0, 10))
				.bind(TeamWeightBean::getStocksPurchased, TeamWeightBean::setStocksPurchased);
		binder.setBean(weight);
		binder.addStatusChangeListener(status -> weightChanged.onNext(new TeamWeightChangedBean(weight, status.getBinder().isValid())));
		weight.changes().subscribe(newBean -> binder.setBean(newBean));
		field.addStyleName(ValoTheme.TEXTFIELD_TINY);
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

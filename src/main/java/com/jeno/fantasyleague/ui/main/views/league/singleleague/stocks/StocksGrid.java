package com.jeno.fantasyleague.ui.main.views.league.singleleague.stocks;

import com.jeno.fantasyleague.backend.model.League;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

import java.util.function.Function;

public class StocksGrid extends Grid<StocksBean> {

	private final BehaviorSubject<StocksBean> stockChanged = BehaviorSubject.create();

	public StocksGrid(League league, ListDataProvider<StocksBean> dataProvider, Function<StocksBean, ValidationResult> validateStockChange, boolean forAdminModule) {
		super();
		setDataProvider(dataProvider);
		initGrid(league, validateStockChange, forAdminModule);
	}

	private void initGrid(League league, Function<StocksBean, ValidationResult> validateStockChange, boolean forAdminModule) {
		setSelectionMode(SelectionMode.NONE);
		setHeightByRows(true);

		addColumn(new ComponentRenderer<>(b -> new StockCard(league, b, stockChanged, validateStockChange, forAdminModule)));
		addThemeNames("card-grid", "no-row-borders");
		removeThemeVariants(GridVariant.LUMO_ROW_STRIPES);
		getStyle().set("border", "none");
	}

	public Observable<StocksBean> stockChanged() {
		return stockChanged;
	}

}

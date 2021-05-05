package com.jeno.fantasyleague.ui.main.views.league.singleleague.stocks;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.function.Function;

import com.jeno.fantasyleague.backend.model.League;
import com.jeno.fantasyleague.resources.Resources;
import com.jeno.fantasyleague.ui.common.label.StatusLabel;
import com.jeno.fantasyleague.ui.common.window.PopupWindow;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.matches.MatchBindingModel;
import com.jeno.fantasyleague.util.DecimalUtil;
import com.jeno.fantasyleague.util.LayoutUtil;
import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.binder.Validator;

import io.reactivex.subjects.BehaviorSubject;

@Tag("stock-card")
@JsModule("./src/views/stocks/stock-card.js")
public class StockCard extends PolymerTemplate<MatchBindingModel> {

	private final StocksBean bean;
	private final BehaviorSubject<StocksBean> stocksChanged;
	private final League league;
	private final Function<StocksBean, ValidationResult> validateStockChange;

	@Id("teamLayout")
	private Div teamLayout;

	@Id("buyStocks-button")
	private Button buyStocks;

	@Id("pricePerStock")
	private H4 pricePerStock;

	@Id("stocksPurchased")
	private H4 stocksPurchased;

	public StockCard(League league, StocksBean bean, BehaviorSubject<StocksBean> stocksChanged, Function<StocksBean, ValidationResult> validateStockChange) {
		this.league = league;
		this.bean = bean;
		this.stocksChanged = stocksChanged;
		this.validateStockChange = validateStockChange;

		initLayout();
	}

	private void initLayout() {
		teamLayout.add(LayoutUtil.createTeamLayout(bean.getContestant()));
		buyStocks.setText(Resources.getMessage("buyStocks"));
		buyStocks.setEnabled(LocalDateTime.now().isBefore(league.getLeague_starting_date()));
		pricePerStock.setText(Resources.getMessage("price", DecimalUtil.getTwoDecimalsThousandSeperator(bean.getShareCost())));
		updateStocksPurchasedLabel();
	}

	private void updateStocksPurchasedLabel() {
		String valueOfBoughtStocks = DecimalUtil.getTwoDecimalsThousandSeperator(bean.getShareCost().multiply(BigDecimal.valueOf(bean.getStocksPurchased())));
		stocksPurchased.setText(Resources.getMessage("stocksBought", bean.getStocksPurchased(), valueOfBoughtStocks));
	}

	@ClientCallable
	private void buyStocks() {
		new PopupWindow.Builder("Buy Stocks", this::createBuyStocksLayout)
				.setType(PopupWindow.Type.CONFIRM)
				.sizeUndefined(true)
				.build()
				.open();
	}

	private Component createBuyStocksLayout(PopupWindow popupWindow) {
		VerticalLayout layout = new VerticalLayout();
		layout.setAlignItems(FlexComponent.Alignment.CENTER);

		HorizontalLayout amountLayout = new HorizontalLayout();
		amountLayout.setAlignItems(FlexComponent.Alignment.CENTER);
		amountLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
		amountLayout.add(LayoutUtil.createTeamLayout(true, bean.getContestant(), ""));

		if (LocalDateTime.now().isBefore(league.getLeague_starting_date())) {
			StatusLabel statusLabel = new StatusLabel();
			statusLabel.setVisible(false);

			Binder<StocksBean> binder = new Binder<>();
			binder.setValidationStatusHandler(LayoutUtil.getDefaultBinderValidationStatusHandler(statusLabel));
			binder.setBean(bean);

			amountLayout.add(LayoutUtil.createPositiveIntegerTextField(binder, StocksBean::getStocksPurchased, StocksBean::setStocksPurchased, b -> b.withValidator((Validator<Integer>) (value, context) -> {
				Integer ogValue = bean.getStocksPurchased();
				bean.setStocksPurchased(value);
				ValidationResult validationResult = validateStockChange.apply(bean);
				if (validationResult.isError()) {
					bean.setStocksPurchased(ogValue);
				}
				return validationResult;
			})));

			popupWindow.setOnConfirm(() -> {
				boolean valid = binder.isValid();
				if (valid) {
					stocksChanged.onNext(bean);
					popupWindow.close();
					updateStocksPurchasedLabel();
				}
				return valid;
			});
			layout.add(statusLabel);
		} else {
			amountLayout.add(new Label("" + bean.getStocksPurchased()));
		}

		layout.add(amountLayout);
		return layout;
	}
}

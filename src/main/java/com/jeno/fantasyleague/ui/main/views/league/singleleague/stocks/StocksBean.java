package com.jeno.fantasyleague.ui.main.views.league.singleleague.stocks;

import com.jeno.fantasyleague.backend.model.Contestant;
import com.jeno.fantasyleague.backend.model.ContestantWeight;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

import java.math.BigDecimal;

public class StocksBean {

	public static final BigDecimal COSMETICAL_PRICE_MODIFIER = BigDecimal.valueOf(100000);

	private final BehaviorSubject<StocksBean> changes = BehaviorSubject.create();

	private ContestantWeight contestantWeight;
	private BigDecimal shareCost;
	private Integer stocksPurchased;

	public StocksBean(ContestantWeight contestantWeight) {
		this.contestantWeight = contestantWeight;
		this.shareCost = BigDecimal.valueOf(contestantWeight.getContestant().getPower_index()).divide(BigDecimal.valueOf(10));
		this.stocksPurchased = contestantWeight.getWeight();
	}

	public ContestantWeight getContestantWeight() {
		return contestantWeight;
	}

	public Contestant getContestant() {
		return contestantWeight.getContestant();
	}

	public Integer getStocksPurchased() {
		return stocksPurchased != null ? stocksPurchased : 0;
	}

	public void setStocksPurchased(Integer stocksPruchased) {
		this.stocksPurchased = stocksPruchased;
	}

	public Double getPowerIndex() {
		return contestantWeight.getContestant().getPower_index();
	}

	public BigDecimal getShareCost() {
		return shareCost.multiply(COSMETICAL_PRICE_MODIFIER);
	}

	public ContestantWeight setWeightAndContestantWeight() {
		contestantWeight.setWeight(stocksPurchased);
		return contestantWeight;
	}

	public void reset() {
		if (contestantWeight.getWeight() != stocksPurchased) {
			setStocksPurchased(contestantWeight.getWeight());
			changes.onNext(this);
		}
	}

	public Observable<StocksBean> changes() {
		return changes;
	}

	public BigDecimal getPricePayed() {
		return shareCost.multiply(BigDecimal.valueOf(stocksPurchased));
	}
}

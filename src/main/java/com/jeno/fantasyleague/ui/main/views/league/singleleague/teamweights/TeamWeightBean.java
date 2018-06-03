package com.jeno.fantasyleague.ui.main.views.league.singleleague.teamweights;

import com.jeno.fantasyleague.model.Contestant;
import com.jeno.fantasyleague.model.ContestantWeight;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

public class TeamWeightBean {

	public static final Integer COSMETICAL_PRICE_MODIFIER = 100000;

	private final BehaviorSubject<TeamWeightBean> changes = BehaviorSubject.create();

	private ContestantWeight contestantWeight;
	private Double shareCost;
	private Integer stocksPurchased;

	public TeamWeightBean(ContestantWeight contestantWeight) {
		this.contestantWeight = contestantWeight;
		this.shareCost = contestantWeight.getContestant().getPower_index().doubleValue() / 10d;
		this.stocksPurchased = contestantWeight.getWeight();
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

	public Integer getPowerIndex() {
		return contestantWeight.getContestant().getPower_index();
	}

	public Double getShareCost() {
		return shareCost * COSMETICAL_PRICE_MODIFIER;
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

	public Observable<TeamWeightBean> changes() {
		return changes;
	}

	public double getPricePayed() {
		return stocksPurchased * shareCost;
	}
}

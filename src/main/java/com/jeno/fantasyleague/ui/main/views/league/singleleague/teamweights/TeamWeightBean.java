package com.jeno.fantasyleague.ui.main.views.league.singleleague.teamweights;

import com.jeno.fantasyleague.model.Contestant;
import com.jeno.fantasyleague.model.ContestantWeight;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

public class TeamWeightBean {

	private final BehaviorSubject<TeamWeightBean> changes = BehaviorSubject.create();

	private ContestantWeight contestantWeight;
	private Integer weight;

	public TeamWeightBean(ContestantWeight contestantWeight) {
		this.contestantWeight = contestantWeight;
		this.weight = contestantWeight.getWeight();
	}

	public Contestant getContestant() {
		return contestantWeight.getContestant();
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public Integer getPowerIndex() {
		return contestantWeight.getContestant().getPower_index();
	}

	public ContestantWeight setWeightAndContestantWeight() {
		contestantWeight.setWeight(weight);
		return contestantWeight;
	}

	public void reset() {
		if (contestantWeight.getWeight() != weight) {
			setWeight(contestantWeight.getWeight());
			changes.onNext(this);
		}
	}

	public Observable<TeamWeightBean> changes() {
		return changes;
	}
}

package com.jeno.fantasyleague.ui.main.views.league.singleleague.knockoutstage;

import java.util.Objects;
import java.util.Optional;

import com.jeno.fantasyleague.util.GridUtil;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.data.binder.Setter;
import com.vaadin.flow.function.ValueProvider;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

public class GameResultsLayout extends HorizontalLayout {

	private final BehaviorSubject<KnockoutGameBean> scoreChanged = BehaviorSubject.create();

	public GameResultsLayout(
			boolean editable,
			KnockoutGameBean bean,
			ValueProvider<KnockoutGameBean, Integer> homeTeamGetter,
			Setter<KnockoutGameBean, Integer> homeTeamSetter,
			ValueProvider<KnockoutGameBean, Integer> awayTeamGetter,
			Setter<KnockoutGameBean, Integer> awayTeamSetter,
			ValueProvider<KnockoutGameBean, Optional<Boolean>> winnerGetter,
			Setter<KnockoutGameBean, Boolean> winnerSetter) {
		super();

		HorizontalLayout scoreWrapper = new HorizontalLayout();
		scoreWrapper.setMargin(false);
		if (editable) {
			GridUtil.getTextFieldScoreLayout(
					bean,
					homeTeamGetter,
					homeTeamSetter,
					awayTeamGetter,
					awayTeamSetter,
					scoreChanged,
					scoreWrapper,
					false);
		} else {
			Label homeTeamScoreLabel = new Label(homeTeamGetter.apply(bean) != null ? homeTeamGetter.apply(bean).toString() : "-");
			Label awayTeamScoreLabel = new Label(awayTeamGetter.apply(bean) != null ? awayTeamGetter.apply(bean).toString() : "-");
			scoreWrapper.add(homeTeamScoreLabel);
			scoreWrapper.add(awayTeamScoreLabel);
//			scoreWrapper.setComponentAlignment(homeTeamScoreLabel, Alignment.MIDDLE_CENTER);
//			scoreWrapper.setComponentAlignment(awayTeamScoreLabel, Alignment.MIDDLE_CENTER);
		}

		VerticalLayout winnerWrapper = new VerticalLayout();
		winnerWrapper.setMargin(false);
		winnerWrapper.setVisible(scoreNotNullAndEqual(bean, homeTeamGetter, awayTeamGetter));
		RadioButtonGroup<String> winnerSelection = new RadioButtonGroup<>();
		winnerSelection.addClassName("winner-selection");
		winnerSelection.setItems("homeTeam", "awayTeam");
//		winnerSelection.setItemCaptionGenerator(ignored -> "");
		if (winnerGetter.apply(bean).isPresent()) {
			winnerSelection.setValue(winnerGetter.apply(bean).get() ? "homeTeam" : "awayTeam");
		}
		winnerSelection.addValueChangeListener(event -> {
			winnerSetter.accept(bean, "homeTeam".equals(event.getValue()));
			scoreChanged.onNext(bean);
		});
		winnerSelection.setEnabled(editable);
		winnerWrapper.add(winnerSelection);

		scoreChanged.subscribe(gameBean -> winnerWrapper.setVisible(scoreNotNullAndEqual(bean, homeTeamGetter, awayTeamGetter)));

		add(scoreWrapper);
		add(winnerWrapper);
	}

	public boolean scoreNotNullAndEqual(KnockoutGameBean bean, ValueProvider<KnockoutGameBean, Integer> homeTeamGetter, ValueProvider<KnockoutGameBean, Integer> awayTeamGetter) {
		return
				Objects.nonNull(homeTeamGetter.apply(bean)) &&
				Objects.nonNull(awayTeamGetter.apply(bean)) &&
				homeTeamGetter.apply(bean).equals(awayTeamGetter.apply(bean));
	}

	public Observable<KnockoutGameBean> scoreChanged() {
		return scoreChanged;
	}
}

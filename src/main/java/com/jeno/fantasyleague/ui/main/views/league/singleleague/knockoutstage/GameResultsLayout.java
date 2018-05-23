package com.jeno.fantasyleague.ui.main.views.league.singleleague.knockoutstage;

import java.util.Objects;
import java.util.Optional;

import com.jeno.fantasyleague.util.GridUtil;
import com.vaadin.data.ValueProvider;
import com.vaadin.server.Setter;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.RadioButtonGroup;
import com.vaadin.ui.VerticalLayout;
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

		VerticalLayout scoreWrapper = new VerticalLayout();
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
			scoreWrapper.addComponent(homeTeamScoreLabel);
			scoreWrapper.addComponent(awayTeamScoreLabel);
			scoreWrapper.setComponentAlignment(homeTeamScoreLabel, Alignment.MIDDLE_CENTER);
			scoreWrapper.setComponentAlignment(awayTeamScoreLabel, Alignment.MIDDLE_CENTER);
		}

		if (Objects.isNull(bean.getGame().getHome_team()) || Objects.isNull(bean.getGame().getAway_team())) {
			scoreWrapper.setEnabled(false);
		}

		VerticalLayout winnerWrapper = new VerticalLayout();
		winnerWrapper.setMargin(false);
		winnerWrapper.setVisible(scoreNotNullAndEqual(bean, homeTeamGetter, awayTeamGetter));
		RadioButtonGroup<String> winnerSelection = new RadioButtonGroup<>();
		winnerSelection.addStyleName("winner-selection");
		winnerSelection.setItems("homeTeam", "awayTeam");
		winnerSelection.setItemCaptionGenerator(ignored -> "");
		if (winnerGetter.apply(bean).isPresent()) {
			winnerSelection.setValue(winnerGetter.apply(bean).get() ? "homeTeam" : "awayTeam");
		}
		winnerSelection.addValueChangeListener(event -> {
			winnerSetter.accept(bean, "homeTeam".equals(event.getValue()));
			scoreChanged.onNext(bean);
		});
		winnerSelection.setEnabled(editable);
		winnerWrapper.addComponent(winnerSelection);

		scoreChanged.subscribe(gameBean -> winnerWrapper.setVisible(scoreNotNullAndEqual(bean, homeTeamGetter, awayTeamGetter)));

		addComponent(scoreWrapper);
		addComponent(winnerWrapper);
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

package com.jeno.fantasyleague.util;

import java.util.Optional;

import com.jeno.fantasyleague.backend.model.Contestant;
import com.jeno.fantasyleague.resources.Resources;
import com.jeno.fantasyleague.ui.common.field.NonNullValidator;
import com.jeno.fantasyleague.ui.common.field.StringToPositiveIntegerConverter;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.Setter;
import com.vaadin.flow.function.ValueProvider;
import io.reactivex.subjects.BehaviorSubject;

public class GridUtil {

	private GridUtil() {
	}

	public static HorizontalLayout createTeamLayout(Contestant contestant, String teamPlaceHolder) {
		return Optional.ofNullable(contestant)
				.map(GridUtil::createTeamLayout)
				.orElse(new HorizontalLayout(new Label(teamPlaceHolder)));
	}

	public static HorizontalLayout createTeamLayout(Contestant contestant) {
		HorizontalLayout layout = new HorizontalLayout();
		Image icon = new Image();
		icon.setWidth("42px");
		icon.setHeight("28px");
		icon.setSrc(contestant.getIcon_path());
		Label teamName = new Label(contestant.getName());
		layout.add(icon, teamName);
		return layout;
	}

	public static String getScores(Integer teamAScore, Integer teamBScore) {
		return (teamAScore != null ? teamAScore : " ") + " - " + (teamBScore != null ? teamBScore : " ");
	}

	public static  <T> HorizontalLayout getTextFieldScoreLayout(
			T t,
			ValueProvider<T, Integer> homeTeamGetter,
			Setter<T, Integer> homeTeamSetter,
			ValueProvider<T, Integer> awayTeamGetter,
			Setter<T, Integer> awayTeamSetter,
			BehaviorSubject<T> scoreChanged,
			HorizontalLayout layout) {
		return getTextFieldScoreLayout(t, homeTeamGetter, homeTeamSetter, awayTeamGetter, awayTeamSetter, scoreChanged, layout, true);
	}

	public static  <T> HorizontalLayout getTextFieldScoreLayout(
			T t,
			ValueProvider<T, Integer> homeTeamGetter,
			Setter<T, Integer> homeTeamSetter,
			ValueProvider<T, Integer> awayTeamGetter,
			Setter<T, Integer> awayTeamSetter,
			BehaviorSubject<T> scoreChanged,
			HorizontalLayout layout,
			boolean addLabelBetween) {
		Binder<T> binder = new Binder<>();
		layout.add(createPositiveIntegerTextField(t, binder, homeTeamGetter, homeTeamSetter));
		if (addLabelBetween) {
			layout.add(new Label("-"));
		}
		layout.add(createPositiveIntegerTextField(t, binder, awayTeamGetter, awayTeamSetter));
		binder.addValueChangeListener(ignored -> {
			BinderValidationStatus<T> status = binder.validate();
			if (status.isOk()) {
				scoreChanged.onNext(t);
			}
		});
		binder.setBean(t);
		return layout;
	}

	public static <T> TextField createPositiveIntegerTextField(T t, Binder<T> binder, ValueProvider<T, Integer> getter, Setter<T, Integer> setter) {
		TextField field = new TextField();
		field.setWidth("30px");
		field.addClassName("v-slot-ignore-error-indicator");
		binder.forField(field)
				.withNullRepresentation(" ")
				.withConverter(new StringToPositiveIntegerConverter(null, Resources.getMessage("error.positiveNumber")))
				.withValidator(new NonNullValidator(Resources.getMessage("error.cannotBeNull")))
				.bind(getter, setter);
//		field.addClassName(ValoTheme.TEXTFIELD_TINY);
		return field;
	}
}

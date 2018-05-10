package com.jeno.fantasyleague.util;

import com.jeno.fantasyleague.model.Contestant;
import com.jeno.fantasyleague.resources.Resources;
import com.jeno.fantasyleague.ui.common.field.NonNullValidator;
import com.jeno.fantasyleague.ui.common.field.StringToPositiveConverter;
import com.vaadin.data.Binder;
import com.vaadin.data.BinderValidationStatus;
import com.vaadin.data.ValueProvider;
import com.vaadin.server.Setter;
import com.vaadin.server.Sizeable;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;
import io.reactivex.subjects.BehaviorSubject;

public class GridUtil {

	private GridUtil() {
	}

	public static HorizontalLayout createTeamLayout(Contestant contestant) {
		HorizontalLayout layout = new HorizontalLayout();
		Image icon = new Image();
		icon.setWidth(42f, Sizeable.Unit.PIXELS);
		icon.setHeight(28f, Sizeable.Unit.PIXELS);
		icon.setSource(new ThemeResource(contestant.getIcon_path()));
		Label teamName = new Label(contestant.getName());
		layout.addComponents(icon, teamName);
		return layout;
	}

	public static  <T> HorizontalLayout getTextFieldScoreLayout(
			T t,
			ValueProvider<T, Integer> homeTeamGetter,
			Setter<T, Integer> homeTeamSetter,
			ValueProvider<T, Integer> awayTeamGetter,
			Setter<T, Integer> awayTeamSetter,
			BehaviorSubject<Boolean> scoreChanged) {
		HorizontalLayout layout = new HorizontalLayout();
		Binder<T> binder = new Binder<>();
		layout.addComponent(createScoreField(t, binder, homeTeamGetter, homeTeamSetter, scoreChanged));
		layout.addComponent(new Label("-"));
		layout.addComponent(createScoreField(t, binder, awayTeamGetter, awayTeamSetter, scoreChanged));
		binder.addValueChangeListener(ignored -> {
			BinderValidationStatus<T> status = binder.validate();
			if (status.isOk()) {
				scoreChanged.onNext(true);
			} else {
				scoreChanged.onNext(false);
			}
		});
		binder.setBean(t);
		return layout;
	}

	private static <T> TextField createScoreField(T t, Binder<T> binder, ValueProvider<T, Integer> getter, Setter<T, Integer> setter, BehaviorSubject<Boolean> scoreChanged) {
		TextField field = new TextField();
		field.setWidth(30, Sizeable.Unit.PIXELS);
		field.addStyleName("v-slot-ignore-error-indicator");
		binder.forField(field)
				.withNullRepresentation(" ")
				.withConverter(new StringToPositiveConverter(null, Resources.getMessage("error.positiveNumber")))
				.withValidator(new NonNullValidator(Resources.getMessage("error.cannotBeNull")))
				.bind(getter, setter);
		field.addStyleName(ValoTheme.TEXTFIELD_TINY);
		return field;
	}
}

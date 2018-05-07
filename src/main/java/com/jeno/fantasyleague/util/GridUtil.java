package com.jeno.fantasyleague.util;

import com.jeno.fantasyleague.model.Contestant;
import com.jeno.fantasyleague.ui.common.field.StringToPositiveConverter;
import com.vaadin.data.Binder;
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
			BehaviorSubject<Object> scoreChanged) {
		HorizontalLayout layout = new HorizontalLayout();
		layout.addComponent(createScoreField(t, homeTeamGetter, homeTeamSetter, scoreChanged));
		layout.addComponent(new Label("-"));
		layout.addComponent(createScoreField(t, awayTeamGetter, awayTeamSetter, scoreChanged));
		return layout;
	}

	private static <T> TextField createScoreField(T t, ValueProvider<T, Integer> getter, Setter<T, Integer> setter, BehaviorSubject<Object> scoreChanged) {
		TextField field = new TextField();
		field.setWidth(30, Sizeable.Unit.PIXELS);
		field.addStyleName("v-slot-ignore-error-indicator");
		Binder<T> binder = new Binder<>();
		binder.forField(field)
				.withConverter(new StringToPositiveConverter(0, "Must enter a positive number"))
				.bind(getter, setter);
		binder.setBean(t);
		binder.addValueChangeListener(ignored -> scoreChanged.onNext(ignored.getValue()));
		field.addStyleName(ValoTheme.TEXTFIELD_TINY);
		return field;
	}
}

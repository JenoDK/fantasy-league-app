package com.jeno.fantasyleague.util;

import java.util.Optional;
import java.util.function.Function;

import com.jeno.fantasyleague.backend.model.Contestant;
import com.jeno.fantasyleague.backend.model.User;
import com.jeno.fantasyleague.resources.Resources;
import com.jeno.fantasyleague.ui.common.field.NonNullValidator;
import com.jeno.fantasyleague.ui.common.field.StringToPositiveIntegerConverter;
import com.jeno.fantasyleague.ui.common.grid.CustomGridBuilder;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.Setter;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.server.StreamResource;

import io.reactivex.subjects.BehaviorSubject;

public class LayoutUtil {

	private LayoutUtil() {
	}

	public static CustomGridBuilder.IconColumnValue getUserIconColumnValue(User user) {
		return ImageUtil.getUserProfilePictureResource(user).map(CustomGridBuilder.IconColumnValue::new).orElseGet(() -> new CustomGridBuilder.IconColumnValue(Images.DEFAULT_PROFILE_PICTURE, grid -> {}));
	}

	public static HorizontalLayout createTeamLayout(Contestant contestant, String teamPlaceHolder) {
		return LayoutUtil.createTeamLayout(false, contestant, teamPlaceHolder);
	}

	public static HorizontalLayout createTeamLayout(boolean nameToTheLeft, Contestant contestant, String teamPlaceHolder) {
		return Optional.ofNullable(contestant)
				.map(c -> LayoutUtil.createTeamLayout(nameToTheLeft, c))
				.orElse(new HorizontalLayout(new Label(teamPlaceHolder)));
	}

	public static HorizontalLayout createTeamLayout(Contestant contestant) {
		return createTeamLayout(false, contestant);
	}

	public static HorizontalLayout createTeamLayout(boolean nameToTheLeft, Contestant contestant) {
		HorizontalLayout layout = new HorizontalLayout();
		Image icon = new Image(contestant.getIcon_path(), contestant.getName());
		Label teamName = new Label(contestant.getName());
		if (nameToTheLeft) {
			layout.add(teamName, icon);
		} else {
			layout.add(icon, teamName);
		}
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
//		layout.add(createPositiveIntegerTextField(t, binder, homeTeamGetter, homeTeamSetter));
		if (addLabelBetween) {
			layout.add(new Label("-"));
		}
//		layout.add(createPositiveIntegerTextField(t, binder, awayTeamGetter, awayTeamSetter));
		binder.addValueChangeListener(ignored -> {
			BinderValidationStatus<T> status = binder.validate();
			if (status.isOk()) {
				scoreChanged.onNext(t);
			}
		});
		binder.setBean(t);
		return layout;
	}

	public static <T> TextField createPositiveIntegerTextField(Binder<T> binder, ValueProvider<T, Integer> getter, Setter<T, Integer> setter) {
		return createPositiveIntegerTextField(binder, getter, setter, b -> b);
	}

	public static <T> TextField createPositiveIntegerTextField(Binder<T> binder, ValueProvider<T, Integer> getter, Setter<T, Integer> setter, Function<Binder.BindingBuilder<T, Integer>, Binder.BindingBuilder<T, Integer>> builderFunction) {
		TextField field = new TextField();
		field.setWidth("50px");
		field.addThemeName("no-error-msg");
		Binder.BindingBuilder<T, Integer> builder = binder.forField(field)
				.withNullRepresentation(" ")
				.withConverter(new StringToPositiveIntegerConverter(null, Resources.getMessage("error.positiveNumber")))
				.withValidator(new NonNullValidator(Resources.getMessage("error.cannotBeNull")));
		builderFunction.apply(builder);
		builder.bind(getter, setter);
		return field;
	}

	public static void initUserH4(H4 label, User user) {
		initUserH4(label, user, user.getUsername());
	}

	public static void initUserH4(H4 label, User user, String labelText) {
		Optional<StreamResource> userProfilePic = ImageUtil.getUserProfilePictureResource(user);
		Image icon = new Image();
		icon.setWidth("50px");
		icon.setHeight("50px");
		if (userProfilePic.isPresent()) {
			icon.setSrc(userProfilePic.get());
		} else {
			icon.setSrc(Images.DEFAULT_PROFILE_PICTURE);
		}
		label.setText(labelText);
		label.addComponentAsFirst(icon);
	}
}

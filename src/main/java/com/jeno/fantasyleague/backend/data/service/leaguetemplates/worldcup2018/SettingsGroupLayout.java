package com.jeno.fantasyleague.backend.data.service.leaguetemplates.worldcup2018;

import java.util.List;
import java.util.stream.Collectors;

import com.jeno.fantasyleague.backend.model.LeagueSetting;
import com.jeno.fantasyleague.resources.Resources;
import com.jeno.fantasyleague.ui.common.field.CustomButton;
import com.jeno.fantasyleague.ui.common.window.ConfirmationWindow;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

public class SettingsGroupLayout extends VerticalLayout {

	private final BehaviorSubject<List<LeagueSetting>> savedValues = BehaviorSubject.create();

	private List<IntegerSettingsBean> groupedLeagueSettings;
	private Button updateSettingsButton;

	public SettingsGroupLayout(String group, List<IntegerSettingsBean> groupedLeagueSettings) {
		this.groupedLeagueSettings = groupedLeagueSettings;

		updateSettingsButton = new CustomButton(Resources.getMessage("updateSettings"), VaadinIcon.USER_CHECK.create());
		updateSettingsButton.addClickListener(ignored -> {
			ConfirmationWindow.showConfirmationPopup(
					Resources.getMessage("areYouSure"),
					Resources.getMessage("willAffectUserResults"),
					aVoid -> {
						savedValues.onNext(groupedLeagueSettings.stream()
								.filter(IntegerSettingsBean::valueChanged)
								.map(IntegerSettingsBean::getModelItem)
								.collect(Collectors.toList()));
						updateSettingsButton.setVisible(false);
					},
					aVoid -> {});
		});
		updateSettingsButton.setVisible(false);

		groupedLeagueSettings.forEach(setting ->
				addSettingsField(setting, setting.getName().replaceAll(group, "")));
		add(updateSettingsButton);
	}

	private void addSettingsField(IntegerSettingsBean setting, String name) {
		TextField field = new TextField(Resources.getMessage(name));

		Binder<IntegerSettingsBean> binder = new Binder<>();
		binder.forField(field)
				.withConverter(new StringToIntegerConverter(0, Resources.getMessage("error.positiveNumber")))
				.bind(IntegerSettingsBean::getValue, IntegerSettingsBean::setValue);
		binder.setBean(setting);
		binder.addValueChangeListener(ignored -> decideUpdateButtonVisibility());

//		field.addClassName(ValoTheme.TEXTFIELD_TINY);
		add(field);
	}

	private void decideUpdateButtonVisibility() {
		if (groupedLeagueSettings.stream().anyMatch(IntegerSettingsBean::valueChanged)) {
			updateSettingsButton.setVisible(true);
		} else {
			updateSettingsButton.setVisible(false);
		}
	}

	public Observable<List<LeagueSetting>> saved() {
		return savedValues;
	}
}

package com.jeno.fantasyleague.data.service.leaguetemplates.worldcup2018;

import com.jeno.fantasyleague.model.LeagueSetting;
import com.jeno.fantasyleague.resources.Resources;
import com.jeno.fantasyleague.ui.common.field.CustomButton;
import com.jeno.fantasyleague.ui.common.window.ConfirmationWindow;
import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Button;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

import java.util.List;
import java.util.stream.Collectors;

public class SettingsGroupLayout extends VerticalLayout {

	private final BehaviorSubject<List<LeagueSetting>> savedValues = BehaviorSubject.create();

	private List<IntegerSettingsBean> groupedLeagueSettings;
	private Button updateSettingsButton;

	public SettingsGroupLayout(String group, List<IntegerSettingsBean> groupedLeagueSettings) {
		this.groupedLeagueSettings = groupedLeagueSettings;

		updateSettingsButton = new CustomButton(Resources.getMessage("updateSettings"), VaadinIcons.USER_CHECK);
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
		addComponent(updateSettingsButton);
	}

	private void addSettingsField(IntegerSettingsBean setting, String name) {
		TextField field = new TextField(Resources.getMessage(name));

		Binder<IntegerSettingsBean> binder = new Binder<>();
		binder.forField(field)
				.withConverter(new StringToIntegerConverter(0, Resources.getMessage("error.positiveNumber")))
				.bind(IntegerSettingsBean::getValue, IntegerSettingsBean::setValue);
		binder.setBean(setting);
		binder.addValueChangeListener(ignored -> decideUpdateButtonVisibility());

		field.addStyleName(ValoTheme.TEXTFIELD_TINY);
		addComponent(field);
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

package com.jeno.fantasyleague.backend.data.service.leaguetemplates.worldcup2018;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.jeno.fantasyleague.backend.data.repository.LeagueSettingRepository;
import com.jeno.fantasyleague.backend.data.service.leaguetemplates.LeagueSettingRenderer;
import com.jeno.fantasyleague.backend.model.League;
import com.jeno.fantasyleague.backend.model.LeagueSetting;
import com.jeno.fantasyleague.resources.Resources;
import com.jeno.fantasyleague.ui.common.binder.PositiveDoubleBean;
import com.jeno.fantasyleague.ui.common.field.CustomButton;
import com.jeno.fantasyleague.ui.common.field.NonNullValidator;
import com.jeno.fantasyleague.ui.common.field.StringToPositiveDoubleConverter;
import com.jeno.fantasyleague.ui.common.window.ConfirmationWindow;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import org.springframework.beans.factory.annotation.Autowired;

@org.springframework.stereotype.Component
public class FifaWorldCup2018SettingRenderer implements LeagueSettingRenderer {

	public static final String ALL_CORRECT = "AllCorrect";
	public static final String WRONG_SCORE = "WrongScore";
	public static final String ALL_WRONG = "AllWrong";

	public static final String POWER_INDEX_MULTIPLIER = "PowerIndexMultiplier";

	@Autowired
	private LeagueSettingRepository leagueSettingRepository;

	@Override
	public Component render(League league) {
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(false);

		List<LeagueSetting> allLeagueSettings = leagueSettingRepository.findByLeague(league);

//		addPowerIndexMultiplierField(layout, allLeagueSettings.stream()
//				.filter(setting -> POWER_INDEX_MULTIPLIER.equals(setting.getName()))
//				.findFirst()
//				.get());

		Accordion scorePerPhaseAccordion = new Accordion();

		ArrayListMultimap<FifaWorldCup2018Stages, LeagueSetting> leagueSettingPerGroup =
				FifaWorldCup2018Util.getLeagueSettingsPerStage(allLeagueSettings);
		leagueSettingPerGroup.keySet().stream()
				.sorted(Comparator.comparing(FifaWorldCup2018Stages::getSeq))
				.forEach(stage -> {
					List<IntegerSettingsBean> leagueSettings = leagueSettingPerGroup.get(stage).stream()
							.map(IntegerSettingsBean::new)
							.collect(Collectors.toList());
					SettingsGroupLayout settingsGroupLayout = new SettingsGroupLayout(stage.getName(), leagueSettings);
					settingsGroupLayout.saved().subscribe(leagueSettingRepository::saveAll);
					scorePerPhaseAccordion.add(Resources.getMessage(stage.getName()), settingsGroupLayout);
				});
		layout.add(scorePerPhaseAccordion);

		return layout;
	}

	public void addPowerIndexMultiplierField(VerticalLayout layout, LeagueSetting leagueSetting) {
		CustomButton updateSettingsButton = new CustomButton(Resources.getMessage("updateSettings"), VaadinIcon.USER_CHECK.create());
		updateSettingsButton.setVisible(false);

		PositiveDoubleBean bean = new PositiveDoubleBean(Double.valueOf(leagueSetting.getValue()));
		Binder<PositiveDoubleBean> binder = new Binder<>();
		TextField powerIndexModifier = new TextField();
		powerIndexModifier.setWidth("30px");
		powerIndexModifier.addClassName("v-slot-ignore-error-indicator");
		binder.forField(powerIndexModifier)
				.withNullRepresentation("")
				.withConverter(new StringToPositiveDoubleConverter(null, Resources.getMessage("error.positiveNumber")))
				.withValidator(new NonNullValidator<>(Resources.getMessage("error.cannotBeNull")))
				.bind(PositiveDoubleBean::getNumber, PositiveDoubleBean::setNumber);
//		powerIndexModifier.addClassName(ValoTheme.TEXTFIELD_TINY);
		powerIndexModifier.setWidth(null);
		powerIndexModifier.setLabel(Resources.getMessage("powerIndexMultiplier"));
		binder.addValueChangeListener(ignored ->
			updateSettingsButton.setVisible(binder.validate().isOk()));
		binder.setBean(bean);

		updateSettingsButton.addClickListener(ignored ->
				ConfirmationWindow.showConfirmationPopup(
						Resources.getMessage("areYouSure"),
						Resources.getMessage("willAffectUserResults"),
						aVoid -> {
							leagueSetting.setValue(bean.getNumber().toString());
							LeagueSetting updatedSetting = leagueSettingRepository.saveAndFlush(leagueSetting);
							powerIndexModifier.setValue(updatedSetting.getValue());
							updateSettingsButton.setVisible(false);
						},
						aVoid -> {}));

		layout.add(powerIndexModifier);
		layout.add(updateSettingsButton);
	}

	public void addDefaultLeagueSettings(League newLeague) {
		List<LeagueSetting> labSettings = Lists.newArrayList();

		labSettings.add(createLeagueScoreSettings(newLeague, FifaWorldCup2018Stages.GROUP_PHASE.getName() + ALL_CORRECT, 3));
		labSettings.add(createLeagueScoreSettings(newLeague, FifaWorldCup2018Stages.GROUP_PHASE.getName() + WRONG_SCORE, 2));
		labSettings.add(createLeagueScoreSettings(newLeague, FifaWorldCup2018Stages.GROUP_PHASE.getName() + ALL_WRONG, 0));

		labSettings.add(createLeagueScoreSettings(newLeague, FifaWorldCup2018Stages.EIGHTH_FINALS.getName() + ALL_CORRECT, 6));
		labSettings.add(createLeagueScoreSettings(newLeague, FifaWorldCup2018Stages.EIGHTH_FINALS.getName() + WRONG_SCORE, 4));
		labSettings.add(createLeagueScoreSettings(newLeague, FifaWorldCup2018Stages.EIGHTH_FINALS.getName() + ALL_WRONG, 0));

		labSettings.add(createLeagueScoreSettings(newLeague, FifaWorldCup2018Stages.QUARTER_FINALS.getName() + ALL_CORRECT, 10));
		labSettings.add(createLeagueScoreSettings(newLeague, FifaWorldCup2018Stages.QUARTER_FINALS.getName() + WRONG_SCORE, 7));
		labSettings.add(createLeagueScoreSettings(newLeague, FifaWorldCup2018Stages.QUARTER_FINALS.getName() + ALL_WRONG, 0));

		labSettings.add(createLeagueScoreSettings(newLeague, FifaWorldCup2018Stages.SEMI_FINALS.getName() + ALL_CORRECT, 15));
		labSettings.add(createLeagueScoreSettings(newLeague, FifaWorldCup2018Stages.SEMI_FINALS.getName() + WRONG_SCORE, 10));
		labSettings.add(createLeagueScoreSettings(newLeague, FifaWorldCup2018Stages.SEMI_FINALS.getName() + ALL_WRONG, 0));

		labSettings.add(createLeagueScoreSettings(newLeague, FifaWorldCup2018Stages.FINALS.getName() + ALL_CORRECT, 30));
		labSettings.add(createLeagueScoreSettings(newLeague, FifaWorldCup2018Stages.FINALS.getName() + WRONG_SCORE, 20));
		labSettings.add(createLeagueScoreSettings(newLeague, FifaWorldCup2018Stages.FINALS.getName() + ALL_WRONG, 0));

		labSettings.add(createLeagueScoreSettings(newLeague, POWER_INDEX_MULTIPLIER, 1));

		leagueSettingRepository.saveAll(labSettings);
	}

	private LeagueSetting createLeagueScoreSettings(League newLeague, String name, int value) {
		LeagueSetting correctResultAndScorePoints = new LeagueSetting();
		correctResultAndScorePoints.setName(name);
		correctResultAndScorePoints.setValue(Integer.valueOf(value).toString());
		correctResultAndScorePoints.setLeague(newLeague);
		return correctResultAndScorePoints;
	}

}

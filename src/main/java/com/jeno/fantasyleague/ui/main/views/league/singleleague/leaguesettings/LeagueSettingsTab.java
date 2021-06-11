package com.jeno.fantasyleague.ui.main.views.league.singleleague.leaguesettings;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.google.common.collect.ArrayListMultimap;
import com.jeno.fantasyleague.backend.data.service.leaguetemplates.SoccerCupStages;
import com.jeno.fantasyleague.backend.model.ContestantWeight;
import com.jeno.fantasyleague.backend.model.League;
import com.jeno.fantasyleague.backend.model.LeagueSetting;
import com.jeno.fantasyleague.backend.model.LeagueUser;
import com.jeno.fantasyleague.backend.model.User;
import com.jeno.fantasyleague.resources.Resources;
import com.jeno.fantasyleague.ui.common.field.CustomButton;
import com.jeno.fantasyleague.ui.common.label.StatusLabel;
import com.jeno.fantasyleague.ui.common.tabsheet.LazyTabComponent;
import com.jeno.fantasyleague.ui.common.window.PopupWindow;
import com.jeno.fantasyleague.ui.main.views.league.SingleLeagueServiceProvider;
import com.jeno.fantasyleague.util.DateUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;

public class LeagueSettingsTab extends LazyTabComponent {

	public LeagueSettingsTab(League league, SingleLeagueServiceProvider singleLeagueServiceprovider) {
		super();
		setMargin(false);
		setPadding(false);
		setSizeFull();

		TextField changeLeagueName = new TextField("League name");
		changeLeagueName.setWidth("50%");
		changeLeagueName.setValue(league.getName());
		CustomButton save = new CustomButton("Save", VaadinIcon.CHECK.create());
		StatusLabel infoLabel = new StatusLabel();
		save.addClickListener(ignored -> {
			if (!Objects.equals(league.getName(), changeLeagueName.getValue())) {
				try {
					league.setName(changeLeagueName.getValue());
					singleLeagueServiceprovider.getLeagueRepository().saveAndFlush(league);
					infoLabel.setSuccessText("Changes saved");
				} catch (Exception e) {
					infoLabel.setErrorText("Something went wrong " + e.getMessage());
				}
			}
		});
		HorizontalLayout actions = new HorizontalLayout();
		actions.setMargin(false);
		actions.setPadding(false);
		actions.add(save, infoLabel);
		add(changeLeagueName, actions);

		List<User> leagueUsers = singleLeagueServiceprovider.getLeagueUserRepository().findByLeague(league.getId()).stream().map(LeagueUser::getUser).collect(Collectors.toList());
		Button sendEmailButton = new CustomButton(Resources.getMessage("sendMailToLeagueUsers"), VaadinIcon.MAILBOX.create());
		sendEmailButton.addClickListener(ignored ->
				new SendMailPopupWindow(leagueUsers, singleLeagueServiceprovider.getEmailService()).show());
		add(sendEmailButton);
		add(singleLeagueServiceprovider.getLeagueTemplateServiceBean(league).getLeagueSettingRenderer().render(league));
		CustomButton delete = new CustomButton("Delete league");
		delete.addClickListener(ignored -> {
			PopupWindow window = new PopupWindow.Builder("Confirm", win -> {
						StatusLabel label = new StatusLabel();
						label.setErrorText("Are you sure? This is irreversible.");
						return label;
					})
					.setType(PopupWindow.Type.CONFIRM)
					.sizeUndefined(true)
					.build();
			window.setOnConfirm(new PopupWindow.OnConfirm() {
				@Override
				public boolean onConfirm() {
					singleLeagueServiceprovider.deactivateLeague(league);
					return true;
				}
			});
			window.open();
		});
		add(delete);

		LocalDateTime now = LocalDateTime.now(ZoneId.of("Europe/Brussels"));

		Label currentServerTime = new Label();
		currentServerTime.setText("Current date time: " + DateUtil.DATE_TIME_FORMATTER.format(now));
		add(currentServerTime);

		String diffHours = "Hours left: " + ChronoUnit.HOURS.between(now, league.getLeague_starting_date());
		String minLeftS = "Minutes left: " + ChronoUnit.MINUTES.between(now, league.getLeague_starting_date());
		Label timeLeft = new Label();
		timeLeft.setText(diffHours);
		add(timeLeft);

		Label minLeft = new Label();
		minLeft.setText(minLeftS);
		add(minLeft);

		ArrayListMultimap<Long, ContestantWeight> weightsPerUser = ArrayListMultimap.create();

		singleLeagueServiceprovider.getContestantWeightRepository().findByLeagueAndJoinContestant(league).forEach(cw -> {
			weightsPerUser.put(cw.getUser_fk(), cw);
		});
		List<String> stockStrings = weightsPerUser.asMap().entrySet().stream()
				.map(e -> {
					String userString = singleLeagueServiceprovider.getUserRepository().findById(e.getKey()).map(u -> u.getUsername() + " " + u.getName() + " " + u.getEmail()).orElse("NULL USER");
					int sum = e.getValue().stream()
							.mapToInt(ContestantWeight::getWeight)
							.sum();
					return "Stocks for " + userString + ": " + sum;
				})
				.collect(Collectors.toList());
		stockStrings.forEach(s -> {
			Label ss = new Label();
			ss.setText(s);
			add(ss);
		});
	}

}

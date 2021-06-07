package com.jeno.fantasyleague.ui.main.views.league.singleleague.leaguesettings;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.jeno.fantasyleague.backend.model.League;
import com.jeno.fantasyleague.backend.model.LeagueUser;
import com.jeno.fantasyleague.backend.model.User;
import com.jeno.fantasyleague.resources.Resources;
import com.jeno.fantasyleague.ui.common.field.CustomButton;
import com.jeno.fantasyleague.ui.common.label.StatusLabel;
import com.jeno.fantasyleague.ui.common.tabsheet.LazyTabComponent;
import com.jeno.fantasyleague.ui.common.window.PopupWindow;
import com.jeno.fantasyleague.ui.main.views.league.SingleLeagueServiceProvider;
import com.vaadin.flow.component.button.Button;
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
	}

}

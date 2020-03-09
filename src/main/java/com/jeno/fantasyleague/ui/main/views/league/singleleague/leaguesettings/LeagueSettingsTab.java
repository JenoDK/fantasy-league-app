package com.jeno.fantasyleague.ui.main.views.league.singleleague.leaguesettings;

import java.util.List;

import com.jeno.fantasyleague.backend.model.League;
import com.jeno.fantasyleague.backend.model.User;
import com.jeno.fantasyleague.resources.Resources;
import com.jeno.fantasyleague.ui.common.field.CustomButton;
import com.jeno.fantasyleague.ui.main.views.league.SingleLeagueServiceProvider;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class LeagueSettingsTab extends VerticalLayout {

	public LeagueSettingsTab(League league, SingleLeagueServiceProvider singleLeagueServiceprovider) {
		super();
		setMargin(false);
		setPadding(false);
		setSizeFull();

		List<User> leagueUsers = singleLeagueServiceprovider.getLeagueRepository().fetchLeagueUsers(league.getId());
		Button sendEmailButton = new CustomButton(Resources.getMessage("sendMailToLeagueUsers"), VaadinIcon.MAILBOX.create());
		sendEmailButton.addClickListener(ignored ->
				new SendMailPopupWindow(leagueUsers, singleLeagueServiceprovider.getEmailService()).show());
		add(sendEmailButton);
		add(singleLeagueServiceprovider.getLeagueTemplateServiceBean(league).getLeagueSettingRenderer().render(league));
	}

}

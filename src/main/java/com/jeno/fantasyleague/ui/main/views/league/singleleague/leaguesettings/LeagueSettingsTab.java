package com.jeno.fantasyleague.ui.main.views.league.singleleague.leaguesettings;

import java.util.List;

import com.jeno.fantasyleague.model.League;
import com.jeno.fantasyleague.model.User;
import com.jeno.fantasyleague.resources.Resources;
import com.jeno.fantasyleague.ui.main.views.league.SingleLeagueServiceProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Button;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class LeagueSettingsTab extends VerticalLayout {

	public LeagueSettingsTab(League league, SingleLeagueServiceProvider singleLeagueServiceprovider) {
		super();
		setMargin(true);
		setSizeFull();

		List<User> leagueUsers = singleLeagueServiceprovider.getLeagueRepository().fetchLeagueUsers(league.getId());
		Button sendEmailButton = new Button(Resources.getMessage("sendMailToLeagueUsers"), VaadinIcons.MAILBOX);
		sendEmailButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
		sendEmailButton.addStyleName(ValoTheme.BUTTON_TINY);
		sendEmailButton.addClickListener(ignored ->
				new SendMailPopupWindow(leagueUsers, singleLeagueServiceprovider.getEmailService()).show());
		addComponent(sendEmailButton);
		addComponent(singleLeagueServiceprovider.getLeagueTemplateServiceBean(league).getLeagueSettingRenderer().render(league));
	}

}

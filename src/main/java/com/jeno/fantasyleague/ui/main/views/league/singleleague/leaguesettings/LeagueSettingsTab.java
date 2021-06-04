package com.jeno.fantasyleague.ui.main.views.league.singleleague.leaguesettings;

import java.util.List;
import java.util.stream.Collectors;

import com.jeno.fantasyleague.backend.model.League;
import com.jeno.fantasyleague.backend.model.LeagueUser;
import com.jeno.fantasyleague.backend.model.User;
import com.jeno.fantasyleague.resources.Resources;
import com.jeno.fantasyleague.ui.common.field.CustomButton;
import com.jeno.fantasyleague.ui.common.tabsheet.LazyTabComponent;
import com.jeno.fantasyleague.ui.main.views.league.SingleLeagueServiceProvider;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;

public class LeagueSettingsTab extends LazyTabComponent {

	public LeagueSettingsTab(League league, SingleLeagueServiceProvider singleLeagueServiceprovider) {
		super();
		setMargin(false);
		setPadding(false);
		setSizeFull();

		List<User> leagueUsers = singleLeagueServiceprovider.getLeagueUserRepository().findByLeague(league.getId()).stream().map(LeagueUser::getUser).collect(Collectors.toList());
		Button sendEmailButton = new CustomButton(Resources.getMessage("sendMailToLeagueUsers"), VaadinIcon.MAILBOX.create());
		sendEmailButton.addClickListener(ignored ->
				new SendMailPopupWindow(leagueUsers, singleLeagueServiceprovider.getEmailService()).show());
		add(sendEmailButton);
		add(singleLeagueServiceprovider.getLeagueTemplateServiceBean(league).getLeagueSettingRenderer().render(league));
	}

}

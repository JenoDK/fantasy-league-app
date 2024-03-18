package com.jeno.fantasyleague.ui.main.views.league.singleleague.users;

import com.jeno.fantasyleague.backend.data.service.email.ApplicationEmailService;
import com.jeno.fantasyleague.backend.model.League;
import com.jeno.fantasyleague.backend.model.User;
import com.jeno.fantasyleague.ui.common.field.CustomButton;
import com.jeno.fantasyleague.ui.common.grid.CustomGrid;
import com.jeno.fantasyleague.ui.common.grid.CustomGridBuilder;
import com.jeno.fantasyleague.ui.main.views.league.SingleLeagueServiceProvider;
import com.jeno.fantasyleague.util.LayoutUtil;
import com.jeno.fantasyleague.util.VaadinUtil;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;

public class UserGrid extends CustomGrid<User> {

	public UserGrid(ListDataProvider<User> dataProvider, SingleLeagueServiceProvider singleLeagueServiceProvider, League league) {
		super(getDefaultUserGridBuilder(dataProvider));
	}

	public static Button promoteButton(User user, SingleLeagueServiceProvider singleLeagueServiceProvider, League league) {
		Button promoteDemoteButton = new CustomButton();
		changePromoteDemoteButton(user, singleLeagueServiceProvider, league, promoteDemoteButton);
		promoteDemoteButton.addClickListener(ignored -> {
			if (singleLeagueServiceProvider.userIsLeagueAdmin(league, user)) {
				singleLeagueServiceProvider.demoteUserToLeagueNonOwner(league, user);
			} else {
				singleLeagueServiceProvider.promoteUserToLeagueOwner(league, user);
			}
			changePromoteDemoteButton(user, singleLeagueServiceProvider, league, promoteDemoteButton);
		});
		if (league.getCreatedBy().getId().equals(user.getId())) {
			promoteDemoteButton.setVisible(false);
		}
		return promoteDemoteButton;
	}

	public static Button sendMailButton(User user, SingleLeagueServiceProvider singleLeagueServiceProvider, League league) {
		Button sendMailButton = new CustomButton(VaadinIcon.MAILBOX);
		sendMailButton.addClickListener(ignored -> {
			try {
				ApplicationEmailService emailService = singleLeagueServiceProvider.getEmailService();
				String rootUrl = VaadinUtil.getRootRequestURL();
				emailService.sendEmail(
						"FIFA World Cup 2022 - League Invite",
						"You got invited to participate in the league " + league.getName() + ". Log in to " + rootUrl + " and accept the invite.",
						user);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		return sendMailButton;
	}

	private static void changePromoteDemoteButton(User user, SingleLeagueServiceProvider singleLeagueServiceProvider, League league, Button promoteButton) {
		if (singleLeagueServiceProvider.userIsLeagueAdmin(league, user)) {
			promoteButton.setText("Demote");
			promoteButton.setIcon(VaadinIcon.ARROW_CIRCLE_DOWN.create());
		} else {
			promoteButton.setText("Promote");
			promoteButton.setIcon(VaadinIcon.ARROW_CIRCLE_UP.create());
		}
	}

	public static CustomGridBuilder getDefaultUserGridBuilder(DataProvider<User, ?> dataProvider) {
		return new CustomGridBuilder<>(dataProvider, User::getId)
				.withTextColumn(
						new CustomGridBuilder.ColumnProvider<>(
								"usernameColumn",
								User::getUsername,
								"Username"))
				.withTextColumn(
						new CustomGridBuilder.ColumnProvider<>(
								"nameColumn",
								User::getName,
								"Name"))
				.withTextColumn(
						new CustomGridBuilder.ColumnProvider<>(
								"emailColumn",
								User::getEmail,
								"Email"))
				.withIconColumn(
						new CustomGridBuilder.ColumnProvider<>(
								"iconColumn",
								LayoutUtil::getUserIconColumnValue,
								""))
				.withColumnOrder("iconColumn", "usernameColumn", "nameColumn", "emailColumn");
	}

}

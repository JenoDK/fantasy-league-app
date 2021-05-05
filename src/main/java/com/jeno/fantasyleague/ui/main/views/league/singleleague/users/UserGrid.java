package com.jeno.fantasyleague.ui.main.views.league.singleleague.users;

import com.jeno.fantasyleague.backend.model.League;
import com.jeno.fantasyleague.backend.model.User;
import com.jeno.fantasyleague.ui.common.field.CustomButton;
import com.jeno.fantasyleague.ui.common.grid.CustomGrid;
import com.jeno.fantasyleague.ui.common.grid.CustomGridBuilder;
import com.jeno.fantasyleague.ui.main.views.league.SingleLeagueServiceProvider;
import com.jeno.fantasyleague.util.LayoutUtil;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;

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
				.withIconColumn(
						new CustomGridBuilder.ColumnProvider<>(
								"iconColumn",
								LayoutUtil::getUserIconColumnValue,
								""))
				.withColumnOrder("iconColumn", "usernameColumn");
	}

}

package com.jeno.fantasyleague.ui.main.views.league.singleleague.users;

import com.jeno.fantasyleague.model.League;
import com.jeno.fantasyleague.model.User;
import com.jeno.fantasyleague.ui.common.field.CustomButton;
import com.jeno.fantasyleague.ui.common.grid.CustomGrid;
import com.jeno.fantasyleague.ui.common.grid.CustomGridBuilder;
import com.jeno.fantasyleague.ui.main.views.league.SingleLeagueServiceProvider;
import com.jeno.fantasyleague.util.ImageUtil;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Button;
import com.vaadin.ui.renderers.ComponentRenderer;

public class UserGrid extends CustomGrid<User> {

	public UserGrid(DataProvider<User, ?> dataProvider, SingleLeagueServiceProvider singleLeagueServiceProvider, League league) {
		super(getDefaultUserGridBuilder(dataProvider));
		if (singleLeagueServiceProvider.loggedInUserIsLeagueCreator(league)) {
			addColumn(user -> promoteButton(user, singleLeagueServiceProvider, league), new ComponentRenderer())
					.setWidth(130);
		}
	}

	private Button promoteButton(User user, SingleLeagueServiceProvider singleLeagueServiceProvider, League league) {
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
		promoteDemoteButton.setWidth(100, Unit.PIXELS);
		if (league.getCreatedBy().getId().equals(user.getId())) {
			promoteDemoteButton.setVisible(false);
		}
		return promoteDemoteButton;
	}

	private void changePromoteDemoteButton(User user, SingleLeagueServiceProvider singleLeagueServiceProvider, League league, Button promoteButton) {
		if (singleLeagueServiceProvider.userIsLeagueAdmin(league, user)) {
			promoteButton.setCaption("Demote");
			promoteButton.setIcon(VaadinIcons.ARROW_CIRCLE_DOWN);
		} else {
			promoteButton.setCaption("Promote");
			promoteButton.setIcon(VaadinIcons.ARROW_CIRCLE_UP);
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
						new CustomGridBuilder.ColumnProvider<User, CustomGridBuilder.IconColumnValue>(
								"iconColumn",
								user -> new CustomGridBuilder.IconColumnValue(ImageUtil.getUserProfilePictureResource(user)),
								""))
				.withColumnOrder("iconColumn", "usernameColumn");
	}

}

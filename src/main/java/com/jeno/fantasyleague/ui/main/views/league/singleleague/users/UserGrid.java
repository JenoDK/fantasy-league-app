package com.jeno.fantasyleague.ui.main.views.league.singleleague.users;

import com.jeno.fantasyleague.backend.model.League;
import com.jeno.fantasyleague.backend.model.User;
import com.jeno.fantasyleague.ui.common.field.CustomButton;
import com.jeno.fantasyleague.ui.common.grid.CustomGrid;
import com.jeno.fantasyleague.ui.common.grid.CustomGridBuilder;
import com.jeno.fantasyleague.ui.main.views.league.SingleLeagueServiceProvider;
import com.jeno.fantasyleague.util.ImageUtil;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;

public class UserGrid extends CustomGrid<User> {

	public UserGrid(DataProvider<User, ?> dataProvider, SingleLeagueServiceProvider singleLeagueServiceProvider, League league) {
		super(getDefaultUserGridBuilder(dataProvider));
		if (singleLeagueServiceProvider.loggedInUserIsLeagueCreator(league)) {
			addColumn(new ComponentRenderer<>(user -> promoteButton(user, singleLeagueServiceProvider, league)))
					.setWidth("130px");
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
		promoteDemoteButton.setWidth("100px");
		if (league.getCreatedBy().getId().equals(user.getId())) {
			promoteDemoteButton.setVisible(false);
		}
		return promoteDemoteButton;
	}

	private void changePromoteDemoteButton(User user, SingleLeagueServiceProvider singleLeagueServiceProvider, League league, Button promoteButton) {
		if (singleLeagueServiceProvider.userIsLeagueAdmin(league, user)) {
			promoteButton.setText("Demote");
			promoteButton.setIcon(VaadinIcon.ARROW_CIRCLE_DOWN.create());
		} else {
			promoteButton.setText("Promote");
			promoteButton.setIcon(VaadinIcon.ARROW_CIRCLE_UP.create());
		}
	}

	public static CustomGridBuilder<User> getDefaultUserGridBuilder(DataProvider<User, ?> dataProvider) {
		return new CustomGridBuilder<>(dataProvider, User::getId)
				.withTextColumn(
						new CustomGridBuilder.ColumnProvider<>(
								"usernameColumn",
								User::getUsername,
								"Username"))
				.withIconColumn(
						new CustomGridBuilder.ColumnProvider<User, CustomGridBuilder.IconColumnValue>(
								"iconColumn",
								user -> new CustomGridBuilder.IconColumnValue(ImageUtil.getUserProfilePictureResource((User) user)),
								""))
				.withColumnOrder("iconColumn", "usernameColumn");
	}

}

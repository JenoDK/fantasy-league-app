package com.jeno.fantasyleague.ui.main.views.league.singleleague.users;

import com.jeno.fantasyleague.model.User;
import com.jeno.fantasyleague.ui.common.grid.CustomGrid;
import com.jeno.fantasyleague.ui.common.grid.CustomGridBuilder;
import com.jeno.fantasyleague.util.ImageUtil;
import com.vaadin.data.provider.DataProvider;

public class UserGrid extends CustomGrid<User> {

	public UserGrid(DataProvider<User, ?> dataProvider) {
		super(getDefaultUserGridBuilder(dataProvider));
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

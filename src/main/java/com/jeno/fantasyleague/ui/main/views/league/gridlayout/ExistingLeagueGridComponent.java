package com.jeno.fantasyleague.ui.main.views.league.gridlayout;

import com.jeno.fantasyleague.model.League;
import com.jeno.fantasyleague.util.ImageUtil;
import com.jeno.fantasyleague.util.RxUtil;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import io.reactivex.Observable;

public class ExistingLeagueGridComponent extends AbstractLeagueGridComponent {

	private final League league;

	public ExistingLeagueGridComponent(League league) {
		super();
		this.league = league;

		createLeagueComponent(league);
	}

	private void createLeagueComponent(League league) {
		addStyleName("existing");
		Label name = new Label(league.getName(), ContentMode.HTML);
		addComponent(name);

		Image leagueImage = new Image();
		leagueImage.addStyleName("league-picture");
		leagueImage.setSource(ImageUtil.getLeaguePictureResource(league));
		addComponent(leagueImage);

		setExpandRatio(name, 1);
		setExpandRatio(leagueImage, 4);
	}

	public Observable<League> click() {
		return RxUtil.clicks(this).map(ignored -> league);
	}

}

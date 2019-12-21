package com.jeno.fantasyleague.ui.main.views.league.singleleague;

import java.io.ByteArrayInputStream;

import com.jeno.fantasyleague.backend.model.League;
import com.jeno.fantasyleague.ui.common.image.VaadinImageUploader;
import com.jeno.fantasyleague.util.ImageUtil;
import com.jeno.fantasyleague.util.RxUtil;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.StreamResource;
import io.reactivex.Observable;

public class LeagueTopBar extends VerticalLayout {

	private VaadinImageUploader upload;
	private Image imageLayout;
	private Button backToLeaguesView;
	private Label title;

	public LeagueTopBar(League league) {
		super();
		setMargin(false);
		setSpacing(false);
		setSizeFull();
		addClassName("league-view-topbar");

		upload = new VaadinImageUploader();
		upload.imageResized()
				.subscribe(imageInputStream -> imageLayout.setSrc(new StreamResource(
						upload.getFileName(),
						() -> imageInputStream)));

		upload.addClassName("tiny-upload");
		upload.setHeight("50px");
		upload.addFailedListener(event -> {
			// TODO
		});

		imageLayout = ImageUtil.getLeaguePictureResource(league);
		imageLayout.addClassName("league-picture");

		HorizontalLayout titleLayout = new HorizontalLayout();
		titleLayout.setWidth("100%");

		backToLeaguesView = new Button("Leagues", VaadinIcon.ARROW_CIRCLE_LEFT.create());
		backToLeaguesView.addClassName("back-to-leagues-view");
		title = new Label(league.getName());
//		title.addClassName(ValoTheme.LABEL_H2);

		titleLayout.add(backToLeaguesView);
		titleLayout.add(title);
		titleLayout.add(upload);
//		titleLayout.setComponentAlignment(backToLeaguesView, Alignment.BOTTOM_LEFT);
//		titleLayout.setComponentAlignment(title, Alignment.TOP_CENTER);
//		titleLayout.setComponentAlignment(upload, Alignment.BOTTOM_RIGHT);

		add(titleLayout);
		add(imageLayout);
//		setComponentAlignment(imageLayout, Alignment.MIDDLE_CENTER);
	}

	public Observable<ByteArrayInputStream> imageUploadedAndResized() {
		return upload.imageResized();
	}

	public Observable<ClickEvent<Button>> backToLeagues() {
		return RxUtil.clicks(backToLeaguesView);
	}
}

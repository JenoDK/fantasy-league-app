package com.jeno.fantasyleague.ui.main.views.league.singleleague;

import java.io.File;

import com.jeno.fantasyleague.model.League;
import com.jeno.fantasyleague.ui.common.image.VaadinImageUploader;
import com.jeno.fantasyleague.util.ImageUtil;
import com.jeno.fantasyleague.util.RxUtil;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FileResource;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import io.reactivex.Observable;

public class LeagueTopBar extends VerticalLayout {

	private VaadinImageUploader receiver;
	private Upload upload;
	private Image imageLayout;
	private Button backToLeaguesView;
	private Label title;

	public LeagueTopBar(League league) {
		super();
		setMargin(false);
		setSpacing(false);
		setSizeFull();
		addStyleName("league-view-topbar");

		receiver = new VaadinImageUploader();
		receiver.imageResized()
				.map(FileResource::new)
				.subscribe(fileResource -> imageLayout.setSource(fileResource));

		upload = new Upload("", receiver);
		upload.addStyleName("tiny-upload");
		upload.setHeight(50, Unit.PIXELS);
		upload.addFailedListener((Upload.FailedListener) event -> {
			// TODO
		});
		upload.addStartedListener(receiver);
		upload.addSucceededListener(receiver);

		imageLayout = new Image();
		imageLayout.addStyleName("league-picture");
		imageLayout.setSource(ImageUtil.getLeaguePictureResource(league));

		HorizontalLayout titleLayout = new HorizontalLayout();
		titleLayout.setWidth(100, Unit.PERCENTAGE);

		backToLeaguesView = new Button("Leagues", VaadinIcons.ARROW_CIRCLE_LEFT);
		backToLeaguesView.addStyleName("back-to-leagues-view");
		title = new Label(league.getName(), ContentMode.HTML);
		title.addStyleName(ValoTheme.LABEL_H2);

		titleLayout.addComponent(backToLeaguesView);
		titleLayout.addComponent(title);
		titleLayout.addComponent(upload);
		titleLayout.setComponentAlignment(backToLeaguesView, Alignment.BOTTOM_LEFT);
		titleLayout.setComponentAlignment(title, Alignment.TOP_CENTER);
		titleLayout.setComponentAlignment(upload, Alignment.BOTTOM_RIGHT);

		addComponent(titleLayout);
		addComponent(imageLayout);
		setComponentAlignment(imageLayout, Alignment.MIDDLE_CENTER);
	}

	public Observable<File> imageUploadedAndResized() {
		return receiver.imageResized();
	}

	public Observable<Button.ClickEvent> backToLEagues() {
		return RxUtil.clicks(backToLeaguesView);
	}
}

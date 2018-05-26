package com.jeno.fantasyleague.ui.main.views.league.singleleague;

import java.io.File;

import com.jeno.fantasyleague.model.League;
import com.jeno.fantasyleague.ui.common.image.VaadinImageUploader;
import com.jeno.fantasyleague.util.ImageUtil;
import com.vaadin.server.FileResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Upload;
import io.reactivex.Observable;

public class LeaguePictureUploadLayout extends HorizontalLayout {

	private VaadinImageUploader receiver;
	private Upload upload;
	private Image imageLayout;

	public LeaguePictureUploadLayout(League league) {
		super();
		setMargin(false);
		setSpacing(false);

		setSizeFull();

		int width = (int) getWidth();
		int height = (int) getHeight();
		receiver = new VaadinImageUploader(width, height);
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
		imageLayout.setSizeFull();
		imageLayout.setSource(ImageUtil.getLeaguePictureResource(league));

		addComponent(upload);
		addComponent(imageLayout);
		setComponentAlignment(upload, Alignment.TOP_LEFT);
		setExpandRatio(upload, 1);
		setExpandRatio(imageLayout, 19);
	}

	public Observable<File> imageUploadedAndResized() {
		return receiver.imageResized();
	}
}

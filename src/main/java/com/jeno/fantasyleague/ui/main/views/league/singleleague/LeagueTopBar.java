package com.jeno.fantasyleague.ui.main.views.league.singleleague;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import com.jeno.fantasyleague.backend.model.League;
import com.jeno.fantasyleague.ui.common.field.CustomButton;
import com.jeno.fantasyleague.ui.common.image.VaadinImageUploader;
import com.jeno.fantasyleague.util.ImageUtil;
import com.jeno.fantasyleague.util.VaadinUtil;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.StreamResource;

import io.reactivex.Observable;

public class LeagueTopBar extends VerticalLayout {

	private VaadinImageUploader upload;
	private Image imageLayout;

	public LeagueTopBar(League league) {
		super();
		setMargin(false);
		setPadding(false);
		setSpacing(false);
		setSizeFull();

		upload = new VaadinImageUploader();
		CustomButton button = new CustomButton("Change banner", VaadinIcon.CAMERA.create());
		upload.setUploadButton(button);
		upload.setDropAllowed(false);
		upload.imageResized()
				.subscribe(os -> imageLayout.setSrc(new StreamResource(
						upload.getFileName(),
						() -> new ByteArrayInputStream(os.toByteArray()))));

		imageLayout = ImageUtil.getLeaguePictureImage(league);
		VaadinUtil.addStyles(imageLayout.getStyle(), "width: 100%; max-height: 200px; object-fit: cover;");

		HorizontalLayout titleLayout = new HorizontalLayout();
		titleLayout.setPadding(false);
		titleLayout.setMargin(false);
		titleLayout.setWidthFull();

		H3 title = new H3(league.getName());
		title.getStyle().set("flex", "1");
		title.getStyle().set("margin-top", "0.5em");

		titleLayout.add(title);
		titleLayout.add(upload);
		titleLayout.setAlignItems(Alignment.CENTER);
		titleLayout.setJustifyContentMode(JustifyContentMode.BETWEEN);

		add(titleLayout);
		add(imageLayout);
		setAlignItems(Alignment.CENTER);
	}

	public Observable<ByteArrayOutputStream> imageUploaded() {
		return upload.imageResized();
	}

}

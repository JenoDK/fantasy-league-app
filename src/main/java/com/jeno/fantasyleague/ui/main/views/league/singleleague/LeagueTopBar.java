package com.jeno.fantasyleague.ui.main.views.league.singleleague;

import com.jeno.fantasyleague.backend.model.League;
import com.jeno.fantasyleague.ui.common.LeagueImageResourceCache;
import com.jeno.fantasyleague.ui.common.image.VaadinImageUploader;
import com.jeno.fantasyleague.ui.common.label.StatusLabel;
import com.jeno.fantasyleague.util.ImageUtil;
import com.jeno.fantasyleague.util.VaadinUtil;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import io.reactivex.Observable;

import java.io.ByteArrayOutputStream;

public class LeagueTopBar extends VerticalLayout {

	private VaadinImageUploader upload;
	private Image imageLayout;

	public LeagueTopBar(League league) {
		super();
		setMargin(false);
		setPadding(false);
		setSpacing(false);
		setSizeFull();

		VerticalLayout imageUploadLayout = new VerticalLayout();
		imageUploadLayout.setMargin(false);
		imageUploadLayout.setPadding(false);
		imageUploadLayout.setAlignItems(Alignment.END);
		StatusLabel statusLabel = new StatusLabel();
		statusLabel.setVisible(false);
		upload = new VaadinImageUploader(1200, 300, true, false, statusLabel);
		Button button = new Button("Change banner", VaadinIcon.CAMERA.create());
		button.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_SMALL);
		upload.setUploadButton(button);
		upload.setDropAllowed(false);
		imageUploadLayout.add(statusLabel, upload);
		imageLayout = ImageUtil.getLeaguePictureImage(league);
		VaadinUtil.addStyles(imageLayout.getStyle(), "width: 100%; max-height: 300px; object-fit: cover;");

		HorizontalLayout titleLayout = new HorizontalLayout();
		titleLayout.setPadding(false);
		titleLayout.setMargin(false);
		titleLayout.setWidthFull();

		H3 title = new H3(league.getName());
		title.getStyle().set("flex", "1");
		title.getStyle().set("margin-top", "0.5em");
		VerticalLayout tL = new VerticalLayout(title);
		tL.setSpacing(false);
		tL.setPadding(false);
		tL.setMargin(false);
		tL.setWidthFull();

		titleLayout.add(tL);
		titleLayout.add(imageUploadLayout);
		titleLayout.setAlignItems(Alignment.CENTER);
		titleLayout.setJustifyContentMode(JustifyContentMode.BETWEEN);

		add(titleLayout);
		add(imageLayout);
		setAlignItems(Alignment.CENTER);
	}

	public Observable<ByteArrayOutputStream> imageUploaded() {
		return upload.imageResized();
	}

	public void updateLeagueImage(League league) {
		imageLayout.setSrc(LeagueImageResourceCache.addOrGetLeagueImageResource(league));
	}
}

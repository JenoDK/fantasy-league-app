package com.jeno.fantasyleague.ui.common.image;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.Optional;

import com.jeno.fantasyleague.backend.model.User;
import com.jeno.fantasyleague.util.ImageUtil;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.NativeButton;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.StreamResource;
import io.reactivex.Observable;

public class ImageUploadWithPlaceholder extends VerticalLayout {

	private VaadinImageUploader upload;
	private Image imageLayout;
	private Label errorLabel;

	private Optional<File> currentImage = Optional.empty();

	public ImageUploadWithPlaceholder() {
		super();
		initLayout();
	}

	public ImageUploadWithPlaceholder(User user) {
		this();
		imageLayout.setSrc(ImageUtil.getUserProfilePictureResource(user));
	}

	private void initLayout() {
		imageLayout = new Image();

		upload = new VaadinImageUploader(100);
		upload.imageResized()
				.subscribe(imageInputStream -> {
					remove(errorLabel);
					imageLayout.setSrc(new StreamResource(
							upload.getFileName(),
							() -> imageInputStream));
					add(imageLayout);
				});
		upload.addFailedListener(event -> {
			remove(imageLayout);
			if (event.getReason().getCause() == null) {
				errorLabel.setText(event.getReason().getMessage());
			} else {
				errorLabel.setText(event.getReason().getCause().getMessage());
			}
			add(errorLabel);
			currentImage = Optional.empty();
		});
		NativeButton uploadButton = new NativeButton("Choose image");
		uploadButton.addComponentAsFirst(VaadinIcon.PICTURE.create());
		upload.setUploadButton(uploadButton);
		upload.addClassName("ignore-error-indicator");

		Label infoLabel = new Label("Rectangular (or close to) will have the best results");

		errorLabel = new Label();

		add(upload);
		add(infoLabel);
		add(imageLayout);
	}

	public Optional<File> getImage() {
		return currentImage;
	}

	public Observable<ByteArrayInputStream> imageUploadedAndResized() {
		return upload.imageResized();
	}

}

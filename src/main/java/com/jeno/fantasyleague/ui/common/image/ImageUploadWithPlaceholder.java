package com.jeno.fantasyleague.ui.common.image;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Optional;

import com.jeno.fantasyleague.ui.common.field.CustomButton;
import com.jeno.fantasyleague.ui.common.label.StatusLabel;
import com.jeno.fantasyleague.util.Images;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.StreamResource;

import io.reactivex.Observable;

public class ImageUploadWithPlaceholder extends VerticalLayout {

	private VaadinImageUploader upload;
	private Image imageLayout;
	private StatusLabel errorLabel;

	public ImageUploadWithPlaceholder() {
		super();
		initLayout();
	}

	private void initLayout() {
		imageLayout = new Image();
		imageLayout.setSrc(Images.DEFAULT_PROFILE_PICTURE);

		errorLabel = new StatusLabel();
		upload = new VaadinImageUploader(200, errorLabel);
		upload.imageResized()
				.subscribe(this::updateImage);
		CustomButton uploadButton = new CustomButton("Choose image", VaadinIcon.PICTURE.create());
		upload.setUploadButton(uploadButton);
		upload.setDropAllowed(false);

		add(upload);
		add(errorLabel);
		add(imageLayout);
		setAlignItems(Alignment.CENTER);
	}

	private void updateImage(ByteArrayOutputStream os) {
		errorLabel.reset();
		imageLayout.setSrc(
				new StreamResource(upload.getFileName(), () -> new ByteArrayInputStream(os.toByteArray())));
	}

	public void updateImage(StreamResource pictureResource) {
		imageLayout.setSrc(pictureResource);
	}

	public Optional<ByteArrayInputStream> getImage() {
		return upload.currentValue();
	}

	public Observable<ByteArrayOutputStream> imageUploadedAndResized() {
		return upload.imageResized();
	}

}

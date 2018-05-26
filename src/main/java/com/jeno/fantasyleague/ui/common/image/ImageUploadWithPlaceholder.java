package com.jeno.fantasyleague.ui.common.image;

import java.io.File;
import java.util.Optional;

import com.jeno.fantasyleague.model.User;
import com.jeno.fantasyleague.util.ImageUtil;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FileResource;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import io.reactivex.Observable;

public class ImageUploadWithPlaceholder extends VerticalLayout {

	private Upload upload;
	private Image imageLayout;
	private VaadinImageUploader receiver;
	private Label errorLabel;

	private Optional<File> currentImage = Optional.empty();

	public ImageUploadWithPlaceholder() {
		super();
		initLayout();
	}

	public ImageUploadWithPlaceholder(User user) {
		this();
		imageLayout.setSource(ImageUtil.getUserProfilePictureResource(user));
	}

	private void initLayout() {
		imageLayout = new Image();

		receiver = new VaadinImageUploader(100);
		receiver.imageResized()
				.map(image -> {
					currentImage = Optional.ofNullable(image);
					return new FileResource(image);
				})
				.subscribe(fileResource -> {
					removeComponent(errorLabel);
					imageLayout.setSource(fileResource);
					addComponent(imageLayout);
				});

		upload = new Upload("Upload profile picture", receiver);
		upload.addFailedListener((Upload.FailedListener) event -> {
			removeComponent(imageLayout);
			if (event.getReason().getCause() == null) {
				errorLabel.setValue(event.getReason().getMessage());
			} else {
				errorLabel.setValue(event.getReason().getCause().getMessage());
			}
			addComponent(errorLabel);
			currentImage = Optional.empty();
		});
		upload.addStartedListener(receiver);
		upload.setButtonCaption("Choose image");
		upload.setIcon(VaadinIcons.PICTURE);
		upload.addSucceededListener(receiver);
		upload.addStyleName("ignore-error-indicator");

		Label infoLabel = new Label("Rectangular (or close to) will have the best results", ContentMode.HTML);
		infoLabel.addStyleName(ValoTheme.LABEL_TINY);

		errorLabel = new Label();
		errorLabel.addStyleName(ValoTheme.LABEL_FAILURE);

		addComponent(upload);
		addComponent(infoLabel);
		addComponent(imageLayout);
	}

	public Optional<File> getImage() {
		return currentImage;
	}

	public Observable<File> imageUploadedAndResized() {
		return receiver.imageResized();
	}

}

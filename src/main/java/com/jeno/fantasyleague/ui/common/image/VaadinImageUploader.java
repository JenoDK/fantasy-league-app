package com.jeno.fantasyleague.ui.common.image;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Set;

import javax.imageio.ImageIO;

import com.google.common.collect.Sets;
import com.jeno.fantasyleague.ui.common.label.StatusLabel;
import com.jeno.fantasyleague.util.ImageUtil;
import com.vaadin.flow.component.upload.SucceededEvent;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.FileBuffer;

import elemental.json.Json;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

public class VaadinImageUploader extends Upload {

	private static final Set<String> ALLOWED_TYPES = Sets.newHashSet("image/*");

	private final BehaviorSubject<ByteArrayOutputStream> imageResized = BehaviorSubject.create();
	private final int maxWidthOfResizedPicture;
	private final int maxHeightOfResizedPicture;
	private final boolean needsResize;
	private final boolean circle;
	private final StatusLabel statusLabel;

	private String fileName = "";

	public VaadinImageUploader(int maxSizeOfResizedPicture, StatusLabel statusLabel) {
		this(maxSizeOfResizedPicture, maxSizeOfResizedPicture, true, true, statusLabel);
	}

	public VaadinImageUploader(int maxWidthOfResizedPicture, int maxHeightOfResizedPicture, boolean needsResize, boolean circle, StatusLabel statusLabel) {
		super();
		this.maxWidthOfResizedPicture = maxWidthOfResizedPicture;
		this.maxHeightOfResizedPicture = maxHeightOfResizedPicture;
		this.needsResize = needsResize;
		this.circle = circle;
		this.statusLabel = statusLabel;

		init();
	}

	private void init() {
		FileBuffer fileBuffer = new FileBuffer();
		setReceiver(fileBuffer);
		setAcceptedFileTypes(ALLOWED_TYPES.toArray(new String[]{}));
		// You can use the capture html5 attribute
		// https://caniuse.com/html-media-capture
		getElement().setAttribute("capture", "environment");
		setMaxFileSize(20000000);
		addStartedListener(event -> statusLabel.setVisible(false));
		addSucceededListener(event -> uploadFinished(event, fileBuffer));
		addFileRejectedListener(event -> {
			statusLabel.setVisible(true);
			statusLabel.setErrorText(event.getErrorMessage());
		});
	}

	private void uploadFinished(SucceededEvent event, FileBuffer fileBuffer) {
		try(InputStream in = fileBuffer.getInputStream()) {
			BufferedImage image = ImageIO.read(in);
			if (needsResize) {
				image = ImageUtil.resizeImage(image, maxWidthOfResizedPicture, maxHeightOfResizedPicture);
			}
			if (circle) {
				image = ImageUtil.cropToCircleShaped(image, maxWidthOfResizedPicture);
			}
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			ImageIO.write(image, "PNG", os);
			fileName = event.getFileName();
			imageResized.onNext(os);
			setFiles(Json.createArray());
		} catch (IOException e) {
			throw new ImageUploadException("Could not rescale image", e);
		}
	}

	public Observable<ByteArrayOutputStream> imageResized() {
		return imageResized;
	}

	public Optional<ByteArrayInputStream> currentValue() {
		if (imageResized.hasValue()) {
			return Optional.of(new ByteArrayInputStream(imageResized.getValue().toByteArray()));
		} else {
			return Optional.empty();
		}
	}

	public String getFileName() {
		return fileName;
	}
}

package com.jeno.fantasyleague.ui.common.image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

import javax.imageio.ImageIO;

import com.google.common.collect.Sets;
import com.jeno.fantasyleague.util.ImageUtil;
import com.vaadin.ui.Upload;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

public class VaadinImageUploader implements Upload.Receiver, Upload.SucceededListener, Upload.StartedListener {

	private static final Set<String> ALLOWED_TYPES = Sets.newHashSet("image/png", "image/jpeg", "image/gif");
	private static final long MAX_SIZE_IN_KB = 10000000;

	private final BehaviorSubject<File> imageResized = BehaviorSubject.create();
	private final int maxWidthOfResizedPicture;
	private final int maxHeightOfResizedPicture;
	private final boolean circle;

	private File file;

	public VaadinImageUploader(int maxSizeOfResizedPicture) {
		this.maxWidthOfResizedPicture = maxSizeOfResizedPicture;
		this.maxHeightOfResizedPicture = maxSizeOfResizedPicture;
		this.circle = true;
	}

	public VaadinImageUploader(int maxWidthOfResizedPicture, int maxHeightOfResizedPicture) {
		this.maxWidthOfResizedPicture = maxWidthOfResizedPicture;
		this.maxHeightOfResizedPicture = maxHeightOfResizedPicture;
		this.circle = false;
	}

	@Override
	public OutputStream receiveUpload(String filename,
	                                  String mimeType) {
		try {
			Path tempDirectory = Files.createTempDirectory("vaadin");
			file = new File(tempDirectory.toFile(), filename);
			return new FileOutputStream(file, false);
		} catch (IOException e) {
			throw new ImageUploadException("File not found", e);
		}
	}

	@Override
	public void uploadSucceeded(Upload.SucceededEvent event) {
		try {
			BufferedImage resized = ImageUtil.resizeImage(ImageIO.read(file), maxWidthOfResizedPicture, maxHeightOfResizedPicture);
			if (circle) {
				resized = ImageUtil.cropToCircleShaped(resized, maxWidthOfResizedPicture);
			}
			ImageIO.write(resized, "PNG", file);
		} catch (IOException e) {
			throw new ImageUploadException("Could not rescale image", e);
		}
		imageResized.onNext(file);
	}

	@Override
	public void uploadStarted(Upload.StartedEvent event) {
		if (event.getContentLength() > MAX_SIZE_IN_KB) {
			throw new ImageUploadException("File is to big, maximum is 10MB");
		}
		if (!ALLOWED_TYPES.contains(event.getMIMEType())) {
			throw new ImageUploadException("File type not allowed");
		}
	}

	public Observable<File> imageResized() {
		return imageResized;
	}

}

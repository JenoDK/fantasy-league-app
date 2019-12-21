package com.jeno.fantasyleague.ui.common.image;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Set;

import javax.imageio.ImageIO;

import com.google.common.collect.Sets;
import com.jeno.fantasyleague.util.ImageUtil;
import com.vaadin.flow.component.upload.StartedEvent;
import com.vaadin.flow.component.upload.SucceededEvent;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.FileBuffer;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

public class VaadinImageUploader extends Upload {

	private static final Set<String> ALLOWED_TYPES = Sets.newHashSet("image/png", "image/jpeg", "image/gif");
	private static final long MAX_SIZE_IN_KB = 10000000;

	private final BehaviorSubject<ByteArrayInputStream> imageResized = BehaviorSubject.create();
	private final int maxWidthOfResizedPicture;
	private final int maxHeightOfResizedPicture;
	private final boolean needsResize;
	private final boolean circle;

	private static FileBuffer fileBuffer = new FileBuffer();

	private String fileName = "";

	public VaadinImageUploader() {
		this(0, 0, false, false);
	}

	public VaadinImageUploader(int maxSizeOfResizedPicture) {
		this(maxSizeOfResizedPicture, maxSizeOfResizedPicture, true, true);
	}

	public VaadinImageUploader(int maxWidthOfResizedPicture, int maxHeightOfResizedPicture, boolean needsResize, boolean circle) {
		super(fileBuffer);
		this.maxWidthOfResizedPicture = maxWidthOfResizedPicture;
		this.maxHeightOfResizedPicture = maxHeightOfResizedPicture;
		this.needsResize = needsResize;
		this.circle = circle;

		init();
	}

	private void init() {
		addStartedListener(this::uploadStarted);
		addSucceededListener(this::uploadSucceeded);
	}

	private void uploadStarted(StartedEvent event) {
		if (event.getContentLength() > MAX_SIZE_IN_KB) {
			throw new ImageUploadException("File is to big, maximum is 10MB");
		}
		if (!ALLOWED_TYPES.contains(event.getMIMEType())) {
			throw new ImageUploadException("File type not allowed");
		}
	}

	private void uploadSucceeded(SucceededEvent event) {
		try {
			BufferedImage image = ImageIO.read(fileBuffer.getInputStream());
			if (needsResize) {
				image = ImageUtil.resizeImage(image, maxWidthOfResizedPicture, maxHeightOfResizedPicture);
			}
			if (circle) {
				image = ImageUtil.cropToCircleShaped(image, maxWidthOfResizedPicture);
			}
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			ImageIO.write(image, "PNG", os);
			fileName = event.getFileName();
			imageResized.onNext(new ByteArrayInputStream(os.toByteArray()));
		} catch (IOException e) {
			throw new ImageUploadException("Could not rescale image", e);
		}
	}

	public Observable<ByteArrayInputStream> imageResized() {
		return imageResized;
	}

	public String getFileName() {
		return fileName;
	}
}

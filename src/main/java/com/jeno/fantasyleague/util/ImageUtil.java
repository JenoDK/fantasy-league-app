package com.jeno.fantasyleague.util;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.Optional;

import com.jeno.fantasyleague.backend.model.League;
import com.jeno.fantasyleague.backend.model.User;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.server.StreamResource;

public class ImageUtil {

	public static Optional<StreamResource> getUserProfilePictureResource(User user) {
		if (user.getProfile_picture() != null) {
			return Optional.of(new StreamResource(
					"profile_picture.png",
					() -> new ByteArrayInputStream(user.getProfile_picture())));
		} else {
			return Optional.empty();
		}
	}

	public static Image createProfileIcon(User user) {
		Optional<StreamResource> userProfilePic = ImageUtil.getUserProfilePictureResource(user);
		Image icon = new Image();
		icon.setWidth("50px");
		icon.setHeight("50px");
		if (userProfilePic.isPresent()) {
			icon.setSrc(userProfilePic.get());
		} else {
			icon.setSrc(Images.DEFAULT_PROFILE_PICTURE);
		}
		return icon;
	}

	public static Image getLeaguePictureImage(League league) {
		if (league.getLeague_picture() != null) {
			StreamResource resource = new StreamResource(
					"league_banner.png",
					() -> new ByteArrayInputStream(league.getLeague_picture()));
			return new Image(resource, "league_banner");
		} else {
			return new Image(Images.DEFAULT_LEAGUE_BANNER, "league_banner");
		}
	}

	/**
	 * This method serves to put the dynamic image within the same servlet.
	 * See <a href="https://vaadin.com/docs/v14/flow/advanced/tutorial-dynamic-content.html">the vaadin documentation</a> for more info
	 * @param image
	 */
	public static void generateDynamicContent(Image image) {
		image.getElement().getStyle().set("display", "none");
		UI.getCurrent().getElement().appendChild(image.getElement());
	}

	/**
	 * Resizes one dimension to one within the boundry while maintaining aspect ratio
	 * @param image original image
	 * @param areaWidth max areaWidth
	 * @param areaHeight max areaHeight
	 * @return BufferedImage the resized image
	 */
	public static BufferedImage resizeImage(BufferedImage image, int areaWidth, int areaHeight) {
		float scaleX = (float) areaWidth / image.getWidth();
		float scaleY = (float) areaHeight / image.getHeight();
		float scale = Math.max(scaleX, scaleY);
		int w = Math.round(image.getWidth() * scale);
		int h = Math.round(image.getHeight() * scale);

		int type = image.getTransparency() == Transparency.OPAQUE ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;

		boolean scaleDown = scale < 1;

		if (scaleDown) {
			// multi-pass bilinear div 2
			int currentW = image.getWidth();
			int currentH = image.getHeight();
			BufferedImage resized = image;
			while (currentW > w || currentH > h) {
				currentW = Math.max(w, currentW / 2);
				currentH = Math.max(h, currentH / 2);

				BufferedImage temp = new BufferedImage(currentW, currentH, type);
				Graphics2D g2 = temp.createGraphics();
				g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
				g2.drawImage(resized, 0, 0, currentW, currentH, null);
				g2.dispose();
				resized = temp;
			}
			return resized;
		} else {
			Object hint = scale > 2 ? RenderingHints.VALUE_INTERPOLATION_BICUBIC : RenderingHints.VALUE_INTERPOLATION_BILINEAR;

			BufferedImage resized = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2 = resized.createGraphics();
			g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, hint);
			g2.drawImage(image, 0, 0, w, h, null);
			g2.dispose();
			return resized;
		}
	}

	/**
	 * Crops an image to a circle
	 * @param image
	 * @return the cropped image
	 */
	public static BufferedImage cropToCircleShaped(BufferedImage image, int w) {
		BufferedImage output = new BufferedImage(w, w, BufferedImage.TYPE_INT_ARGB);

		Graphics2D g2 = output.createGraphics();

		g2.setComposite(AlphaComposite.Src);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(Color.WHITE);
		g2.fill(new Ellipse2D.Double(0, 0, w, w));
		g2.setComposite(AlphaComposite.SrcAtop);
		g2.drawImage(image, 0, 0, null);

		g2.dispose();

		return output;
	}

}

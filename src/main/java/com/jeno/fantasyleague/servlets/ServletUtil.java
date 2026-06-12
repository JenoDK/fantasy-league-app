package com.jeno.fantasyleague.servlets;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

final class ServletUtil {

	private ServletUtil() {
	}

	static Long parseIdParameter(HttpServletRequest req, String name) {
		try {
			return Long.valueOf(req.getParameter(name));
		} catch (NumberFormatException e) {
			return null;
		}
	}

	static void writePng(HttpServletResponse resp, byte[] picture) throws IOException {
		// Uploads are always re-encoded as PNG by VaadinImageUploader.
		resp.setContentType("image/png");
		resp.setContentLength(picture.length);
		// URLs carry a ?v= cache-buster derived from updated_at, so cache aggressively.
		resp.setHeader("Cache-Control", "public, max-age=31536000, immutable");
		resp.getOutputStream().write(picture);
	}
}

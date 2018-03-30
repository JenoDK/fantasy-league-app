package com.jeno.fantasyleague.data.service.leaguetemplates;

public class TemplateException extends RuntimeException {

	public TemplateException(String message) {
		super(message);
	}

	public TemplateException(String message, Throwable cause) {
		super(message, cause);
	}

	public TemplateException(Throwable cause) {
		super(cause);
	}
}

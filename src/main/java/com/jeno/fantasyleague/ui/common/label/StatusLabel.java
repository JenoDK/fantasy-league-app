package com.jeno.fantasyleague.ui.common.label;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Label;

@CssImport("./styles/shared-styles.css")
@CssImport("./styles/status-label-styles.css")
public class StatusLabel extends Label {

	private static final String SUCCESS = "success";
	private static final String WARNING = "warning";
	private static final String ERROR = "error";
	private static final String INFO = "info";
	private final boolean showIcon;

	public StatusLabel() {
		this(true);
	}

	public StatusLabel(boolean showIcon) {
		super();
		this.showIcon = showIcon;
		if (showIcon) {
			addClassName("show-icon");
		}
	}

	public void reset() {
		setText("");
		removeClassNames(SUCCESS, ERROR, WARNING);
	}

	public void setInfoText(String text) {
		setText(text);
		setInfoStyle();
	}

	public void setInfoStyle() {
		setStyle(INFO);
	}

	public void setSuccessText(String text) {
		setText(text);
		setSuccessStyle();
	}

	public void setErrorText(String text) {
		setText(text);
		setErrorStyle();
	}

	public void setSuccessStyle() {
		setStyle(SUCCESS);
	}

	public void setErrorStyle() {
		setStyle(ERROR);
	}

	public void setWarningStyle() {
		setStyle(WARNING);
	}

	private void setStyle(String style) {
		removeClassNames(getClassNames().toArray(String[]::new));
		addClassName(style);
		if (showIcon) {
			addClassName("show-icon");
		}
	}

}

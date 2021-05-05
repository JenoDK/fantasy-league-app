package com.jeno.fantasyleague.ui.common.label;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Label;

@CssImport("./styles/shared-styles.css")
@CssImport("./styles/status-label-styles.css")
public class StatusLabel extends Label {

	private static final String SUCCESS = "success";
	private static final String WARNING = "warning";
	private static final String ERROR = "error";

	public StatusLabel() {
		this(true);
	}

	public StatusLabel(boolean showIcon) {
		super();
		if (showIcon) {
			addClassName("show-icon");
		}
	}

	public void reset() {
		setText("");
		removeClassNames(SUCCESS, ERROR, WARNING);
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
		removeClassNames(ERROR, WARNING);
		addClassName(SUCCESS);
	}

	public void setErrorStyle() {
		removeClassNames(SUCCESS, WARNING);
		addClassName(ERROR);
	}

	public void setWarningStyle() {
		removeClassNames(SUCCESS, ERROR);
		addClassName(WARNING);
	}

}

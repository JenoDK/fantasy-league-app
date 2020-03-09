package com.jeno.fantasyleague.ui.common.label;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.templatemodel.TemplateModel;

@Tag("status-label")
@JsModule("./src/components/status-label.js")
public class StatusLabel extends PolymerTemplate<TemplateModel> {

	private static final String SUCCESS = "success";
	private static final String WARNING = "warning";
	private static final String ERROR = "error";

	@Id("label")
	private Label label;

	public StatusLabel() {
		this(true);
	}

	public StatusLabel(boolean showIcon) {
		super();
		if (showIcon) {
			label.addClassName("show-icon");
		}
	}

	public void reset() {
		label.setText("");
		label.removeClassNames(SUCCESS, ERROR, WARNING);
	}

	public Label getLabel() {
		return label;
	}

	public void setText(String text) {
		label.setText(text);
	}

	public void setSuccessText(String text) {
		label.setText(text);
		setSuccessStyle();
	}

	public void setErrorText(String text) {
		label.setText(text);
		setErrorStyle();
	}

	public void setSuccessStyle() {
		label.removeClassNames(ERROR, WARNING);
		label.addClassName(SUCCESS);
	}

	public void setErrorStyle() {
		label.removeClassNames(SUCCESS, WARNING);
		label.addClassName(ERROR);
	}

	public void setWarningStyle() {
		label.removeClassNames(SUCCESS, ERROR);
		label.addClassName(WARNING);
	}

}

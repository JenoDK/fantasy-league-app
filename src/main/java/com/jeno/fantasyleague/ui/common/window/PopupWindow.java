package com.jeno.fantasyleague.ui.common.window;

import java.util.function.Function;

import com.jeno.fantasyleague.ui.common.field.CustomButton;
import com.jeno.fantasyleague.util.VaadinUtil;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class PopupWindow extends Dialog {

	private final Builder builder;

	private OnConfirm onConfirm;

	public PopupWindow(Builder builder) {
		super();
		this.builder = builder;

		// Size config
		if(builder.sizeUndefined){
			setSizeUndefined();
		} else {
			if (builder.heightUndefined) {
				setHeight(null);
			} else {
				setHeight(builder.pixelHeight + "px");
			}
			setWidth(builder.pixelWidth + "px");
		}
		getElement().getThemeList().add("crud");

		VerticalLayout root = new VerticalLayout();
		root.setSizeFull();
		root.setMargin(false);
		root.setSpacing(false);
		root.setPadding(false);
		root.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);

		root.add(builder.contentGenerator.apply(this));

		HorizontalLayout bottomBar = new HorizontalLayout();
		bottomBar.setSpacing(false);
		bottomBar.setPadding(false);
		bottomBar.setHeight(null);
		bottomBar.setWidthFull();
		VaadinUtil.addStyles(bottomBar.getStyle(),
				"padding: var(--lumo-space-xs);\n" +
				"background-color: var(--lumo-contrast-5pct);");
		bottomBar.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
		bottomBar.setAlignItems(FlexComponent.Alignment.END);
		switch(builder.type) {
			case ALERT:
				CustomButton ok = new CustomButton("OK");
				ok.addClickListener(ignored -> close());
				bottomBar.add(ok);
				break;
			case CONFIRM:
				CustomButton confirm = new CustomButton("Confirm");
				confirm.addThemeName("primary");
				confirm.addClickListener(ignored -> {
					if (onConfirm.onConfirm()) {
						close();
					}
				});
				CustomButton cancel = new CustomButton("Cancel");
				cancel.addThemeName("tertiary");
				cancel.addClickListener(ignored -> close());
				bottomBar.add(cancel, confirm);
				break;
		}
		root.add(bottomBar);

		add(root);
	}

	public void setOnConfirm(OnConfirm onConfirm) {
		this.onConfirm = onConfirm;
	}

	public static class Builder {

		private float pixelHeight = 200;
		private float pixelWidth = 300;
		private boolean sizeUndefined = false;
		private boolean heightUndefined = false;
		private Type type;

		private final String caption;
		private final Function<PopupWindow, Component> contentGenerator;

		public Builder(String caption, Function<PopupWindow, Component> contentGenerator) {
			this.caption = caption;
			this.contentGenerator = contentGenerator;
		}

		public Builder sizeUndefined(boolean sizeUndefined) {
			this.sizeUndefined = sizeUndefined;
			return this;
		}

		public Builder heightUndefined(boolean heightUndefined) {
			this.heightUndefined = heightUndefined;
			return this;
		}

		public Builder setWidth(float pixelWidth) {
			this.pixelWidth = pixelWidth;
			return this;
		}

		public Builder setHeight(float pixelHeight) {
			this.pixelHeight = pixelHeight;
			return this;
		}

		public Builder setType(Type type) {
			this.type = type;
			return this;
		}

		public PopupWindow build() {
			return new PopupWindow(this);
		}
	}

	public enum Type {
		ALERT, CONFIRM
	}

	public interface OnConfirm {
		/**
		 * What to do on confirm
		 * @return whether the dialog can close
		 */
		boolean onConfirm();
	}

}

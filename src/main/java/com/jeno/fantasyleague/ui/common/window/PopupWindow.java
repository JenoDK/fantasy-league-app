package com.jeno.fantasyleague.ui.common.window;

import java.util.function.Function;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;

public class PopupWindow extends Dialog {

	public PopupWindow(Builder builder) {
		super(new Label(builder.caption));

		// Size config
		if(builder.sizeUndefined){
			setSizeUndefined();
		} else {
			setHeight(builder.pixelHeight + "px");
			setWidth(builder.pixelWidth + "px");
		}

		setId(builder.id);

		try {
			add(builder.contentGenerator.apply(this));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static class Builder {

		public static final int DEFAULT_HEIGHT = 600;

		private float pixelHeight = DEFAULT_HEIGHT;
		private float pixelWidth = 600;
		private boolean sizeUndefined = false;

		private final String id;
		private final String caption;

		private final Function<Dialog, Component> contentGenerator;

		public Builder(String caption, String id, Function<Dialog, Component> contentGenerator) {
			this.caption = caption;
			this.id = id;
			this.contentGenerator = contentGenerator;
		}

		public Builder sizeUndefined(boolean sizeUndefined) {
			this.sizeUndefined = sizeUndefined;
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

		public PopupWindow build() {
			return new PopupWindow(this);
		}

	}

}

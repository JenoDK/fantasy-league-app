package com.jeno.fantasyleague.ui.common.window;

import com.vaadin.ui.Component;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class PopupWindow extends Window {

	public PopupWindow(Builder builder) {
		super();

		// Size config
		if(builder.sizeUndefined){
			this.setSizeUndefined();
		} else {
			this.setHeight(builder.pixelHeight, Unit.PIXELS);
			this.setWidth(builder.pixelWidth, Unit.PIXELS);
		}

		setClosable(builder.closable);
		setModal(builder.modal);
		setResizable(builder.resizable);

		setCaption(builder.caption);
		setId(builder.id);

		try {
			setContent(builder.contentGenerator.apply(this));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void show() {
		UI.getCurrent().addWindow(this);
	}

	public static class Builder {

		public static final int DEFAULT_HEIGHT = 600;

		private float pixelHeight = DEFAULT_HEIGHT;
		private float pixelWidth = 600;
		private boolean modal = true;
		private boolean closable = true;
		private boolean resizable = false;
		private boolean sizeUndefined = false;

		private final String id;
		private final String caption;

		private static final Consumer<Window> CLOSE = Window::close;

		private final Function<Window, Component> contentGenerator;

		public Builder(String caption, String id, Function<Window, Component> contentGenerator) {
			this.caption = caption;
			this.id = id;
			this.contentGenerator = contentGenerator;
		}

		public Builder resizable(boolean resizable) {
			this.resizable = resizable;
			return this;
		}

		public Builder sizeUndefined(boolean sizeUndefined) {
			this.sizeUndefined = sizeUndefined;
			return this;
		}

		public Builder closable(boolean closable) {
			this.closable = closable;
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

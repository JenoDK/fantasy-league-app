package com.jeno.fantasyleague.ui.common.window;

import com.vaadin.ui.Component;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import io.reactivex.functions.Consumer;

public class PopupWindow extends Window {

	private final Consumer<Window> onCancel;
	private final Consumer<Window> onConfirm;

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

		this.onCancel = builder.onCancel;
		this.onConfirm = builder.onConfirm;

		setContent(builder.contents);
	}

	public static class Builder {

		public static final int DEFAULT_HEIGHT = 600;

		private float pixelHeight = DEFAULT_HEIGHT;
		private float pixelWidth = 600;
		private boolean modal = true;
		private boolean closable = true;
		private boolean resizable = false;
		private boolean sizeUndefined = false;

		private String confirmText = "Ok";
		private String cancelText = "Cancel";
		private final String id;
		private final String caption;

		private static final Consumer<Window> CLOSE = Window::close;

		private Consumer<Window> onConfirm = CLOSE;
		private Consumer<Window> onCancel = CLOSE;

		private final Component contents;

		public Builder(String name, String id, Component contents) {
			this.caption = name;
			this.id = id;
			this.contents = contents;
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

		public Builder setOnConfirm(Consumer<Window> onConfirm) {
			this.onConfirm =  onConfirm;
			return this;
		}

		public Builder setOnCancel(Consumer<Window> onCancel) {
			this.onCancel = onCancel;
			return this;
		}

		public PopupWindow build() {
			return new PopupWindow(this);
		}

		public void show() {
			UI.getCurrent().addWindow(build());
		}
	}

}

package com.jeno.fantasyleague.ui.common.window;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import io.reactivex.functions.Consumer;

public class ConfirmationWindow {

	public static void showConfirmationPopup(String caption, String confirmText, Consumer<Void> onYes, Consumer<Void> onNo) {
		new PopupWindow.Builder(caption, "confirmationWindow", window -> createConfirmationLayout(confirmText, onYes, onNo, window))
				.closable(true)
				.setHeight(200)
				.setWidth(350)
				.build()
				.show();
	}

	public static Component createConfirmationLayout(String confirmText, Consumer<Void> onYes, Consumer<Void> onNo, Window window) {
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.setHeight(100, Sizeable.Unit.PERCENTAGE);

		Button yesButton = new Button("Yes", VaadinIcons.CHECK_CIRCLE_O);
		yesButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
		yesButton.addStyleName(ValoTheme.BUTTON_TINY);
		yesButton.addClickListener(ignored -> {
			try {
				onYes.accept(null);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			window.close();
		});

		Button cancelButton = new Button("Cancel");
		cancelButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
		cancelButton.addStyleName(ValoTheme.BUTTON_TINY);
		cancelButton.addClickListener(ignored -> {
			try {
				onNo.accept(null);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			window.close();
		});

		HorizontalLayout buttonLayout = new HorizontalLayout();
		buttonLayout.setWidth(100, Sizeable.Unit.PERCENTAGE);
		buttonLayout.addComponent(yesButton);
		buttonLayout.addComponent(cancelButton);
		buttonLayout.setComponentAlignment(yesButton, Alignment.MIDDLE_LEFT);
		buttonLayout.setComponentAlignment(cancelButton, Alignment.MIDDLE_RIGHT);

		layout.addComponent(new Label(confirmText));
		layout.addComponent(buttonLayout);
		layout.setComponentAlignment(buttonLayout, Alignment.BOTTOM_CENTER);

		return layout;
	}
}

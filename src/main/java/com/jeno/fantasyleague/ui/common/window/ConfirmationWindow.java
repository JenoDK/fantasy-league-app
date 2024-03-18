package com.jeno.fantasyleague.ui.common.window;

import com.jeno.fantasyleague.ui.common.field.CustomButton;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import io.reactivex.functions.Consumer;

public class ConfirmationWindow {

	public static void showConfirmationPopup(String caption, String confirmText, Consumer<Void> onYes, Consumer<Void> onNo) {
		new PopupWindow.Builder(caption, window -> createConfirmationLayout(confirmText, onYes, onNo, window))
				.setHeight(200)
				.setWidth(350)
				.build()
				.open();
	}

	public static Component createConfirmationLayout(String confirmText, Consumer<Void> onYes, Consumer<Void> onNo, Dialog window) {
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.setHeight("100%");

		Button yesButton = new CustomButton("Yes");
		yesButton.addClickListener(ignored -> {
			try {
				onYes.accept(null);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			window.close();
		});

		Button cancelButton = new CustomButton("Cancel");
		cancelButton.addClickListener(ignored -> {
			try {
				onNo.accept(null);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			window.close();
		});

		HorizontalLayout buttonLayout = new HorizontalLayout();
		buttonLayout.setWidth("100%");
		buttonLayout.add(yesButton, cancelButton);

		layout.add(new Text(confirmText));
		layout.add(buttonLayout);

		return layout;
	}
}

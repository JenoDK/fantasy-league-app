package com.jeno.demo.util;

import com.vaadin.shared.Registration;
import com.vaadin.ui.Button;
import io.reactivex.Observable;

public class RxUtil {

	private RxUtil() {
	}

	/**
	 * Get a {@link Observable} corresponding with the click stream of a
	 * {@link Button}.
	 */
	public static Observable<Button.ClickEvent> clicks(Button button) {
		return Observable.<Button.ClickEvent>create(subscriber -> {
			final Button.ClickListener listener = subscriber::onNext;
			Registration registration = button.addClickListener(listener);
			subscriber.setCancellable(() -> registration.remove());
		});
	}

}

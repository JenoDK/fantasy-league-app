package com.jeno.fantasyleague.util;

import com.vaadin.event.LayoutEvents;
import com.vaadin.shared.Registration;
import com.vaadin.ui.AbstractOrderedLayout;
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
		return Observable.create(subscriber -> {
			final Button.ClickListener listener = subscriber::onNext;
			Registration registration = button.addClickListener(listener);
			subscriber.setCancellable(() -> registration.remove());
		});
	}

	/**
	 * Get a {@link Observable} corresponding with the click stream of a
	 * {@link AbstractOrderedLayout}.
	 */
	public static Observable<LayoutEvents.LayoutClickEvent> clicks(AbstractOrderedLayout layout) {
		return Observable.create(subscriber -> {
			final LayoutEvents.LayoutClickListener listener = subscriber::onNext;
			Registration registration = layout.addLayoutClickListener(listener);
			subscriber.setCancellable(() -> registration.remove());
		});
	}
}

package com.jeno.fantasyleague.util;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ClickNotifier;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.shared.Registration;
import io.reactivex.Observable;

public class RxUtil {

	private RxUtil() {
	}

	/**
	 * Get a {@link Observable} corresponding with the click stream of a
	 * {@link Component} extending {@link ClickNotifier}.
	 */
	public static <T extends Component, C extends ClickNotifier<T>> Observable<ClickEvent<T>> clicks(C button) {
		return Observable.create(subscriber -> {
			final ComponentEventListener<ClickEvent<T>> listener = subscriber::onNext;
			Registration registration = button.addClickListener(listener);
			subscriber.setCancellable(registration::remove);
		});
	}

}

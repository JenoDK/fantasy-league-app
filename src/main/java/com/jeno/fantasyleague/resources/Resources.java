package com.jeno.fantasyleague.resources;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.i18n.I18NProvider;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.support.ResourceBundleMessageSource;

import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;

@SpringComponent
public class Resources implements I18NProvider {

	private static Resources activeResources = null;

	public static final Locale LOCALE_NL = new Locale("nl", "NL");
	public static final Locale LOCALE_EN_GB = new Locale("en", "GB");
	public static final Locale LOCALE_EN_US = new Locale("en", "US");

	private static List<Locale> locales = List.of(LOCALE_NL, LOCALE_EN_GB, LOCALE_EN_US);

	private final ResourceBundleMessageSource messageSource;

	@Autowired
	public Resources(ResourceBundleMessageSource messageSource) {
		this.messageSource = messageSource;
	}

	@Override
	public List<Locale> getProvidedLocales() {
		return locales;
	}

	@Override
	public String getTranslation(String key, Locale locale, Object... params) {
		if (key == null) {
			LoggerFactory.getLogger(Resources.class.getName())
					.warn("Got lang request for key with null value!");
			return "";
		}

		String value;
		try {
			value = messageSource.getMessage(key, params, locale);
		} catch (final MissingResourceException | NoSuchMessageException e) {
			LoggerFactory.getLogger(Resources.class.getName())
					.warn("Missing resource", e);
			return "!" + locale.getLanguage() + ": " + key;
		}
		if (params.length > 0) {
			value = MessageFormat.format(value, params);
		}
		return value;
	}

	public static void set(Resources resources) {
		activeResources = resources;
	}

	/**
	 * Get the active resources for this thread
	 *
	 * @return The resources
	 * @throws NoActiveResourcesFoundException If no resources are found
	 */
	public static Resources get() {
		if (activeResources == null) {
			throw new NoActiveResourcesFoundException();
		}

		return activeResources;
	}

	/**
	 * Get the message from the resource bundle if found, else just return back the resource string.
	 *
	 * @param resource
	 * @param args
	 * @return
	 */
	public static String getMessage(String resource, Object... args) {
		return get().getTranslation(resource, getLocale(), args);
	}

	/**
	 * Static copy from {@link Component#getLocale()}
	 * @return
	 */
	private static Locale getLocale() {
		UI currentUi = UI.getCurrent();
		Locale locale = currentUi == null ? null : currentUi.getLocale();
		if (locale == null) {
			if (locales != null && !locales.isEmpty()) {
				locale = locales.get(0);
			} else {
				locale = Locale.getDefault();
			}
		}
		return locale;
	}

}

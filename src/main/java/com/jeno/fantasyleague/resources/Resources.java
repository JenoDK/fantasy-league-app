package com.jeno.fantasyleague.resources;

import com.vaadin.spring.annotation.SpringComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.support.ResourceBundleMessageSource;

import java.util.Locale;

@SpringComponent
public class Resources {

	private static final Logger LOG = LogManager.getLogger(Resources.class.getName());

	private static final ThreadLocal<Resources> activeResources = new ThreadLocal<>();

	@Autowired
	private ResourceBundleMessageSource messageSource;

	private Locale locale = Locale.ENGLISH;

	private String getMessageInternal(String resource, Object... args) {
		return getMessageInternal(locale, resource, args);
	}

	private String getMessageInternal(Locale locale, String resource, Object... args) {
		try {
			return messageSource.getMessage(resource, args, locale);
		} catch (NoSuchMessageException nsmEx) {
			try {
				LOG.trace("Could not find resource", nsmEx);

				return messageSource.getMessage(resource, args, Locale.ENGLISH);
			} catch (NoSuchMessageException nsmExDefaultLocale) {
				LOG.trace("Could not find resource in default locale", nsmExDefaultLocale);

				LOG.warn(nsmExDefaultLocale.getMessage());
			}
		}
		return resource;
	}

	/**
	 * Get the active resources for this thread
	 *
	 * @return The resources
	 * @throws NoActiveResourcesFoundException If no resources are found
	 */
	public static Resources get() {
		Resources resources = activeResources.get();

		if (resources == null) {
			throw new NoActiveResourcesFoundException();
		}

		return resources;
	}

	/**
	 * Sets the active resources for this thread
	 *
	 * @param resources The resources
	 */
	public static void set(Resources resources) {
		activeResources.set(resources);
	}

	/**
	 * Get the message from the resource bundle if found, else just return back the resource string.
	 *
	 * @param resource
	 * @param args
	 * @return
	 */
	public static String getMessage(String resource, Object... args) {
		return get().getMessageInternal(resource, args);
	}

}

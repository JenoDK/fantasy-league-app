package com.jeno.fantasyleague.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import com.vaadin.flow.component.UI;

public class DateUtil {

	public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").withZone(ZoneOffset.UTC);
	public static final DateTimeFormatter DATE_DAY_FORMATTER = DateTimeFormatter.ofPattern("dd/MM");

	private DateUtil() {
	}

	/**
	 * Util method to convert the java 8 {@link java.time.LocalDateTime} into {@link java.util.Date}
	 * @param localDateTime
	 * @return converted date
	 */
	public static Date convertLocalDateTimeToDate(LocalDateTime localDateTime) {
		ZonedDateTime zdt = localDateTime.atZone(ZoneId.systemDefault());
		return Date.from(zdt.toInstant());
	}

	public static boolean nowIsBeforeUtcDateTime(LocalDateTime localDateTime) {
		Instant nowInUtc = Instant.now();
		return nowInUtc.isBefore(localDateTime.toInstant(ZoneOffset.UTC));
	}

	public static String toLocalDateTime(LocalDateTime localDateTime) {
		AtomicInteger offsetInSeconds = new AtomicInteger(0);
		UI currentUI = UI.getCurrent();
		if (currentUI != null && currentUI.getPage() != null) {
			currentUI.getPage().retrieveExtendedClientDetails(extendedClientDetails -> {
				offsetInSeconds.set(extendedClientDetails.getRawTimezoneOffset() / 1000);
			});
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
		formatter.withZone(ZoneId.of("Europe/Brussels"));
		ZonedDateTime dateTimeInMyZone = ZonedDateTime.of(localDateTime, ZoneOffset.ofTotalSeconds(offsetInSeconds.get()));
		return formatter.format(localDateTime);
	}

}

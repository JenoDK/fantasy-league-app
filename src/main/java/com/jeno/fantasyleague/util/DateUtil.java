package com.jeno.fantasyleague.util;

import com.vaadin.flow.server.VaadinSession;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtil {

	public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
	public static final DateTimeFormatter DATE_DAY_FORMATTER = DateTimeFormatter.ofPattern("dd/MM");
	public static final String TIMEZONE_OFFSET_ATTRIBUTE = "timezoneOffset";
	public static final String TIMEZONE_ID = "timezoneId";

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

	public static String formatInUserTimezone(LocalDateTime localDateTime) {
		return formatInUserTimezone(localDateTime, DATE_TIME_FORMATTER);
	}

	public static String formatInUserTimezone(LocalDateTime localDateTime, DateTimeFormatter dateTimeFormatter) {
		VaadinSession currentVaadinSession = VaadinSession.getCurrent();
		if (currentVaadinSession != null && currentVaadinSession.getAttribute(TIMEZONE_ID) != null) {
			ZoneId zoneId = ZoneId.of((String) currentVaadinSession.getAttribute(TIMEZONE_ID));
			ZoneId utcZoneId = ZoneId.of("UTC");
			ZonedDateTime utcZonedDateTime = localDateTime.atZone(utcZoneId);
			ZonedDateTime targetZonedDateTime = utcZonedDateTime.withZoneSameInstant(zoneId);
			return dateTimeFormatter.format(targetZonedDateTime);
		} else {
			return dateTimeFormatter.format(localDateTime);
		}
	}

}

package com.jeno.fantasyleague.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import com.vaadin.flow.server.VaadinSession;

public class DateUtil {

	public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
	public static final DateTimeFormatter DATE_DAY_FORMATTER = DateTimeFormatter.ofPattern("dd/MM");
	public static final String TIMEZONE_OFFSET_ATTRIBUTE = "timezoneOffset";

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
		if (currentVaadinSession != null && currentVaadinSession.getAttribute(TIMEZONE_OFFSET_ATTRIBUTE) != null) {
			Instant asInstant = Instant.ofEpochSecond(localDateTime.toEpochSecond(ZoneOffset.UTC));
			int timezoneOffsetInSeconds = (int) currentVaadinSession.getAttribute(TIMEZONE_OFFSET_ATTRIBUTE);
			return dateTimeFormatter.format(asInstant.atOffset(ZoneOffset.ofTotalSeconds(timezoneOffsetInSeconds)));
		} else {
			return dateTimeFormatter.format(localDateTime);
		}
	}

}

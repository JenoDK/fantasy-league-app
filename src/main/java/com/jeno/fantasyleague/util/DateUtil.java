package com.jeno.fantasyleague.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtil {

	public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
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

}

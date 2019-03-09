package se.liu.ida.joshu135.tddd78.util;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Utility class that handles time.
 */
public final class Time {
	private Time() {}

	public static String timeString() {
		LocalTime time = LocalTime.now();
		return time.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
	}
}

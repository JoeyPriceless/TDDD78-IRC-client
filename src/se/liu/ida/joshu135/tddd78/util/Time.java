package se.liu.ida.joshu135.tddd78.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Time {
	public static String timeString() {
		Calendar cal = Calendar.getInstance();
		Date date = cal.getTime();
		return new SimpleDateFormat("HH:mm:ss").format(date);
	}
}

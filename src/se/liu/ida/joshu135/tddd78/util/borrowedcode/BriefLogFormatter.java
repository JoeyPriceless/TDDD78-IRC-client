package se.liu.ida.joshu135.tddd78.util.borrowedcode;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * A Custom format implementation that is designed for brevity.
 * Source: https://www.javalobby.org//java/forums/t18515.html
 */
public class BriefLogFormatter extends Formatter {

	private static final DateFormat format = new SimpleDateFormat("HH:mm:ss");
	private static final String lineSep = System.getProperty("line.separator");

	public String format(LogRecord record) {
		String loggerName = record.getLoggerName();
		if(loggerName == null) {
			loggerName = "root";
		}
		String date = format.format(new Date(record.getMillis()));
		// return value looks like this
		// root[INFO|main|10:31:35]: Hello World\n
		return String.format("%s[%s|%s|%s]: %s%s", loggerName, record.getLevel(), Thread.currentThread().getName(), date,
							 record.getMessage(), lineSep);
	}

}
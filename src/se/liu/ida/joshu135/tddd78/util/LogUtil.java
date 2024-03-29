package se.liu.ida.joshu135.tddd78.util;

import se.liu.ida.joshu135.tddd78.util.borrowedcode.BriefLogFormatter;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Static class that sets up a logger with custom formatting.
 */
// Inspection: Adding a private constructor causes IllegalAccessException since Run Configuration needs to access it.
public final class LogUtil {

	public static Logger getLogger(String name) {
		Logger logger = Logger.getLogger(name);
		logger.setLevel(Level.INFO);
		ConsoleHandler handler = new ConsoleHandler();
		handler.setFormatter(new BriefLogFormatter());
		logger.addHandler(handler);
		return logger;
	}
}
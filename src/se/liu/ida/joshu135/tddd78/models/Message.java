package se.liu.ida.joshu135.tddd78.models;

import se.liu.ida.joshu135.tddd78.util.LogConfig;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Message {
	private static final Logger LOGGER = LogConfig.getLogger(Message.class.getSimpleName());
	private String message;
	private String prefix;
	private String command;
	private String params;
	private String trailing;

	public String getMessage() {
		return message;
	}

	public String getPrefix() {
		return prefix;
	}

	public String getCommand() {
		return command;
	}

	public String getParams() {
		return params;
	}

	public String getTrailing() {
		return trailing;
	}

	public Message(final String msg) {
//		this.message = msg.trim();
		this.message = msg;
		if (message == null) {
			LOGGER.warning("Cannot create object from null message.");
			return;
		}
		// See https://tools.ietf.org/html/rfc2812#section-2.4 for the message format.
		// Group 0: prefix (including ':')
		// Group 1: command
		// Group 2: substring of params
		// Group 3: substring of trailing
		Pattern p = Pattern.compile("(:\\S*)?\\s?([a-zA-Z]+|\\d{3})\\s([^:]*):?([^\\n\\r]*)");
		Matcher m = p.matcher(message);
		if (m.find()) {
			prefix = m.group(1);
			command = m.group(2);
			params = m.group(3);
			trailing = m.group(4);
			LOGGER.log(Level.FINE, String.format("%s --- p[%s] c[%s] p[%s] t[%s] ", message, prefix, command, params, trailing));
		} else {
			LOGGER.warning(String.format("Could not parse regex '%s'", message));
		}
	}
}

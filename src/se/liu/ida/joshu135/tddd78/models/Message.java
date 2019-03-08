package se.liu.ida.joshu135.tddd78.models;

import se.liu.ida.joshu135.tddd78.util.LogConfig;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Contains messages in the BNF format specified in the IRC protocol. Messages are split into different parts and may consists
 * of: a prefix, a command, up to 15 parameters and some trailing text.
 */
public class Message {
	private static final Logger LOGGER = LogConfig.getLogger(Message.class.getSimpleName());
	private String message;
	private String prefix = null;
	private String nickname = null;
	private String userHost = null;
	private String command = null;
	private String params = null;
	private String trailing = null;

	public String getMessage() {
		return message;
	}

	public String getPrefix() {
		return prefix;
	}

	public String getNickname() {
		return nickname;
	}

	public String getUserHost() {
		return userHost;
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
		this.message = msg;
		if (message == null) {
			LOGGER.warning("Cannot create object from null message.");
			return;
		}
		// See https://tools.ietf.org/html/rfc2812#section-2.4 for the message format.
		// Group 0: prefix (including ':')
		// Group 1: command   .substring(1); // Remove first character ":"
		// Group 2: substring of params
		// Group 3: substring of trailing
		Pattern pGeneral = Pattern.compile("(:\\S*)?\\s?([a-zA-Z]+|\\d{3})\\s([^:]*):?([^\\n\\r]*)");
		Matcher mGeneral = pGeneral.matcher(message);
		if (mGeneral.find()) {
			prefix = mGeneral.group(1);
			// Check if message has been sent from a user. If so, split the nickname from the hostname.
			if (prefix != null) {
				Pattern pUser = Pattern.compile(":(\\S*)?!~?(\\S*)");
				Matcher mUser = pUser.matcher(prefix);
				if (mUser.find()) {
					nickname = mUser.group(1);
					userHost = mUser.group(2);
				}
			}
			command = mGeneral.group(2);
			params = mGeneral.group(3);
			trailing = mGeneral.group(4);
			// Only for debug
			LOGGER.log(Level.FINE, String.format("%s --- p[%s] (n[%s]) (h[%s]) c[%s] p[%s] t[%s] ", message, prefix, nickname,
												 userHost, command, params, trailing));
		} else {
			LOGGER.warning(String.format("Could not parse regex '%s'", message));
		}
	}

	/**
	 * Splits parameters by it's space characters and returns them.
	 * @return A String[] with up to 15 parameters.
	 */
	public String[] splitParams() {
		return splitSpace(params);
	}

	/**
	 * Get a parameter at a certain index.
	 * @param index Zero-based index of parameter.
	 *
	 * @return A string containing the parameter at index i.
	 */
	public String getParamAt(int index) {
		return splitParams()[index];
	}

	/**
	 * Splits trailing message by it's space characters and returns them.
	 * @return A String[] with all trailing parameters.
	 */
	public String[] splitTrailing() {
		return splitSpace(trailing);
	}

	/**
	 * Get a trailing parameter at a certain index.
	 * @param index Zero-based index of trailing param.
	 *
	 * @return A string containing the trailing param at index i.
	 */
	public String getTrailingAt(int index) {
		return splitTrailing()[index];
	}

	private String[] splitSpace(String text) {
		return text.split("\\s+");
	}
}

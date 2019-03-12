package se.liu.ida.joshu135.tddd78.backend;

import se.liu.ida.joshu135.tddd78.models.Message;
import se.liu.ida.joshu135.tddd78.models.AppUser;
import se.liu.ida.joshu135.tddd78.util.LogUtil;

import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Composes messages that are ready to be sent to an IRC server. Also includes shortcuts to frequent commands
 * Messages are placed in a LinkedTransferQueue and dealt with in MessageSender on another thread.
 */
public class MessageComposer {
	private static final Logger LOGGER = LogUtil.getLogger(MessageComposer.class.getSimpleName());
	private static final String NEWLINE = "\r\n";
	private static final int MAX_COMMAND_PARAMS = 15;
	private BlockingQueue<Message> messageQueue;

	/**
	 * Max length of a message, including newline characters "\r\n"
	 */
	public static final int MAX_LENGTH = 512;

	public MessageComposer(final BlockingQueue<Message> messageQueue) {
		this.messageQueue = messageQueue;
	}

	/**
	 * Composes a message without prefix according to the RFC 2812 specification.
	 * @param command An IRC command. Listed here https://tools.ietf.org/html/rfc2812#section-3
	 * @param params A variable amount of command parameters. From 0 up to a maximum of 15.
	 *
	 * @return The message, consisting of a command and its params, followed by newline characters \r\n.
	 * @throws IllegalArgumentException If params exceed maximum length 15.
	 */
	public static String compose(String command, String... params) {
		// Could not do this as a regular overload due to argvar's parameter ambiguity
		return composeWithPrefix("", command, params);
	}

	/**
	 * Composes a message to be sent according to the RFC 2812 specification.
	 * @param prefix prefix String according to RFC 2812
	 * @param command An IRC command. Listed here https://tools.ietf.org/html/rfc2812#section-3
	 * @param params A variable amount of command parameters. From 0 up to a maximum of 15.
	 *
	 * @return The message, consisting of an optional prefix, a command and its params, followed by newline characters \r\n.
	 * @throws IllegalArgumentException If params exceed maximum length 15.
	 */
	public static String composeWithPrefix(String prefix, String command, String... params) {
		String[] shortParams = params;
		if (params.length > MAX_COMMAND_PARAMS) {
			String joined = String.join(" ", params);
			LOGGER.log(Level.WARNING, command + joined + " -- exceeds maximum of 15 params. Using first 15 params.");
			shortParams = new String[MAX_COMMAND_PARAMS];
			System.arraycopy(params, 0, shortParams, 0, shortParams.length);
		}

		StringBuilder messageBuilder = new StringBuilder();
		if (!prefix.isEmpty()) {
			messageBuilder.append(":").append(prefix);
		}
		messageBuilder.append(command);
		for (String param : shortParams) {
			messageBuilder.append(" ").append(param);
		}
		messageBuilder.append(NEWLINE);
		if (messageBuilder.length() > MAX_LENGTH) {
			// Not supporting Java pre-5.0
			LOGGER.log(Level.WARNING, String.format("Composed message length (%s) is longer than max length of 512" +
														   "characters.", messageBuilder.length()));
		}
		return messageBuilder.toString();
	}

	public void queueMessage(Message msg) {
		messageQueue.add(msg);
	}

	/**
	 * Register a connection with an IRC server. Send NICK & USER message.
	 * https://tools.ietf.org/html/rfc2812#section-3.1
	 */
	public void registerConnection(AppUser user) {
		Message nickMsg = new Message(compose("NICK", user.getNickname()));
		Message userMsg = new Message(compose("USER", user.getUsername(), user.getMode(), "*",
															  ":" + user.getRealname()));
		queueMessage(nickMsg);
		queueMessage(userMsg);
	}

	/**
	 * Join a channel without a key
	 * @param channel channel name
	 *
	 */
	public void joinChannel(String channel) {
		joinChannel(channel, null);
	}

	public void joinChannel(String channel, String key) {
		// The "0" argument specifies that user leaves all current channels.
		Message leaveMsg = new Message(compose("JOIN", "0"));
		queueMessage(leaveMsg);
		String s;
		if (key != null) {
			s = compose("JOIN", channel, key);
		} else {
			s = compose("JOIN", channel);
		}
		Message joinMsg = new Message(s);
		queueMessage(joinMsg);
	}

	public void sendPrivMsg(String channel, String text) {
		Message message = new Message(compose("PRIVMSG", channel, ":" + text));
		queueMessage(message);
	}

	public void listChannels() {
		queueMessage(new Message(compose("LIST")));
	}

	/**
	 * Takes a vararg of strings that have to be in the message and returns the max length of the variable parts of the message.
	 * @param maxLength The maximum length. Default: 510
	 * @param knownStrings All the strings that are required to be in the message.
	 *
	 * @return maxLength minus total length of knownStrings
	 */
	public static int lengthAllowance(int maxLength, String... knownStrings) {
		int length = NEWLINE.length(); // 2 characters for \n\r
		for (String s : knownStrings) {
			length += s.length();
		}
		return maxLength - length;
	}
}

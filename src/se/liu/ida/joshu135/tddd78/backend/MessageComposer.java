package se.liu.ida.joshu135.tddd78.backend;

import se.liu.ida.joshu135.tddd78.models.Message;
import se.liu.ida.joshu135.tddd78.models.User;

import java.util.concurrent.LinkedTransferQueue;

/**
 * Static class that composes messages that are ready to be sent to an IRC server. Also includes shortcuts to frequent commands
 */
public class MessageComposer {
	private static final String NEWLINE = "\r\n";
	private static final int MAX_LENGTH = 512;
	private static final int MAX_COMMAND_PARAMS = 15;
	private LinkedTransferQueue<Message> messageQueue;

	public MessageComposer(final LinkedTransferQueue<Message> messageQueue) {
		this.messageQueue = messageQueue;
	}

	/**
	 * Composes a message without prefix according to the RFC 2812 specification.
	 * @param command An IRC command. Listed here https://tools.ietf.org/html/rfc2812#section-3
	 * @param params A variable amount of command parameters. From 0 up to a maximum of 15.
	 *
	 * @return The message, consisting of a command and its params, followed by newline characters \r\n.
	 * @throws IllegalArgumentException If params exceed maximum length 15.
	 * @throws MessageLengthException If the total message is too long (total of 512 chars).
	 */
	public static String compose(String command, String... params) throws IllegalArgumentException, MessageLengthException {
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
	 * @throws MessageLengthException If the total message is too long (total of 512 chars).
	 */
	// TODO add logging
	public static String composeWithPrefix(String prefix, String command, String... params) throws IllegalArgumentException,
			MessageLengthException {
		if (params.length > MAX_COMMAND_PARAMS) {
			throw new IllegalArgumentException(params.length + " exceeds maximum of 15 params.");
		}

		StringBuilder messageBuilder = new StringBuilder();
		if (!prefix.equals("")) {
			messageBuilder.append(":".concat(prefix));
		}
		messageBuilder.append(command);
		for (String param : params) {
			messageBuilder.append(" ".concat(param));
		}
		messageBuilder.append(NEWLINE);
		if (messageBuilder.length() > MAX_LENGTH) {
			throw new MessageLengthException("Composed message is longer than max length of 512 characters.");
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
	public void registerConnection(User user) {
		try {
			Message nickMsg = new Message(MessageComposer.compose("NICK", user.getNickname()));
			Message userMsg = new Message(MessageComposer.compose("USER", user.getUsername(), user.getMode(), "*",
																  ":" + user.getRealname()));
			queueMessage(nickMsg);
			queueMessage(userMsg);
		} catch (MessageComposer.MessageLengthException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Join a channel without a key
	 * @param channel channel name
	 *
	 * @throws MessageComposer.MessageLengthException
	 */
	public void joinChannel(String channel) throws MessageComposer.MessageLengthException {
		joinChannel(channel, null);
	}

	/**
	 * Join a channel with a key
	 * @param channel
	 * @param key
	 *
	 * @throws MessageComposer.MessageLengthException
	 */
	public void joinChannel(String channel, String key)
			throws MessageComposer.MessageLengthException {
		String s;
		if (key != null) {
			s = MessageComposer.compose("JOIN", channel, key);
		} else {
			s = MessageComposer.compose("JOIN", channel);
		}
		Message joinMsg = new Message(s);
		queueMessage(joinMsg);
	}

	/**
	 * Thrown when there is a problem with the length of a composed message. Usually when it exceeds the protocols max length.
	 */
	public static class MessageLengthException extends Exception {
		public MessageLengthException() {
			super();
		}

		public MessageLengthException(final String message) {
			super(message);
		}

		public MessageLengthException(final String message, final Throwable cause) {
			super(message, cause);
		}

		public MessageLengthException(final Throwable cause) {
			super(cause);
		}

		public MessageLengthException(final String message, final Throwable cause, final boolean enableSuppression,
									  final boolean writableStackTrace)
		{
			super(message, cause, enableSuppression, writableStackTrace);
		}
	}
}

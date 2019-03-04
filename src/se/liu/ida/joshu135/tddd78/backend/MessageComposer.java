package se.liu.ida.joshu135.tddd78.backend;

import org.apache.commons.lang3.exception.ExceptionUtils;
import se.liu.ida.joshu135.tddd78.models.Message;
import se.liu.ida.joshu135.tddd78.models.User;
import se.liu.ida.joshu135.tddd78.util.LogConfig;

import java.util.concurrent.LinkedTransferQueue;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Composes messages that are ready to be sent to an IRC server. Also includes shortcuts to frequent commands
 * Messages are placed in a LinkedTransferQueue and dealt with in MessageSender on another thread.
 */
public class MessageComposer {
	private static final Logger LOGGER = LogConfig.getLogger(MessageComposer.class.getSimpleName());
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
	public static String composeWithPrefix(String prefix, String command, String... params) throws IllegalArgumentException,
			MessageLengthException {
		if (params.length > MAX_COMMAND_PARAMS) {
			String joined = Stream.of(params).collect(Collectors.joining());
			throw new IllegalArgumentException(joined + "exceeds maximum of 15 params");
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
			throw new MessageLengthException(String.format("Composed message length (%s) is longer than max length of 512" +
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
	public void registerConnection(User user) {
		try {
			Message nickMsg = new Message(MessageComposer.compose("NICK", user.getNickname()));
			Message userMsg = new Message(MessageComposer.compose("USER", user.getUsername(), user.getMode(), "*",
																  ":" + user.getRealname()));
			queueMessage(nickMsg);
			queueMessage(userMsg);
		} catch (MessageComposer.MessageLengthException ex) {
			LOGGER.severe(ExceptionUtils.getStackTrace(ex));
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
		// The "0" argument specifies that user leaves all current channels.
		Message leaveMsg = new Message(MessageComposer.compose("JOIN", "0"));
		queueMessage(leaveMsg);
		String s;
		if (key != null) {
			s = MessageComposer.compose("JOIN", channel, key);
		} else {
			s = MessageComposer.compose("JOIN", channel);
		}
		Message joinMsg = new Message(s);
		queueMessage(joinMsg);
	}

	public void sendChannelMessage(String channel, String text) throws MessageLengthException{
		Message message = new Message(MessageComposer.compose("PRIVMSG", channel, ":" + text));
		queueMessage(message);
	}


	// TODO implement "lengthAllowance" method which can prewarn user when message gets too long.
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

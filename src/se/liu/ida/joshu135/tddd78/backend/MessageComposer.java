package se.liu.ida.joshu135.tddd78.backend;

public class MessageComposer {
	private static final String NEWLINE = "\r\n";
	private static final int MAX_LENGTH = 512;


	/**
	 * @param command
	 * @param params
	 *
	 * @return
	 * @throws IllegalArgumentException
	 * @throws MessageLengthException
	 */
	public static String compose(String command, String... params) throws IllegalArgumentException, MessageLengthException {
		return composeWithPrefix("", command, params);
	}

	/**
	 * Composes a message to be sent to the IRC server.
	 * @param prefix prefix String according to RFC 2812
	 * @param command A valid IRC command
	 * @param params List of parameters for the command
	 *
	 * @return
	 * @throws IllegalArgumentException Thrown when params exceed maximum length 15.
	 * @throws MessageLengthException Thrown when total message length exceeds 512 chars.
	 */
	// TODO add logging
	public static String composeWithPrefix(String prefix, String command, String... params) throws IllegalArgumentException,
			MessageLengthException {
		if (params.length > 15) {
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
		if (messageBuilder.length() > 512) {
			throw new MessageLengthException("Composed message is longer than max length of 512 characters.");
		}
		return messageBuilder.toString();
	}

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

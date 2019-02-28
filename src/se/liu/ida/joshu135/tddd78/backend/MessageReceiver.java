package se.liu.ida.joshu135.tddd78.backend;

import se.liu.ida.joshu135.tddd78.backend.response.ResponseAction;
import se.liu.ida.joshu135.tddd78.backend.response.ResponseActionFactory;
import se.liu.ida.joshu135.tddd78.models.Message;
import se.liu.ida.joshu135.tddd78.util.LogConfig;

import java.io.IOException;
import java.util.concurrent.LinkedTransferQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MessageReceiver implements Runnable {
	private static final Logger LOGGER = LogConfig.getLogger(MessageSender.class.getSimpleName());
	private ConnectionHandler conHandler;
	private MessageComposer composer;

	public MessageReceiver(final ConnectionHandler conHandler, MessageComposer composer) {
		this.conHandler = conHandler;
		this.composer = composer;
	}

	/**
	 * Infinite loop that handles messages in order and only moves on to the next message after the response has been read.
	 */
	@Override public void run() {
		String line;
		Message message;
			try {
				while ((line = conHandler.readLine()) != null) {
					message = new Message(line);
					ResponseAction action = ResponseActionFactory.getAction(message.getCommand());
					if (action != null) {
						action.handle(composer, message);
					}
				}
			} catch (IOException | MessageComposer.MessageLengthException ex) {
				LOGGER.log(Level.WARNING, ex.getMessage(), ex);
			} catch (NullPointerException ex) {
				LOGGER.log(Level.SEVERE, "NullPointerException", ex);
			}
	}
}

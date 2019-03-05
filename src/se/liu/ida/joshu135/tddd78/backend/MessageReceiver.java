package se.liu.ida.joshu135.tddd78.backend;

import se.liu.ida.joshu135.tddd78.backend.MessageComposer.MessageLengthException;
import se.liu.ida.joshu135.tddd78.backend.response.ResponseAction;
import se.liu.ida.joshu135.tddd78.backend.response.ResponseActionFactory;
import se.liu.ida.joshu135.tddd78.frontend.ChatViewer;
import se.liu.ida.joshu135.tddd78.models.Message;
import se.liu.ida.joshu135.tddd78.util.LogConfig;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A runnable that handles inbound messages in order and only moves on to the next message after the response has been read.
 */
public class MessageReceiver implements Runnable {
	private static final Logger LOGGER = LogConfig.getLogger(MessageReceiver.class.getSimpleName());
	private ResponseActionFactory factory;
	private ConnectionHandler conHandler;
	private MessageComposer composer;

	public MessageReceiver(final ConnectionHandler conHandler, MessageComposer composer, final ChatViewer chatViewer) {
		this.conHandler = conHandler;
		this.composer = composer;
		this.factory = new ResponseActionFactory(chatViewer);
	}

	@Override public void run() {
			try {
				String line;
				while ((line = conHandler.readLine()) != null) {
					Message message = new Message(line);
					ResponseAction action = factory.getAction(message.getCommand());
					if (action != null) {
						action.handle(composer, message);
					}
				}
			} catch (IOException | MessageLengthException ex) {
				LOGGER.log(Level.WARNING, ex.getMessage(), ex);
			}
	}
}

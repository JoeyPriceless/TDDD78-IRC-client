package se.liu.ida.joshu135.tddd78.backend;

import se.liu.ida.joshu135.tddd78.backend.response.Correspondent;
import se.liu.ida.joshu135.tddd78.backend.response.ResponseHandler;
import se.liu.ida.joshu135.tddd78.backend.response.ResponseHandlerFactory;
import se.liu.ida.joshu135.tddd78.frontend.ViewMediator;
import se.liu.ida.joshu135.tddd78.models.Message;
import se.liu.ida.joshu135.tddd78.util.LogUtil;

import java.util.logging.Logger;

/**
 * A runnable that handles inbound messages in order and only moves on to the next message after the response has been read.
 */
public class MessageReceiver implements Runnable {
	private static final Logger LOGGER = LogUtil.getLogger(MessageReceiver.class.getSimpleName());
	private ResponseHandlerFactory factory;
	private ConnectionHandler conHandler;
	private MessageComposer composer;

	public MessageReceiver(final ConnectionHandler conHandler, MessageComposer composer, final ViewMediator mediator) {
		this.conHandler = conHandler;
		this.composer = composer;
		this.factory = new ResponseHandlerFactory(mediator);
	}

	@Override public void run() {
		// Infinite loop is intentional.
		while (true) {
			String line = conHandler.readLine();
			if (line == null || line.equals("null")) {
				LOGGER.warning("Received null line");
				continue;
			}
			Message message = new Message(line);
			ResponseHandler action = factory.getAction(message.getCommand());
			// If action instance implements Correspondent, it needs a MessageComposer in order to responsd to the message.
			if (action instanceof Correspondent) {
				((Correspondent) action).handle(composer, message);
			} else {
				action.handle(message);
			}

		}
	}
}

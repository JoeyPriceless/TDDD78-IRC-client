package se.liu.ida.joshu135.tddd78.backend.response;

import se.liu.ida.joshu135.tddd78.backend.MessageComposer;
import se.liu.ida.joshu135.tddd78.frontend.ChatViewer;
import se.liu.ida.joshu135.tddd78.models.Message;

public class PongAction implements ResponseAction {
	public PongAction() {
	}

	// TODO support for secondary pong server.
	@Override public void handle(final MessageComposer composer, Message response) throws
			MessageComposer.MessageLengthException
	{
		Message message = new Message(MessageComposer.compose("PONG", response.getTrailing()));
		composer.queueMessage(message);
	}
}

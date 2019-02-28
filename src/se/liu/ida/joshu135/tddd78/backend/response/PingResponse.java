package se.liu.ida.joshu135.tddd78.backend.response;

import se.liu.ida.joshu135.tddd78.backend.MessageComposer;
import se.liu.ida.joshu135.tddd78.models.Message;

public class PingResponse implements ResponseAction {
	public PingResponse() {
	}

	// TODO support for secondary pong server.
	@Override public void handle(final MessageComposer composer, Message response) throws
			MessageComposer.MessageLengthException
	{
		Message message = new Message(MessageComposer.compose("PONG", response.getTrailing()));
		composer.queueMessage(message);
	}
}

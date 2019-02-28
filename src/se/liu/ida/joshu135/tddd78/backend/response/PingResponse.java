package se.liu.ida.joshu135.tddd78.backend.response;

import se.liu.ida.joshu135.tddd78.backend.MessageComposer;
import se.liu.ida.joshu135.tddd78.models.Message;

public class PingResponse implements ResponseAction {
	public PingResponse() {
	}

	@Override public void handle(final MessageComposer composer, final String... params) throws
			MessageComposer.MessageLengthException
	{
		Message message = new Message(MessageComposer.compose("PONG", params));
		composer.queueMessage(message);
	}
}

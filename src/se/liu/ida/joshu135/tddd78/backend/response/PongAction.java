package se.liu.ida.joshu135.tddd78.backend.response;

import se.liu.ida.joshu135.tddd78.backend.MessageComposer;
import se.liu.ida.joshu135.tddd78.models.Message;

public class PongAction implements ResponseDialog {
	public PongAction() {
	}

	// Respond to a PING message with a PONG.
	// If ping contains 2 parameters, send the response to the second one.
	@Override public void handle(final MessageComposer composer, Message message) throws IllegalArgumentException
	{
		String[] trail = message.splitTrailing();
		String server;
		if (trail.length == 0) {
			throw new IllegalArgumentException("No PONG server");
		} else if (trail.length == 1) {
			server = trail[0];
		} else {
			server = trail[1];
		}
		Message response = new Message(MessageComposer.compose("PONG", server));
		composer.queueMessage(response);
	}

	@Override public void handle(final Message message) {
		throw new UnsupportedOperationException("Action requires access to MessageComposer.");
	}
}

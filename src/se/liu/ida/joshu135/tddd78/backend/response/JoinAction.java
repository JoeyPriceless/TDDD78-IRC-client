package se.liu.ida.joshu135.tddd78.backend.response;

import se.liu.ida.joshu135.tddd78.backend.MessageComposer;
import se.liu.ida.joshu135.tddd78.frontend.ChatViewer;
import se.liu.ida.joshu135.tddd78.models.Message;

public class JoinAction extends ChatWriter implements ResponseAction {
	public JoinAction(final ChatViewer chatViewer) {
		super(chatViewer);
	}

	// TODO Regex for username / IP in prefix
	@Override public void handle(final MessageComposer composer, final Message response)
			throws MessageComposer.MessageLengthException
	{
		displayServerMessage(response.getPrefix() + " joins " + response.getParams());
	}
}
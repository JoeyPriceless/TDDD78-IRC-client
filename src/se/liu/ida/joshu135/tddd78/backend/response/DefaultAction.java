package se.liu.ida.joshu135.tddd78.backend.response;

import se.liu.ida.joshu135.tddd78.backend.MessageComposer;
import se.liu.ida.joshu135.tddd78.frontend.ChatViewer;
import se.liu.ida.joshu135.tddd78.models.Message;

public class DefaultAction extends ChatWriter implements ResponseAction {
	public DefaultAction(final ChatViewer chatViewer) {
		super(chatViewer);
	}

	@Override public void handle(final MessageComposer composer, final Message response)
			throws MessageComposer.MessageLengthException
	{
		displayServerMessage(response.getTrailing());
	}
}

package se.liu.ida.joshu135.tddd78.backend.response;

import se.liu.ida.joshu135.tddd78.backend.MessageComposer;
import se.liu.ida.joshu135.tddd78.frontend.ChatViewer;
import se.liu.ida.joshu135.tddd78.models.Message;

public class QuitAction extends ChatWriter implements ResponseAction {
	public QuitAction(final ChatViewer chatViewer) {
		super(chatViewer);
	}

	// TODO Regex for username / IP in prefix
	@Override public void handle(final MessageComposer composer, final Message response)
			throws MessageComposer.MessageLengthException
	{
		displayServerMessage(String.format("%s has quit IRC (%s)", response.getPrefix(), response.getTrailing()));
	}
}

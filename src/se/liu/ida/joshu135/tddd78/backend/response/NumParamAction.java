package se.liu.ida.joshu135.tddd78.backend.response;

import se.liu.ida.joshu135.tddd78.backend.MessageComposer;
import se.liu.ida.joshu135.tddd78.frontend.ChatViewer;
import se.liu.ida.joshu135.tddd78.models.Message;

public class NumParamAction extends ChatWriter implements ResponseAction {
	public NumParamAction(final ChatViewer chatViewer) {
		super(chatViewer);
	}

	@Override public void handle(final MessageComposer composer, final Message response)
	{
		// Discard the first parameter that is the user's name.
		displayServerMessage(String.format("%s %s", response.splitParams()[1], response.getTrailing()));
	}
}

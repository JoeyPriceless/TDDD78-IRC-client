package se.liu.ida.joshu135.tddd78.backend.response;

import se.liu.ida.joshu135.tddd78.backend.MessageComposer;
import se.liu.ida.joshu135.tddd78.frontend.ChatViewer;
import se.liu.ida.joshu135.tddd78.models.Message;

public class DisplayAction extends ViewEditor implements ResponseAction {
	public DisplayAction(final ChatViewer chatViewer) {
		super(chatViewer);
	}

	@Override public void handle(final Message response)
	{
		displayServerMessage(response.getTrailing());
	}
}

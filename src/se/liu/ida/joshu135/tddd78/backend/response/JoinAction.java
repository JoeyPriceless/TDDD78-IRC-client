package se.liu.ida.joshu135.tddd78.backend.response;

import se.liu.ida.joshu135.tddd78.frontend.ChatViewer;
import se.liu.ida.joshu135.tddd78.models.Message;

public class JoinAction extends ViewEditor implements ResponseAction {
	public JoinAction(final ChatViewer chatViewer) {
		super(chatViewer);
	}

	@Override public void handle(final Message response)
	{
		displayServerMessage(String.format("%s (%s) joins %s", response.getNickname(), response.getUserHost(),
										   response.getParams()));
	}
}

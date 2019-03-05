package se.liu.ida.joshu135.tddd78.backend.response;

import se.liu.ida.joshu135.tddd78.backend.MessageComposer;
import se.liu.ida.joshu135.tddd78.frontend.ChatViewer;
import se.liu.ida.joshu135.tddd78.models.Message;

public class PrivMsgAction extends ChatWriter implements ResponseAction {
	public PrivMsgAction(final ChatViewer chatViewer) {
		super(chatViewer);
	}

	@Override public void handle(final MessageComposer composer, final Message response)
	{
		displayUserMessage(response.getNickname(), response.getTrailing());
	}
}
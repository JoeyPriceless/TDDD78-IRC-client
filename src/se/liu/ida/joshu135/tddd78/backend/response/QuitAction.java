package se.liu.ida.joshu135.tddd78.backend.response;

import se.liu.ida.joshu135.tddd78.backend.MessageComposer;
import se.liu.ida.joshu135.tddd78.frontend.ChatViewer;
import se.liu.ida.joshu135.tddd78.models.Message;

public class QuitAction extends ChatWriter implements ResponseAction {
	public QuitAction(final ChatViewer chatViewer) {
		super(chatViewer);
	}

	@Override public void handle(final MessageComposer composer, final Message response)
	{
		String cmd = response.getCommand();
		String message;
		// Differentiates between leaving server and leaving channel.
		if (cmd == null) {
			return;
		} else if (cmd.equals("PART")) {
			message = String.format("%s (%s) has left %s", response.getNickname(), response.getUserHost(),
												response.getParams());
		} else {
			String trail = response.getTrailing().isEmpty() ? (" - " + response.getTrailing()) : "";

			message = String.format("%s (%s) has quit IRC%s", response.getNickname(), response.getUserHost(),
									trail);
		}
		displayServerMessage(message);

	}
}

package se.liu.ida.joshu135.tddd78.backend.response;

import se.liu.ida.joshu135.tddd78.frontend.ChatViewer;
import se.liu.ida.joshu135.tddd78.models.Message;

public class QuitAction extends ViewEditor implements ResponseAction {
	public QuitAction(final ChatViewer chatViewer) {
		super(chatViewer);
	}

	@Override public void handle(final Message message)
	{
		String cmd = message.getCommand();
		String messageText;
		// Differentiates between leaving server and leaving channel.
		if (cmd == null) {
			return;
		} else if (cmd.equals("PART")) {
			messageText = String.format("%s (%s) has left %s", message.getNickname(), message.getUserHost(),
									message.getParams());
		} else {
			String trail = message.getTrailing().isEmpty() ? (" - " + message.getTrailing()) : "";

			messageText = String.format("%s (%s) has quit IRC%s", message.getNickname(), message.getUserHost(),
									trail);
		}
		displayServerMessage(messageText);

	}
}

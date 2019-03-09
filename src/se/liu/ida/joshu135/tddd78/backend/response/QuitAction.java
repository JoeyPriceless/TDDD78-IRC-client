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
		String nickname = message.getNickname();
		String host = message.getUserHost();
		// Differentiates between leaving server and leaving channel.
		if (cmd == null) {
			return;
		} else if (cmd.equals("PART")) {
			messageText = String.format("%s (%s) has left %s", nickname, host, message.getParams());
		} else {
			String trail = message.getTrailing().isEmpty() ? (" - " + message.getTrailing()) : "";

			messageText = String.format("%s (%s) has quit IRC%s", nickname, host, trail);
		}
		displayServerMessage(messageText);
		chatViewer.getUserListComponent().removeUser(nickname);
	}
}

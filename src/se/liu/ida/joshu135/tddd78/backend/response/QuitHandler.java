package se.liu.ida.joshu135.tddd78.backend.response;

import se.liu.ida.joshu135.tddd78.frontend.ViewMediator;
import se.liu.ida.joshu135.tddd78.models.Message;

public class QuitHandler extends AbstractViewEditor implements ResponseHandler {
	public QuitHandler(final ViewMediator mediator) {
		super(mediator);
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
		displayChannelInfoMessage(messageText);
		mediator.removeUserFromList(nickname);
	}
}

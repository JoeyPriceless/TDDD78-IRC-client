package se.liu.ida.joshu135.tddd78.backend.response;

import se.liu.ida.joshu135.tddd78.frontend.ChatViewer;
import se.liu.ida.joshu135.tddd78.models.Message;

public class JoinHandler extends AbstractViewEditor implements ResponseHandler {
	public JoinHandler(final ChatViewer chatViewer) {
		super(chatViewer);
	}

	@Override public void handle(final Message message)
	{
		// Varies between servers whether channel name is in params or trailing...
		String nickname = message.getNickname();
		String channelName = message.getParams().isEmpty() ? message.getTrailing() : message.getParams();
		displayServerMessage(String.format("%s (%s) joins %s", nickname, message.getUserHost(),
										   channelName));
		chatViewer.getUserListComponent().addUser(nickname);
	}
}

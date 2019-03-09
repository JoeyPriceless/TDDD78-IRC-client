package se.liu.ida.joshu135.tddd78.backend.response;

import se.liu.ida.joshu135.tddd78.frontend.ChatViewer;
import se.liu.ida.joshu135.tddd78.models.Channel;
import se.liu.ida.joshu135.tddd78.models.Message;

public class ListAction extends ViewEditor implements ResponseAction {
	public ListAction(final ChatViewer chatViewer) {
		super(chatViewer);
	}

	@Override public void handle(final Message message) {
		// The LIST 322 response has the channel name in the second parameter.
		String name = message.getParamAt(1);
		if (!name.contains("#")) {
			return;
		}
		Channel channel = new Channel(name, false);
		addChannelToBrowser(channel);
	}
}

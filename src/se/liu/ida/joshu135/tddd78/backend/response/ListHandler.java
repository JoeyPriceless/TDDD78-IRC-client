package se.liu.ida.joshu135.tddd78.backend.response;

import se.liu.ida.joshu135.tddd78.frontend.ViewMediator;
import se.liu.ida.joshu135.tddd78.models.Channel;
import se.liu.ida.joshu135.tddd78.models.Message;

/**
 * Adds a single channel to the ChannelDialog.
 */
public class ListHandler extends AbstractViewEditor implements ResponseHandler {
	public ListHandler(final ViewMediator mediator) {
		super(mediator);
	}

	@Override public void handle(final Message message) {
		// The LIST 322 response has the channel name in the second parameter.
		String name = message.getParamAt(1);
		if (!name.contains("#")) {
			return;
		}
		Channel channel = new Channel(name, false);
		mediator.addChannelToBrowser(channel);
	}
}

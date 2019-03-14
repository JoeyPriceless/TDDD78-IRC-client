package se.liu.ida.joshu135.tddd78.backend.response;

import se.liu.ida.joshu135.tddd78.frontend.ViewMediator;
import se.liu.ida.joshu135.tddd78.models.Message;

/**
 * Indicates to the ChannelDialog that it's the end of the channel listing.
 */
public class EndListHandler extends AbstractViewEditor implements ResponseHandler {
	public EndListHandler(final ViewMediator mediator) {
		super(mediator);
	}

	@Override public void handle(final Message message) {
		mediator.endOfList();
	}
}

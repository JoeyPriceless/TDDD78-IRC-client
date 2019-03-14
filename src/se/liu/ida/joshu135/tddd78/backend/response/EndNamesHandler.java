package se.liu.ida.joshu135.tddd78.backend.response;

import se.liu.ida.joshu135.tddd78.frontend.ViewMediator;
import se.liu.ida.joshu135.tddd78.models.Message;

/**
 * Indicates to the UserListComponent that it's the end of the user listing.
 */
public class EndNamesHandler extends AbstractViewEditor implements ResponseHandler {
	public EndNamesHandler(final ViewMediator mediator) {
		super(mediator);
	}

	@Override public void handle(final Message message) {
		mediator.endOfName();
	}
}

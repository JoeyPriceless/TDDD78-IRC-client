package se.liu.ida.joshu135.tddd78.backend.response;

import se.liu.ida.joshu135.tddd78.frontend.ViewMediator;
import se.liu.ida.joshu135.tddd78.models.Message;

/**
 * Re-opens the ServerDialog if the server responds with a message indicating that the connection was unsuccessful.
 * The error message is then displayed to the user.
 */
public class ServerDialogHandler extends AbstractViewEditor implements ResponseHandler {
	public ServerDialogHandler(final ViewMediator mediator) {
		super(mediator);
	}

	@Override public void handle(final Message message) {
		String error = message.getTrailing() != null ? message.getTrailing() : message.getParams();
		mediator.showServerDialog(true, error);
	}
}
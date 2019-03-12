package se.liu.ida.joshu135.tddd78.backend.response;

import se.liu.ida.joshu135.tddd78.frontend.ViewMediator;
import se.liu.ida.joshu135.tddd78.models.Message;

public class ServerDialogHandler extends AbstractViewEditor implements ResponseHandler {
	public ServerDialogHandler(final ViewMediator mediator) {
		super(mediator);
	}

	@Override public void handle(final Message message) {
		String error = message.getTrailing() != null ? message.getTrailing() : message.getParams();
		showServerDialog(error);
	}
}
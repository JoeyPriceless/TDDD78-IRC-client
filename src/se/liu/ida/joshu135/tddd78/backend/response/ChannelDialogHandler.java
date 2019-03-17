package se.liu.ida.joshu135.tddd78.backend.response;

import se.liu.ida.joshu135.tddd78.frontend.ViewMediator;
import se.liu.ida.joshu135.tddd78.models.Message;

/**
 *
 */
public class ChannelDialogHandler extends AbstractViewEditor implements ResponseHandler {
	public ChannelDialogHandler(final ViewMediator mediator) {
		super(mediator);
	}

	@Override public void handle(final Message message) {
		mediator.clearUserList();
		mediator.showChannelDialog();
	}
}
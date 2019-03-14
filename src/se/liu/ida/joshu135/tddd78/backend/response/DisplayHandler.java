package se.liu.ida.joshu135.tddd78.backend.response;

import se.liu.ida.joshu135.tddd78.frontend.ViewMediator;
import se.liu.ida.joshu135.tddd78.models.Message;

/**
 * Displays the message to current screen.
 */
public class DisplayHandler extends AbstractViewEditor implements ResponseHandler {
	public DisplayHandler(final ViewMediator mediator) {
		super(mediator);
	}

	@Override public void handle(final Message message)
	{
		mediator.appendToActive(message.getTrailing());
	}
}

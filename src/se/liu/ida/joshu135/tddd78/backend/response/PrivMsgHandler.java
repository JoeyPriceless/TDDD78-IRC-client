package se.liu.ida.joshu135.tddd78.backend.response;

import se.liu.ida.joshu135.tddd78.frontend.ViewMediator;
import se.liu.ida.joshu135.tddd78.models.Message;

/**
 * Notifies of a message sent either in a channel or private conversation.
 */
public class PrivMsgHandler extends AbstractViewEditor implements ResponseHandler {
	public PrivMsgHandler(final ViewMediator mediator) {
		super(mediator);
	}

	@Override public void handle(final Message message)
	{
		// All servers will send a private message with "VERSION" upon registration. These are not displayed to the user.
		if (message.getTrailing().equals("\u0001VERSION\u0001")) return;

		// First param is the message's target (either channel or application's user)
		mediator.appendPrivMsg(message.getParamAt(0), message.getNickname(), message.getTrailing());
	}
}

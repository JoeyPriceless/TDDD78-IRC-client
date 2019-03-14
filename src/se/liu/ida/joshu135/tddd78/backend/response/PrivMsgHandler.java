package se.liu.ida.joshu135.tddd78.backend.response;

import se.liu.ida.joshu135.tddd78.frontend.ViewMediator;
import se.liu.ida.joshu135.tddd78.models.Message;

public class PrivMsgHandler extends AbstractViewEditor implements ResponseHandler {
	public PrivMsgHandler(final ViewMediator mediator) {
		super(mediator);
	}

	@Override public void handle(final Message message)
	{
		// All servers will send a private message with "VERSION" upon registration. These are not displayed to the user.
		if (!message.getTrailing().equals("\u0001VERSION\u0001")) {
			mediator.appendMessage(message.getNickname(), message.getTrailing());
		}
	}
}

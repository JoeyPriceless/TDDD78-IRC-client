package se.liu.ida.joshu135.tddd78.backend.response;

import se.liu.ida.joshu135.tddd78.frontend.ViewMediator;
import se.liu.ida.joshu135.tddd78.models.Message;

/**
 * Some responses come in the form "252 (num) :operator(s) online". These have to be parsed differently.
 */
public class NumParamHandler extends AbstractViewEditor implements ResponseHandler {
	public NumParamHandler(final ViewMediator mediator) {
		super(mediator);
	}

	@Override public void handle(final Message message)
	{
		// Discard the first parameter that is the user's name.
		displayChannelInfoMessage(String.format("%s %s", message.splitParams()[1], message.getTrailing()));
	}
}

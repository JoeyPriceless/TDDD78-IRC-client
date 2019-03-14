package se.liu.ida.joshu135.tddd78.backend.response;

import se.liu.ida.joshu135.tddd78.frontend.ViewMediator;

/**
 * Abstract class to be extended by any ResponseHandler that wishes to edit the user interface through ViewMediator.
 */
public abstract class AbstractViewEditor {
	protected ViewMediator mediator;

	protected AbstractViewEditor(final ViewMediator mediator) {
		this.mediator = mediator;
	}

	protected void displayChannelInfoMessage(String message) {
		mediator.appendToChannel(null, message);
	}
}

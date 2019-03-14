package se.liu.ida.joshu135.tddd78.backend.response;

import se.liu.ida.joshu135.tddd78.frontend.ChatViewer;
import se.liu.ida.joshu135.tddd78.frontend.ViewMediator;
import se.liu.ida.joshu135.tddd78.models.Scope;

/**
 * Abstract class to be extended by any ResponseHandler that wishes to edit the user interface through ChatViewer
 */
public abstract class AbstractViewEditor {
	protected ViewMediator mediator;

	protected AbstractViewEditor(final ViewMediator mediator) {
		this.mediator = mediator;
	}

	protected void displayChannelInfoMessage(String message) {
		mediator.appendToChannel(null, message);
	}

	protected void displayInfoMessage(String message) {
		mediator.appendToActive(message);
	}

	protected void showServerDialog(String errorMessage) {
		mediator.showServerDialog(true, errorMessage);
	}

	protected void showChannelDialog() {
		mediator.clearUserList();
		mediator.showChannelDialog();
	}
}

package se.liu.ida.joshu135.tddd78.backend.response;

import se.liu.ida.joshu135.tddd78.frontend.ViewMediator;
import se.liu.ida.joshu135.tddd78.models.Message;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Adds a range of names to the UserListComponent
 */
public class NamesHandler extends AbstractViewEditor implements ResponseHandler {
	public NamesHandler(final ViewMediator mediator) {
		super(mediator);
	}

	@Override public void handle(final Message message) {
		 Iterable<String> names = new ArrayList<>(Arrays.asList(message.splitTrailing()));
		 mediator.addUsersToList(names);
	}
}

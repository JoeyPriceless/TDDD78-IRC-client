package se.liu.ida.joshu135.tddd78.backend.response;

import se.liu.ida.joshu135.tddd78.frontend.ChatViewer;
import se.liu.ida.joshu135.tddd78.models.Message;

import java.util.ArrayList;
import java.util.Arrays;

public class NamesHandler extends AbstractViewEditor implements ResponseHandler {
	public NamesHandler(final ChatViewer chatViewer) {
		super(chatViewer);
	}

	@Override public void handle(final Message message) {
		 Iterable<String> names = new ArrayList<>(Arrays.asList(message.splitTrailing()));
		 chatViewer.getUserListComponent().addUsers(names);
	}
}

package se.liu.ida.joshu135.tddd78.backend.response;

import se.liu.ida.joshu135.tddd78.frontend.ChatViewer;
import se.liu.ida.joshu135.tddd78.models.Message;

public class EndNamesHandler extends AbstractViewEditor implements ResponseHandler {
	public EndNamesHandler(final ChatViewer chatViewer) {
		super(chatViewer);
	}

	@Override public void handle(final Message message) {
		chatViewer.getUserListComponent().endOfName();
	}
}
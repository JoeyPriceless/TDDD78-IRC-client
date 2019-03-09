package se.liu.ida.joshu135.tddd78.backend.response;

import se.liu.ida.joshu135.tddd78.frontend.ChatViewer;
import se.liu.ida.joshu135.tddd78.models.Message;

public class OpenServerDialogAction extends ViewEditor implements ResponseAction {
	public OpenServerDialogAction(final ChatViewer chatViewer) {
		super(chatViewer);
	}

	@Override public void handle(final Message message) {
		String error = message.getTrailing() != null ? message.getTrailing() : message.getParams();
		showServerDialog(error);
	}
}
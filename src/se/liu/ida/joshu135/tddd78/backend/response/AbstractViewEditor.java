package se.liu.ida.joshu135.tddd78.backend.response;

import se.liu.ida.joshu135.tddd78.frontend.ChatViewer;

/**
 * Abstract class to be extended by any ResponseHandler that wishes to edit the user interface through ChatViewer
 */
public abstract class AbstractViewEditor {
	protected ChatViewer chatViewer;

	protected AbstractViewEditor(final ChatViewer chatViewer) {
		this.chatViewer = chatViewer;
	}

	protected void displayServerMessage(String message) {
		chatViewer.appendToChannel(null, message);
	}

	protected void showServerDialog(String errorMessage) {
		chatViewer.showServerDialog(true, errorMessage);
	}

	protected void showChannelDialog() {
		chatViewer.showChannelDialog();
	}
}

package se.liu.ida.joshu135.tddd78.backend.response;

import se.liu.ida.joshu135.tddd78.frontend.ChatViewer;
import se.liu.ida.joshu135.tddd78.models.Channel;

/**
 * Abstract class to be extended by any ResponseAction that wishes to edit the user interface through ChatViewer
 */
public abstract class ViewEditor {
	private ChatViewer chatViewer;

	protected ViewEditor(final ChatViewer chatViewer) {
		this.chatViewer = chatViewer;
	}

	protected void displayServerMessage(String message) {
		chatViewer.appendToChat(message);
	}

	protected void displayUserMessage(String sender, String message) {
		chatViewer.appendToChat(sender, message);
	}

	protected void showServerDialog() {}

	protected  void showChannelDialog() { chatViewer.showChannelDialog(); }

	protected void addChannelToBrowser(Channel channel) { chatViewer.addChannelToBrowser(channel); }

	protected void endOfList() { chatViewer.endOfList(); }
}

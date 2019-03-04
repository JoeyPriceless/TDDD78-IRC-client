package se.liu.ida.joshu135.tddd78.backend.response;

import se.liu.ida.joshu135.tddd78.frontend.ChatViewer;
import se.liu.ida.joshu135.tddd78.util.Time;

/**
 * Abstract class to be extended by any ResponseAction that wishes to append messages to the chat.
 */
public abstract class ChatWriter {
	private ChatViewer chatViewer;

	public ChatWriter(final ChatViewer chatViewer) {
		this.chatViewer = chatViewer;
	}

	protected void displayServerMessage(String message) {
		chatViewer.appendToChat(message);
	}

	protected void displayUserMessage(String sender, String message) {
		chatViewer.appendToChat(sender, message);
	}
}

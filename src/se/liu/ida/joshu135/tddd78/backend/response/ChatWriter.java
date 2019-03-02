package se.liu.ida.joshu135.tddd78.backend.response;

import se.liu.ida.joshu135.tddd78.frontend.ChatViewer;
import se.liu.ida.joshu135.tddd78.models.Message;
import se.liu.ida.joshu135.tddd78.util.Time;

public abstract class ChatWriter {
	private ChatViewer chatViewer;

	public ChatWriter(final ChatViewer chatViewer) {
		this.chatViewer = chatViewer;
	}

	protected void displayServerMessage(String message) {
		appendToChat(message);
	}

	protected void displayUserMessage (String sender, String message) {
		appendToChat(String.format("<%s> %s", sender, message));
	}

	private void appendToChat(String text) {
		chatViewer.appendToChat(String.format("[%s] %s", Time.timeString(), text));
	}
}

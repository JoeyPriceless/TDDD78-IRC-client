package se.liu.ida.joshu135.tddd78.frontend;

import se.liu.ida.joshu135.tddd78.backend.ConnectionHandler;
import se.liu.ida.joshu135.tddd78.backend.MessageComposer;
import se.liu.ida.joshu135.tddd78.backend.MessageReceiver;
import se.liu.ida.joshu135.tddd78.backend.MessageSender;
import se.liu.ida.joshu135.tddd78.models.Message;
import se.liu.ida.joshu135.tddd78.models.AppUser;

import java.util.concurrent.LinkedTransferQueue;

/**
 * Initializes the application and its GUI.
 */
public final class Main {
	private Main() {}

	// Inspection (Entry Points): Don't see any issue here?
	public static void main(String[] args) {
		LinkedTransferQueue<Message> messageQueue = new LinkedTransferQueue<>();
		MessageComposer composer = new MessageComposer(messageQueue);
		AppUser user = new AppUser();
		ConnectionHandler conHandler = new ConnectionHandler(user);
		ChatViewer chatViewer = new ChatViewer(conHandler, user, composer);

		MessageSender messageSender = new MessageSender(messageQueue, conHandler);
		MessageReceiver messageReceiver = new MessageReceiver(conHandler, composer, chatViewer.getMediator());
		Thread sendThread = new Thread(messageSender, "SendT");
		Thread responseThread = new Thread(messageReceiver, "ResponseT");
		sendThread.start();
		responseThread.start();
	}
}
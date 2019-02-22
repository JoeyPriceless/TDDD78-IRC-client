package se.liu.ida.joshu135.tddd78.frontend;

import se.liu.ida.joshu135.tddd78.backend.ConnectionHandler;
import se.liu.ida.joshu135.tddd78.backend.MessageComposer;
import se.liu.ida.joshu135.tddd78.models.Message;
import se.liu.ida.joshu135.tddd78.models.User;

import java.util.concurrent.LinkedTransferQueue;

public class Cli {
	public static void main(String[] args) {
		String server = "irc.freenode.net";
		int port = 6667;
		String channel = "#bot-test";

		User user = new User("tddd78", "tddd78", "tddd78");
		LinkedTransferQueue<Message> messageQueue = new LinkedTransferQueue<>();
		try {
			Runnable con = new ConnectionHandler(server, port, messageQueue);
			Thread messageThread = new Thread(con, "Message Thread");
			messageThread.start();
			MessageComposer composer = new MessageComposer(messageQueue);
			composer.registerConnection(user);
			composer.joinChannel(channel);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}

package se.liu.ida.joshu135.tddd78.frontend;

import se.liu.ida.joshu135.tddd78.backend.ConnectionHandler;
import se.liu.ida.joshu135.tddd78.backend.MessageComposer;
import se.liu.ida.joshu135.tddd78.backend.MessageReceiver;
import se.liu.ida.joshu135.tddd78.backend.MessageSender;
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
			ConnectionHandler conHandler = new ConnectionHandler(server, port, messageQueue);
			MessageSender messageSender = new MessageSender(messageQueue, conHandler);
			MessageReceiver messageReceiver = new MessageReceiver(conHandler);
			Thread sendThread = new Thread(messageSender, "SendT");
			Thread responseThread = new Thread(messageReceiver, "ResponseT");
			sendThread.start();
			responseThread.start();
			MessageComposer composer = new MessageComposer(messageQueue);
			composer.registerConnection(user);
			composer.joinChannel(channel);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}

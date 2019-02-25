package se.liu.ida.joshu135.tddd78.backend;

import se.liu.ida.joshu135.tddd78.models.Message;
import se.liu.ida.joshu135.tddd78.util.LogConfig;

import java.io.IOException;
import java.util.concurrent.LinkedTransferQueue;
import java.util.logging.Logger;

public class MessageSender implements Runnable {
	private static final Logger LOGGER = LogConfig.getLogger(MessageSender.class.getSimpleName());
	private LinkedTransferQueue<Message> messageQueue; // K: Message, V: Terminator Numeric
	private ConnectionHandler conHandler;

	public MessageSender(final LinkedTransferQueue<Message> messageQueue, final ConnectionHandler conHandler) {
		this.messageQueue = messageQueue;
		this.conHandler = conHandler;
	}

	/**
	 * Infinite loop that handles messages in order and only moves on to the next message after the response has been read.
	 */
	@Override public void run() {
		Message msg;
		while (true) {
			try {
				msg = messageQueue.take();
				conHandler.writeMessage(msg.getMessage());
			} catch (InterruptedException ex) {
				ex.printStackTrace();
				continue;
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
}

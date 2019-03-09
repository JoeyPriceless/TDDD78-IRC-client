package se.liu.ida.joshu135.tddd78.backend;

import se.liu.ida.joshu135.tddd78.models.Message;
import se.liu.ida.joshu135.tddd78.util.LogConfig;

import java.io.IOException;
import java.util.concurrent.LinkedTransferQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Runnable that shares a LinkedTransferQueue with MessageComposer, containing outbound messages.
 * Messages are sent one at a time and in order. Their responses are dealt with asynchronously in MessageReceiver.
 */
public class MessageSender implements Runnable {
	private static final Logger LOGGER = LogConfig.getLogger(MessageSender.class.getSimpleName());
	private LinkedTransferQueue<Message> messageQueue;
	private ConnectionHandler conHandler;

	public MessageSender(final LinkedTransferQueue<Message> messageQueue, final ConnectionHandler conHandler) {
		this.messageQueue = messageQueue;
		this.conHandler = conHandler;
	}

	/**
	 * Infinite loop that handles messages in order and only moves on to the next message after the response has been read.
	 */
	// TODO implement listener
	@Override public void run() {
		final int sleepDuration = 50;
		// Infinite loop is intentional. Could potentially be turned into an event listener.
		//noinspection InfiniteLoopStatement
		while (true) {
			try {
				Message msg = messageQueue.take();
				conHandler.writeMessage(msg.getMessage());
			} catch (InterruptedException ex) {
				LOGGER.log(Level.WARNING, ex.getMessage(), ex);
			} catch (IOException ex) {
				LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
			}
		}
	}
}

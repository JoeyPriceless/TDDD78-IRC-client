package se.liu.ida.joshu135.tddd78.backend;

import se.liu.ida.joshu135.tddd78.models.Message;
import se.liu.ida.joshu135.tddd78.util.LogUtil;

import java.io.IOException;
import java.util.concurrent.LinkedTransferQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Runnable that shares a LinkedTransferQueue with MessageComposer, containing outbound messages.
 * Messages are sent one at a time and in order. Their responses are dealt with asynchronously in MessageReceiver.
 */
public class MessageSender implements Runnable {
	private static final Logger LOGGER = LogUtil.getLogger(MessageSender.class.getSimpleName());
	private LinkedTransferQueue<Message> messageQueue;
	private ConnectionHandler conHandler;

	public MessageSender(final LinkedTransferQueue<Message> messageQueue, final ConnectionHandler conHandler) {
		this.messageQueue = messageQueue;
		this.conHandler = conHandler;
	}

	/**
	 * Infinite loop that handles messages in order and only moves on to the next message after the response has been read.
	 */
	@Override public void run() {
		// Infinite loop is intentional.
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

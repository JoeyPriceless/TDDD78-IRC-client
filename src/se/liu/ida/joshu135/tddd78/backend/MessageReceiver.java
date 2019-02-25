package se.liu.ida.joshu135.tddd78.backend;

import se.liu.ida.joshu135.tddd78.models.Message;
import se.liu.ida.joshu135.tddd78.util.LogConfig;

import java.io.IOException;
import java.util.concurrent.LinkedTransferQueue;
import java.util.logging.Logger;

public class MessageReceiver implements Runnable {
	private static final Logger LOGGER = LogConfig.getLogger(MessageSender.class.getSimpleName());
	private ConnectionHandler conHandler;

	public MessageReceiver(final ConnectionHandler conHandler) {
		this.conHandler = conHandler;
	}

	/**
	 * Infinite loop that handles messages in order and only moves on to the next message after the response has been read.
	 */
	@Override public void run() {
		while (true) {
			try {
				String line = conHandler.readLine();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
}

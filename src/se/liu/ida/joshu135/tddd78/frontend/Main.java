package se.liu.ida.joshu135.tddd78.frontend;

import org.apache.commons.lang3.exception.ExceptionUtils;
import se.liu.ida.joshu135.tddd78.backend.ConnectionHandler;
import se.liu.ida.joshu135.tddd78.backend.MessageComposer;
import se.liu.ida.joshu135.tddd78.backend.MessageReceiver;
import se.liu.ida.joshu135.tddd78.backend.MessageSender;
import se.liu.ida.joshu135.tddd78.models.Message;
import se.liu.ida.joshu135.tddd78.models.User;
import se.liu.ida.joshu135.tddd78.util.LogConfig;

import java.util.concurrent.LinkedTransferQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Initializes the application and it's GUI.
 */
public class Main {
	private static final Logger LOGGER = LogConfig.getLogger(Main.class.getSimpleName());

	public static void main(String[] args) {
		try {
			LinkedTransferQueue<Message> messageQueue = new LinkedTransferQueue<>();
			MessageComposer composer = new MessageComposer(messageQueue);
			ConnectionHandler conHandler = new ConnectionHandler(composer);
			User user = new User();
			ChatViewer chatViewer = new ChatViewer(conHandler, user, composer);

			MessageSender messageSender = new MessageSender(messageQueue, conHandler);
			MessageReceiver messageReceiver = new MessageReceiver(conHandler, composer, chatViewer);
			Thread sendThread = new Thread(messageSender, "SendT");
			Thread responseThread = new Thread(messageReceiver, "ResponseT");
			sendThread.start();
			responseThread.start();
		} catch (Exception ex) {
			LOGGER.log(Level.SEVERE, ExceptionUtils.getStackTrace(ex));
		}
	}
}
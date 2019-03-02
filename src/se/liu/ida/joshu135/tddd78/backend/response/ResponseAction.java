package se.liu.ida.joshu135.tddd78.backend.response;

import se.liu.ida.joshu135.tddd78.backend.MessageComposer;
import se.liu.ida.joshu135.tddd78.frontend.ChatViewer;
import se.liu.ida.joshu135.tddd78.models.Message;

/**
 * Interface that is implemented by any class that deals with the server's responses.
 * Is able to read the response and send responses back to the server through the handle method.
 */
public interface ResponseAction {
	void handle(MessageComposer composer, Message response) throws MessageComposer.MessageLengthException;
}

package se.liu.ida.joshu135.tddd78.backend.response;

import se.liu.ida.joshu135.tddd78.backend.MessageComposer;
import se.liu.ida.joshu135.tddd78.models.Message;

/**
 * Interface that is implemented by any class that wishes to respond back to the server's responses, thus creating a dialog.
 * Grants access to a MessageComposer object.
 */
public interface ResponseDialog extends ResponseAction {
	void handle(MessageComposer composer, Message message);
}

package se.liu.ida.joshu135.tddd78.backend.response;

import se.liu.ida.joshu135.tddd78.backend.MessageComposer;
import se.liu.ida.joshu135.tddd78.models.Message;

public interface ResponseDialog extends ResponseAction {
	void handle(MessageComposer composer, Message message);
}

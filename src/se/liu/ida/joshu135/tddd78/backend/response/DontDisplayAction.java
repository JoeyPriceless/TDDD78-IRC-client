package se.liu.ida.joshu135.tddd78.backend.response;

import se.liu.ida.joshu135.tddd78.backend.MessageComposer;
import se.liu.ida.joshu135.tddd78.backend.MessageComposer.MessageLengthException;
import se.liu.ida.joshu135.tddd78.models.Message;

public class DontDisplayAction implements ResponseAction {

	@Override public void handle(final MessageComposer composer, final Message response)
			throws MessageLengthException
	{

	}
}

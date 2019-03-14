package se.liu.ida.joshu135.tddd78.backend.response;

import se.liu.ida.joshu135.tddd78.models.Message;

/**
 * Doesn't do anything with the message.
 */
public class DontDisplayHandler implements ResponseHandler {

	@Override public void handle(final Message message) {}
}

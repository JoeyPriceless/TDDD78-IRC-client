package se.liu.ida.joshu135.tddd78.backend.response;

import se.liu.ida.joshu135.tddd78.models.Message;

/**
 * Interface that is implemented by any class that deals with the server's responses.
 * Grants the ability to read the response.
 */
public interface ResponseHandler {
	void handle(Message message);
}

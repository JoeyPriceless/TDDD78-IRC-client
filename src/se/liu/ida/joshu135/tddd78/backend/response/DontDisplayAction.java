package se.liu.ida.joshu135.tddd78.backend.response;

import se.liu.ida.joshu135.tddd78.models.Message;

public class DontDisplayAction implements ResponseAction {

	@Override public void handle(final Message message) {}
}

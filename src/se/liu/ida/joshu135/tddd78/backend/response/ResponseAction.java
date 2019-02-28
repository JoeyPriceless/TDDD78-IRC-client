package se.liu.ida.joshu135.tddd78.backend.response;

import se.liu.ida.joshu135.tddd78.backend.MessageComposer;

public interface ResponseAction {
	void handle(MessageComposer composer, String... params) throws MessageComposer.MessageLengthException;
}

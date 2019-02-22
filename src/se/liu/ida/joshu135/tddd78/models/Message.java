package se.liu.ida.joshu135.tddd78.models;

public class Message {
	private String message;
	private String terminatorNumeric;

	public Message(final String message, final String terminatorNumeric) {
		this.message = message;
		this.terminatorNumeric = terminatorNumeric;
	}

	public Message(final String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public String getTerminatorNumeric() {
		return terminatorNumeric;
	}
}

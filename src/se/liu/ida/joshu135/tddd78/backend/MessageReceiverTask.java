package se.liu.ida.joshu135.tddd78.backend;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;

public class MessageReceiverTask implements Runnable {
	private Socket socket;
	private BufferedReader reader;
	private String terminateNumeric;

	public MessageReceiverTask(final Socket socket, final BufferedReader reader, String terminateNumeric) {
		this.socket = socket;
		this.reader = reader;
		this.terminateNumeric = terminateNumeric;
	}

	@Override public void run() {
		try {
			String line;
			while ((line = reader.readLine()) != null) {
				// TODO handle failed login
				System.out.println("Response: " + line);
				// TODO add getNumeric to MessageParser
				if (line.contains(terminateNumeric)) {
					return;
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}

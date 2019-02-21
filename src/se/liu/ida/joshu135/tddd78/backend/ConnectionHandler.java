package se.liu.ida.joshu135.tddd78.backend;


import se.liu.ida.joshu135.tddd78.models.User;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 *
 */
public class ConnectionHandler {
	private Socket socket;
	private BufferedWriter writer;
	private BufferedReader reader;
	private String channel;
	private User user;

	public ConnectionHandler(final String server, final int port, final String channel, final User user)
			throws IOException
	{
		this.socket = new Socket(server, port);
		this.channel = channel;
		this.user = user;
		writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}

	/**
	 * Register a connection with an IRC server. Send NICK & USER message as recommended by RFC 2812
	 * https://tools.ietf.org/html/rfc2812#section-3.1
	 */
	public void registerConnection() {
		try {
			writer.write(MessageComposer.compose("NICK", user.getNickname()));
			writer.write(MessageComposer.compose("USER", user.getUsername(), user.getMode(), "*", ":", user.getRealname()));
			writer.flush();

			String line;
			while(true) {
				line = reader.readLine();
				System.out.println("Response: " + line);
				if (line.contains("004")) {
					break;
				}
			}
		} catch(MessageComposer.MessageLengthException ex) {
			System.out.println(ex.getMessage());
		} catch(IOException ex) {
			System.out.println("Could not read or write message to server");
			System.out.println(ex.getMessage());
		}
	}
}

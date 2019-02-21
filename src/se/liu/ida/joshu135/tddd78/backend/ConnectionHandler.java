package se.liu.ida.joshu135.tddd78.backend;


import se.liu.ida.joshu135.tddd78.models.User;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * Handles the TCP connection to the server and sends it the messages.
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
	 * Takes a variable number of messages and sends them all at once and in order.
	 * @param msgs A list of messages
	 *
	 * @throws IOException
	 */
	public void writeMessage(String... msgs) throws IOException {
		for (String msg : msgs) {
			writer.write(msg);
		}
		writer.flush();
	}

	/**
	 * Register a connection with an IRC server. Send NICK & USER message.
	 * https://tools.ietf.org/html/rfc2812#section-3.1
	 */
	public void registerConnection() {
		try {
			writeMessage();
			String nickMsg = MessageComposer.compose("NICK", user.getNickname());
			String userMsg = MessageComposer.compose("USER", user.getUsername(), user.getMode(), "*", ":", user.getRealname());
			writeMessage(nickMsg, userMsg);

			// TODO handle failed login
			String line;
			while(true) {
				line = reader.readLine();
				System.out.println("Response: " + line);
				if (line.contains("004")) {
					break;
				}
			}
		} catch(MessageComposer.MessageLengthException ex) {
			ex.printStackTrace();
		} catch(IOException ex) {
			System.out.println("Could not read or write message to server");
			ex.printStackTrace();
		}
	}

	public void joinChannel() throws MessageComposer.MessageLengthException, IOException {
		String join = MessageComposer.compose("JOIN", channel);
		writeMessage(join);
	}
}

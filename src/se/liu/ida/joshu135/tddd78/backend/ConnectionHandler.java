package se.liu.ida.joshu135.tddd78.backend;


import se.liu.ida.joshu135.tddd78.models.Message;
import se.liu.ida.joshu135.tddd78.util.LogConfig;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.concurrent.LinkedTransferQueue;
import java.util.logging.Logger;

/**
 * Handles the TCP connection to the server and sends/receives messages on a separate thread.
 * Messages are placed in a LinkedTransferQueue and sent in order. Only moves onto the next message after the response has been
 * read for the current message.
 */
public class ConnectionHandler {
	private static final Logger LOGGER = LogConfig.getLogger(ConnectionHandler.class.getSimpleName());
	private Socket socket;
	private BufferedWriter writer;
	private BufferedReader reader;

	public ConnectionHandler(final String server, final int port)
			throws IOException
	{
		this.socket = new Socket(server, port);
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
			LOGGER.info(msg.trim());
			writer.write(msg);
		}
		writer.flush();
	}

	public String readLine() throws IOException {
		String line = reader.readLine();
		LOGGER.info(line);
		return line;
	}

//	/**
//	 * Join multiple channels, optionally with keys
//	 * @param channelKey A hashmap with each channel to join where key is the channel and value is it's key.
//	 *
//	 * @throws MessageComposer.MessageLengthException
//	 * @throws IOException
//	 */
//	public void joinChannels(HashMap<String, String> channelKey) throws MessageComposer.MessageLengthException, IOException {
//		String join = MessageComposer.compose("JOIN", channel);
//		writeMessage(join);
//	}
}

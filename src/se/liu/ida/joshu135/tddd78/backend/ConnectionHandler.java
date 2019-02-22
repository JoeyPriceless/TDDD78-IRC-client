package se.liu.ida.joshu135.tddd78.backend;


import se.liu.ida.joshu135.tddd78.models.Message;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.concurrent.LinkedTransferQueue;

/**
 * Handles the TCP connection to the server and sends/receives messages on a separate thread.
 * Messages are placed in a LinkedTransferQueue and sent in order. Only moves onto the next message after the response has been
 * read for the current message.
 */
public class ConnectionHandler implements Runnable{
	private Socket socket;
	private BufferedWriter writer;
	private BufferedReader reader;
	private LinkedTransferQueue<Message> messageQueue; // K: Message, V: Terminator Numeric

	public ConnectionHandler(final String server, final int port, LinkedTransferQueue<Message> messageQueue)
			throws IOException
	{
		this.socket = new Socket(server, port);
		writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		this.messageQueue = messageQueue;
	}

	/**
	 * Infinite loop that handles messages in order and only moves on to the next message after the response has been read.
	 */
	@Override public void run() {
		Message msg;
		while (true) {
			try {
				msg = messageQueue.take();
				writeMessage(msg.getMessage());
				readResponse(msg.getTerminatorNumeric());

			} catch (InterruptedException ex) {
				ex.printStackTrace();
				continue;
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * Takes a variable number of messages and sends them all at once and in order.
	 * @param msgs A list of messages
	 *
	 * @throws IOException
	 */
	public void writeMessage(String... msgs) throws IOException {
		for (String msg : msgs) {
			System.out.println("Message: " + msg.trim());
			writer.write(msg);
		}
		writer.flush();
	}

	/**
	 * If a terminateNumeric is given, the server's response is read until it reaches a message with that numeric.
	 * If termianteNumeric is null, no messages are read and the program moves onto the next message.
	 * @param terminateNumeric
	 *
	 * @throws IOException
	 */
	// TODO handle failed commands
	// TODO Respond to PING
	// TODO Think about how terminate without numeric
	public void readResponse(String terminateNumeric) throws IOException {
		String line;
		while ((line = reader.readLine()) != null) {
			System.out.println("Response: " + line);
			// TODO add getNumeric to MessageParser
			if (terminateNumeric == null || line.contains(terminateNumeric)) {
				return;
			}
		}
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

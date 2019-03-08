package se.liu.ida.joshu135.tddd78.backend;


import se.liu.ida.joshu135.tddd78.models.Channel;
import se.liu.ida.joshu135.tddd78.models.Server;
import se.liu.ida.joshu135.tddd78.models.User;
import se.liu.ida.joshu135.tddd78.util.LogConfig;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.logging.Logger;

/**
 * Handles the TCP connection to the server and sends/receives messages to the socket.
 */
public class ConnectionHandler {
	private static final Logger LOGGER = LogConfig.getLogger(ConnectionHandler.class.getSimpleName());
	private Socket socket;
	private BufferedWriter writer;
	private BufferedReader reader;
	private MessageComposer composer;
	private Server server;
	private Channel channel;

	public ConnectionHandler(MessageComposer composer) {
		this.server = new Server();
		this.composer = composer;
		this.writer = null;
		this.reader = null;
	}

	public void setServer(Server server, User user) throws IOException {
		if (this.server.getHostname() != null && this.server.getHostname().equals(server.getHostname()) &&
			this.server.getPort() != 0 && this.server.getPort() == server.getPort()) {
			return;
		}
		this.server = server;
		if (socket != null) { socket.close(); }
		socket = new Socket(server.getHostname(), server.getPort());
		writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		composer.registerConnection(user);
	}

	public void setChannel(final Server server, final String channel)
	{
		if (this.channel != null && this.channel.getName().equals(channel)) {
			return;
		}
		this.channel = new Channel(server, channel);
	}

	public Channel getChannel() {
		return channel;
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

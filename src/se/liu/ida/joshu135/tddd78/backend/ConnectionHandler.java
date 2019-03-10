package se.liu.ida.joshu135.tddd78.backend;


import org.apache.commons.lang3.exception.ExceptionUtils;
import se.liu.ida.joshu135.tddd78.models.Channel;
import se.liu.ida.joshu135.tddd78.models.Server;
import se.liu.ida.joshu135.tddd78.models.User;
import se.liu.ida.joshu135.tddd78.util.LogUtil;

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
	private static final Logger LOGGER = LogUtil.getLogger(ConnectionHandler.class.getSimpleName());
	private Socket socket = null;
	private BufferedWriter writer;
	private BufferedReader reader;
	private MessageComposer composer;
	private Server server;
	private Channel channel = null;
	private User user;

	public ConnectionHandler(MessageComposer composer, User user) {
		this.composer = composer;
		this.user = user;
		this.server = null;
		this.writer = null;
		this.reader = null;
	}

	// IOException is needed for socket, etc and contains UnknownHostException. I Don't see why I would add a redundant
	// UnknownHostException onto a required IOException.
	@SuppressWarnings("OverlyBroadThrowsClause") public void setServer(Server server, User user) throws IOException {
		if (this.server != null && this.server.getHostname().equals(server.getHostname()) &&
			this.server.getPort() != 0 && this.server.getPort() == server.getPort() &&
			this.user.equals(user)) {
			return;
		}
		this.server = server;
		if (socket != null) { socket.close(); }
		socket = new Socket(server.getHostname(), server.getPort());
		writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		composer.registerConnection(user);
	}

	public Server getServer() {
		return server;
	}

	public void setChannel(final Server server, final Channel channel)
	{
		if (this.channel != null && this.channel.equals(channel)) {
			return;
		}
		channel.createNode();
		server.replaceChannel(channel);
		this.channel = channel;
	}

	public void setChannel(Channel channel)
	{
		if (this.channel != null && this.channel.equals(channel)) {
			return;
		}
		this.channel = channel;
	}

	public Channel getChannel() {
		return channel;
	}

	/**
	 * Takes a variable number of messages and sends them all at once and in order.
	 * @param msgs A list of messages
	 *
	 * @throws IOException If an I/O exception occurs in writer.
	 */
	public void writeMessage(String... msgs) throws IOException {
		for (String msg : msgs) {
			LOGGER.info(msg.trim());
			writer.write(msg);
		}
		writer.flush();
	}

	public String readLine() {
		String line = null;
		try {
		 	line = reader.readLine();
			LOGGER.info(line);
		} catch (IOException ex) {
			LOGGER.warning(ExceptionUtils.getStackTrace(ex));
		}
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

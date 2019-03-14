package se.liu.ida.joshu135.tddd78.backend;


import org.apache.commons.lang3.exception.ExceptionUtils;
import se.liu.ida.joshu135.tddd78.models.AppUser;
import se.liu.ida.joshu135.tddd78.models.Server;
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
	private BufferedWriter writer = null;
	private BufferedReader reader = null;
	private MessageComposer composer;
	private Server server = null;
	private AppUser user;

	public ConnectionHandler(MessageComposer composer, AppUser user) {
		this.composer = composer;
		this.user = user;
	}

	// IOException is needed for socket, etc and contains UnknownHostException. I Don't see why I would add a redundant
	// UnknownHostException onto a required IOException.
	public void setServer(Server server, AppUser user) throws IOException {
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
}

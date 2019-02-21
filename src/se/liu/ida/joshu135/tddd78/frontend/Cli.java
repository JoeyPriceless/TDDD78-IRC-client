package se.liu.ida.joshu135.tddd78.frontend;

import se.liu.ida.joshu135.tddd78.backend.ConnectionHandler;
import se.liu.ida.joshu135.tddd78.models.User;

public class Cli {
	public static void main(String[] args) {
		String server = "irc.freenode.net";
		int port = 6667;
		String channel = "#bot-test";

		User user = new User("tddd78", "tddd78", "tddd78");
		try {
			ConnectionHandler connection = new ConnectionHandler(server, port,channel, user);
			connection.registerConnection();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}

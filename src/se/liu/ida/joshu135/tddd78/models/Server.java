package se.liu.ida.joshu135.tddd78.models;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * A server with a hostname, port and JTree node used for displaying it.
 */
public class Server {
	private String hostname = null;
	private int port;
	private DefaultMutableTreeNode node;

	public String getHostname() {
		return hostname;
	}

	public int getPort() {
		return port;
	}

	public Server() {
		node = new DefaultMutableTreeNode(this);
	}

	public Server(final String hostname, final int port) {
		this.hostname = hostname;
		this.port = port;
		node = new DefaultMutableTreeNode(this);
	}

	public DefaultMutableTreeNode getNode() {
		return node;
	}

	@Override public String toString() {
		return hostname;
	}
}

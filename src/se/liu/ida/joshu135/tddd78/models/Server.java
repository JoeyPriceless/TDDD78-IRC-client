package se.liu.ida.joshu135.tddd78.models;

import javax.swing.tree.DefaultMutableTreeNode;

public class Server {
	private String displayName;
	private String hostname;
	private int port;
	private DefaultMutableTreeNode node;

	public String getDisplayName() {
		return displayName;
	}

	public String getHostname() {
		return hostname;
	}

	public void setDisplayName(final String displayName) {
		this.displayName = displayName;
	}

	public int getPort() {
		return port;
	}

	public Server() {
		node = new DefaultMutableTreeNode(this);
	}

	public Server(final String hostname, final int port) {
		this.hostname = hostname;
		// TODO extract displayname from 005 response. "NETWORK=freenode"
		this.displayName = hostname;
		this.port = port;
		node = new DefaultMutableTreeNode(this);
	}

	public DefaultMutableTreeNode getNode() {
		return node;
	}

	@Override public String toString() {
		return displayName;
	}
}

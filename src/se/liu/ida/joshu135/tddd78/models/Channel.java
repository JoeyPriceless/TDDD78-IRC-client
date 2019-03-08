package se.liu.ida.joshu135.tddd78.models;

import javax.swing.tree.DefaultMutableTreeNode;

public class Channel {
	private Server server;
	private String name;
	private DefaultMutableTreeNode node;

	public Channel(final Server server, final String name) {
		this.server = server;
		this.name = name;
		node = new DefaultMutableTreeNode(this);
		setNodeParent();
	}

	/**
	 * Create a channel without adding it's node to the ServerTree.
	 * @param name Channel name, including '#' prefix.
	 */
	public Channel(final String name) {
		this.name = name;
	}

	public Server getServer() {
		return server;
	}

	public String getName() {
		return name;
	}

	private void setNodeParent() {
		server.getNode().add(node);
	}

	@Override public String toString() {
		return name;
	}
}

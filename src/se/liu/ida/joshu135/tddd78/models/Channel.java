package se.liu.ida.joshu135.tddd78.models;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * A server with a name and associated server. Also contains its JTree node.
 */
public class Channel {
	private Server server = null;
	private String name;
	private DefaultMutableTreeNode node = null;

	public Channel(final Server server, final String name) {
		this.name = name;
		setServer(server);
	}

	/**
	 * Create a channel without adding it's node to the ServerTree.
	 * @param name Channel name, including '#' prefix.
	 */
	public Channel(final String name) {
		this.name = name;
	}

	public void setServer(final Server server) {
		this.server = server;
		node = new DefaultMutableTreeNode(this);
		setNodeParent();	}

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

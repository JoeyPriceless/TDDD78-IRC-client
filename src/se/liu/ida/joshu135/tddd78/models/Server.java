package se.liu.ida.joshu135.tddd78.models;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.ArrayList;
import java.util.List;

/**
 * A server with a hostname, port and JTree node used for displaying it.
 */
public class Server {
	private String hostname = null;
	private int port;
	private DefaultMutableTreeNode node;
	private List<Channel> channels;

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
		channels = new ArrayList<>();
	}

	public void addChannel(Channel channel) {
		node.add(channel.getNode());
		channels.add(channel);
	}

	public void removeChannel(Channel channel) {
		node.remove(channel.getNode());
		channels.remove(channel);
	}

	public void replaceChannel(Channel newChannel) {
		for (Channel c : channels) {
			node.remove(c.getNode());
		}
		channels.clear();
		addChannel(newChannel);
	}

	public DefaultMutableTreeNode getNode() {
		return node;
	}

	@Override public String toString() {
		return hostname;
	}
}

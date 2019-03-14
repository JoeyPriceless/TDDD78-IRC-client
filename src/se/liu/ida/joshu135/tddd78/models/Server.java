package se.liu.ida.joshu135.tddd78.models;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.ArrayList;
import java.util.List;

/**
 * A server with a hostname, port and JTree node used for displaying it. Also keeps track of all the node's children. These can
 * be either Channels or Users.
 */
public class Server {
	private String hostname;
	private int port;
	private DefaultMutableTreeNode node;
	private AbstractServerChild activeChild = null;
	private Channel channel = null;
	private List<User> users;

	public String getHostname() {
		return hostname;
	}

	public int getPort() {
		return port;
	}

	public AbstractServerChild getActiveChild() {
		return activeChild;
	}

	public void setActiveChild(final AbstractServerChild activeChild) {
		this.activeChild = activeChild;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel, boolean setActive) {
		if (channel.equals(this.channel)) return;
		this.channel = channel;
		addChild(channel, setActive);
	}

	/**
	 * @param name Name of the user
	 * @param setActive True if the user should be set to be the active child
	 *
	 * @return The requested user. If it does not exist it will be created.
	 */
	public User getUser(String name, boolean setActive) {
		for (User user : users) {
			if (user.getName().equals(name))
				return user;
		}
		// If no such conversation exists, initialize it.
		User newUser = new User(name, false);
		addUser(newUser, setActive);
		return newUser;
	}

	public Server(final String hostname, final int port) {
		this.hostname = hostname;
		this.port = port;
		node = new DefaultMutableTreeNode(this);
		users = new ArrayList<>();
	}

	public void addUser(User user, boolean setActive) {
		if (users.contains(user)) return;
		users.add(user);
		addChild(user, setActive);
	}

	private void addChild(AbstractServerChild child, boolean setActive) {
		if (setActive) {
			activeChild = child;
		}
		child.createNode();
		node.add(child.getNode());
	}

	/**
	 * Resets the current channel and destroys it's node.
	 */
	public void killChannel() {
		if (channel == null) return;
		channel.destroyNode();
		selectActiveChild();
	}

	/**
	 * If activeChild needs to be selected: set it to the channel. If there is not channel, choose the last private message
	 * conversation.
	 */
	private void selectActiveChild() {
		if (activeChild != null) return;
		if (users.isEmpty() && channel == null) return;
		activeChild = channel != null ? channel : users.get(users.size() - 1);
	}

	public void killUsers() {
		for (User u : users) {
			u.destroyNode();
		}
		users.clear();
		selectActiveChild();
	}

	public void replaceChannel(Channel newChannel) {
		if (newChannel.equals(channel)) {
			return;
		}
		killChannel();
		setChannel(newChannel, true);
	}

	/**
	 * Destroy the server node itself, killing the channel and all users.
	 */
	public void destroyNode() {
		killChannel();
		killUsers();
		this.node.removeFromParent();
	}

	public DefaultMutableTreeNode getNode() {
		return node;
	}

	@Override public String toString() {
		return hostname;
	}
}

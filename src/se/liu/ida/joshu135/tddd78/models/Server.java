package se.liu.ida.joshu135.tddd78.models;

import java.util.ArrayList;
import java.util.List;

/**
 * A server with a hostname, port and JTree node used for displaying it. Also keeps track of all the node's children. These can
 * be either Channels or Users.
 */
public class Server extends AbstractChildNode{
	private int port;
	private AbstractChildNode activeNode;
	private Channel channel = null;
	private List<User> users;

	public int getPort() {
		return port;
	}

	public AbstractChildNode getActiveNode() {
		return activeNode;
	}

	public void setActiveNode(final AbstractChildNode activeNode) {
		this.activeNode = activeNode;
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
		super(hostname, true);
		activeNode = this;
		this.port = port;
		users = new ArrayList<>();
	}

	public void addUser(User user, boolean setActive) {
		if (users.contains(user)) return;
		users.add(user);
		addChild(user, setActive);
	}

	private void addChild(AbstractChildNode child, boolean setActive) {
		if (setActive) {
			activeNode = child;
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
	 * If activeNode needs to be selected: set it to the channel. If there is not channel, choose the last private message
	 * conversation.
	 */
	private void selectActiveChild() {
		if (activeNode != null) return;
		if (users.isEmpty() && channel == null) return;
		activeNode = channel != null ? channel : users.get(users.size() - 1);
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
}

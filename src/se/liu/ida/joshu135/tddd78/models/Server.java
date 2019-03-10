package se.liu.ida.joshu135.tddd78.models;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.ArrayList;
import java.util.List;

/**
 * A server with a hostname, port and JTree node used for displaying it.
 */
public class Server {
	private String hostname;
	private int port;
	private DefaultMutableTreeNode node;
	private AbstractServerChild activeChild;
	private List<AbstractServerChild> children;

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

	public Channel getActiveChannel() {
		for (AbstractServerChild child : children) {
			if (child instanceof Channel)
				return (Channel)child;
		}
		return null;
	}

	public User getUser(String name, boolean setActive) {
		for (AbstractServerChild child : children) {
			if (child instanceof User && child.getName().equals(name))
				return (User)child;
		}
		// If no such conversation exists, initialize it.
		User newUser = new User(name, false);
		addChild(newUser, setActive);
		return newUser;
	}

	public Server(final String hostname, final int port) {
		this.hostname = hostname;
		this.port = port;
		node = new DefaultMutableTreeNode(this);
		children = new ArrayList<>();
	}

	public void addChild(AbstractServerChild child, boolean setActive) {
		if (setActive) {
			activeChild = child;
		}
		if (children.contains(child)) {
			return;
		}
		child.createNode();
		node.add(child.getNode());
		children.add(child);
	}

	public void killChild(AbstractServerChild child) {
		children.remove(child);
		if (children.size() == 0) {
			activeChild = null;
		} else if (activeChild.equals(child)) {
			activeChild = children.get(children.size() - 1);
		}
		child.destroyNode();
	}

	// Simply gruesome.
	public void killChildren() {
		for (AbstractServerChild c : children) {
			c.destroyNode();
		}
		children.clear();
		activeChild = null;
	}

	public void replaceChannel(Channel newChannel) {
		if (children.contains(newChannel)) {
			return;
		}
		if (activeChild instanceof Channel) {
			killChild(activeChild);
		}
		addChild(newChannel, true);
	}

	public void destroyNode() {
		killChildren();
		this.node.removeFromParent();
		this.node = null;
	}

	public DefaultMutableTreeNode getNode() {
		return node;
	}

	@Override public String toString() {
		return hostname;
	}
}

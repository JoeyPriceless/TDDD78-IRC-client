package se.liu.ida.joshu135.tddd78.models;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.Objects;

/**
 * A channel with a name and associated server. Also contains its JTree node.
 */
public class Channel {
	private String name;
	private DefaultMutableTreeNode node = null;

	public String getName() {
		return name;
	}

	public DefaultMutableTreeNode getNode() {
		return node;
	}

	public Channel(final String name, boolean shouldCreateNode) {
		this.name = name;
		if (shouldCreateNode) {
			createNode();
		}
	}

	public void createNode() {
		node = new DefaultMutableTreeNode(this);
	}

	public void destroyNode() {
		node.removeFromParent();
		node = null;
	}

	@Override public String toString() {
		return name;
	}

	@Override public boolean equals(final Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		final Channel channel = (Channel) o;
		return Objects.equals(name, channel.name);
	}

	@Override public int hashCode() {
		return Objects.hash(name);
	}
}

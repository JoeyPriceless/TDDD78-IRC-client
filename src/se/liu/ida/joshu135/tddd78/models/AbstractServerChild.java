package se.liu.ida.joshu135.tddd78.models;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.Objects;

/**
 * Abstract class which is extended by classes that are meant to be placed under a server in the ServerTree hierarchy. Manages
 * it's node as well as name and chat history.
 */
public abstract class AbstractServerChild {
	private DefaultMutableTreeNode node = null;
	protected String name;
	protected String history;

	public String getName() {
		return name;
	}

	public DefaultMutableTreeNode getNode() {
		return node;
	}

	public String getHistory() {
		return history;
	}

	public AbstractServerChild(final String name, boolean shouldCreateNode) {
		this.name = name;
		this.history = "";
		if (shouldCreateNode) {
			createNode();
		}
	}

	public void appendText(String text) {
		// Ensure that there's a new line at the end.
		history += (text.stripTrailing() + "\n");
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
		final AbstractServerChild that = (AbstractServerChild) o;
		return Objects.equals(name, that.name);
	}

	@Override public int hashCode() {
		return Objects.hash(name);
	}
}

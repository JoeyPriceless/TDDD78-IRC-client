package se.liu.ida.joshu135.tddd78.models;

/**
 * A user conversation with a name and associated server. Also contains its JTree node as well as the chat's history.
 */
public class User extends AbstractChildNode {
	public User(final String name, final boolean shouldCreateNode) {
		super(name, shouldCreateNode);
	}
}
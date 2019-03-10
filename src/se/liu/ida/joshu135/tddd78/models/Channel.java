package se.liu.ida.joshu135.tddd78.models;

/**
 * A channel with a name and associated server. Also contains its JTree node.
 */
public class Channel extends AbstractServerChild {
	public Channel(final String name, final boolean shouldCreateNode) {
		super(name, shouldCreateNode);
	}
}

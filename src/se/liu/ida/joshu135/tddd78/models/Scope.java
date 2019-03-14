package se.liu.ida.joshu135.tddd78.models;

import org.apache.commons.lang3.StringUtils;

/**
 * Used in UserListComponent to differentiate between listing all users on server or all users in the selected conversation.
 */
public enum Scope {
	/**
	 * An instance of AbstractServerChild
	 */
	SERVERCHILD,
	/**
	 * An in instance of Server
	 */
	SERVER;

	@Override public String toString() {
		switch (this) {
			case SERVERCHILD: return "Channel";
			case SERVER: return "Server";
			default: return StringUtils.capitalize(name().toLowerCase());
		}
	}
}

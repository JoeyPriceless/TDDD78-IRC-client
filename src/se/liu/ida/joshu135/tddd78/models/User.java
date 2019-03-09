package se.liu.ida.joshu135.tddd78.models;

import java.util.Objects;

/**
 * Keeps track of information regarding the application's user such as it's names and status.
 */
public class User {
	private static final int USERNAME_MAX_LENGTH = 9;
	private String nickname = null;
	private String realname = null;
	private String username = null;
	private String mode;

	public String getNickname() {
		return nickname;
	}

	public String getRealname() {
		return realname;
	}

	public String getUsername() {
		return username;
	}

	public String getMode() {
		return mode;
	}

	public User() {
		this.mode = "0";
	}

	public User(final String nickname, final String realname, final String username, final String mode)
			throws IllegalArgumentException {
		setNames(nickname, realname, username);
		this.mode = mode;
	}

	public User(final String nickname, final String realname, final String username) throws IllegalArgumentException {
		setNames(nickname, realname, username);
		this.mode = "0";
	}

	public void setNames(final String nickname, final String realname, final String username) throws IllegalArgumentException {
		this.nickname = nickname;
		this.realname = realname;
		this.username = username;
	}

	@Override public boolean equals(final Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		final User user = (User) o;
		return Objects.equals(nickname, user.nickname) && Objects.equals(realname, user.realname) &&
			   Objects.equals(username, user.username);
	}

	@Override public int hashCode() {
		return Objects.hash(nickname, realname, username);
	}
}

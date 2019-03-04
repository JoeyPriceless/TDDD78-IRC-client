package se.liu.ida.joshu135.tddd78.models;

/**
 * Keeps track of information regarding the application's user such as it's names and status.
 */
public class User {
	private static final int USERNAME_MAX_LENGTH = 9;
	private String nickname;
	private String realname;
	private String username;
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
		if (nickname.length() > USERNAME_MAX_LENGTH) {
			throw new IllegalArgumentException("Username exceeds char limit of " + USERNAME_MAX_LENGTH);
		}
		this.nickname = nickname;
		this.realname = realname;
		this.username = username;
	}
}

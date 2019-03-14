package se.liu.ida.joshu135.tddd78.models;

import java.util.Objects;

/**
 * Keeps track of information regarding the application's user such as it's names and status.
 */
public class AppUser {
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

	public AppUser() {
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
		final AppUser user = (AppUser) o;
		return Objects.equals(nickname, user.nickname) && Objects.equals(realname, user.realname) &&
			   Objects.equals(username, user.username);
	}

	@Override public int hashCode() {
		// Code inspection complained on returning "Objects.hash(nickname, realname, username)" due to performance.
		// I therefore refered to this: https://stackoverflow.com/questions/16824721/generating-hashcode-from-multiple-fields
		return 31 * (nickname.hashCode() + realname.hashCode() + username.hashCode());
	}
}

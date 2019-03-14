package se.liu.ida.joshu135.tddd78.models;

import org.apache.commons.lang3.StringUtils;

public enum Scope {
	CHANNEL,
	SERVER;

	@Override public String toString() {
		return StringUtils.capitalize(name().toLowerCase());
	}
}

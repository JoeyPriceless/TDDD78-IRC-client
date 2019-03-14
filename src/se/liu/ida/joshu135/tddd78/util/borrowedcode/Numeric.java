package se.liu.ida.joshu135.tddd78.util.borrowedcode;

/**
 * Mapping between all numeric replies that are relevant to the application. These are then used by the ResponseHandlerFactory
 * to take the appropriate action to a response.
 * List of Numeric replies: https://tools.ietf.org/html/rfc2812#section-2.4
 * Source : https://stackoverflow.com/a/8490188/4400799 (Converted from C# to Java by myself)
 *
 */
// Inspection claims that some of these are not used but those are covered by ResponseHandlerFactor.addMapRange
public enum Numeric {
	// Initial connection

	/**
	 * 001 :Welcome to the Internet Relay Network (nickname)
	 */
	RPL_WELCOME("001"),

	/**
	 * 002 :Your host is (server), running version (ver)
	 */
	RPL_YOURHOST("002"),

	/**
	 * 003 :This server was created (datetime)
	 */
	RPL_CREATED("003"),

	/**
	 * 004 (server) (ver) (usermode) (chanmode)
	 */
	RPL_MYINFO("004"),

	/**
	 * 375 :- server Message of the Day
	 */
	RPL_MOTDSTART("375"),

	/**
	 * 372 :- (info)
	 */
	RPL_MOTD("372"),

	/**
	 * 376 :End of /MOTD command.
	 */
	RPL_MOTDEND("376"),

	/**
	 * 221 (mode)
	 */
	RPL_UMODEIS("221"),


	// List user

	/**
	 * 251 :There are (user) users and (invis) invisible on (serv) servers
	 */
	RPL_LUSERCLIENT("251"),

	/**
	 * 252 (num) :operator(s) online
	 */
	RPL_LUSEROP("252"),

	/**
	 * 253 (num) :unknown connection(s)
	 */
	RPL_LUSERUNKNOWN("253"),

	/**
	 * 254 (num) :channels formed
	 */
	RPL_LUSERCHANNELS("254"),

	/**
	 * 255 :I have (user) clients and (serv) servers
	 */
	RPL_LUSERME("255"),

	/**
	 * 265 :Current local users: (curr) Max: (max)
	 */
	RPL_LUSERLOCALUSER("265"),

	/**
	 * 266 :Current global users: (curr) Max: (max)
	 */
	RPL_LUSERGLOBALUSER("266"),

	// Post-channel join

	/**
	 * 331 (channel) :No topic is set.
	 */
	RPL_NOTOPIC("331"),

	/**
	 * 332 (channel) :(topic)
	 */
	RPL_TOPIC("332"),

	/**
	 * 333 (channel) (nickname) (time)
	 */
	RPL_TOPICSETBY("333"),

	/**
	 * 353 = (channel) :(names)
	 */
	RPL_NAMREPLY("353"),

	/**
	 * 366 (channel) :End of /NAMES list.
	 */
	RPL_ENDOFNAMES("366"),


	// List

	/**
	 * 321 Channel :Users Name
	 */
	RPL_LISTSTART("321"),

	/**
	 * 322 (channel) (users) :(topic)
	 */
	RPL_LIST("322"),

	/**
	 * 323 :End of /LIST
	 */
	RPL_LISTEND("323"),

	/**
	 * 364 (server) (hub) :(hops) (info)
	 */
	RPL_LINKS("364"),

	/**
	 * 365 (mask) :End of /LINKS list.
	 */
	RPL_ENDOFLINKS("365"),


	// Errors
	/**
	 * 431 :No nickname given
	 */
	ERR_NONICKNAMEGIVEN("431"),

	/**
	 * 432 (nickname) :Erroneus Nickname
	 */
	ERR_ERRONEUSNICKNAME("432"),

	/**
	 * 433 (nickname) :Nickname is already in use.
	 */
	ERR_NICKNAMEINUSE("433"),

	/**
	 * 436 (nickname) :Nickname collision KILL
	 */
	ERR_NICKNAMECOLLISION("436"),

	/**
	 * 437 (channel) :Cannot change nickname while banned on channel
	 */
	ERR_UNAVAILABLERESOURCE("437"),

	/**
	 * 462 :You may not reregister
	 */
	ERR_ALREADYREGISTERED("462"),

	/**
	 * 451 (command) :Register first.
	 */
	ERR_NOTREGISTERED("451");

	private final String numeric;

	/**
	 * @return The integral representation of the numeric
	 */
	public int getInt() {
		return Integer.parseInt(numeric);
	}

	/**
	 * @return The string representation of the numeric
	 */
	public String getNumeric() {
		return numeric;
	}

	Numeric(final String numeric) {
		this.numeric = numeric;
	}
}
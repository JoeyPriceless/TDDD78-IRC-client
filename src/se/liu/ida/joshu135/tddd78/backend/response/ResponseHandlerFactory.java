package se.liu.ida.joshu135.tddd78.backend.response;

import se.liu.ida.joshu135.tddd78.frontend.ViewMediator;
import se.liu.ida.joshu135.tddd78.util.borrowedcode.Numeric;

import java.util.HashMap;
import java.util.Map;

/**
 * Factory class for dynamically creating ResponseHandler objects depending on the recieved command.
 * Contains a mapping between commands (Strings/3-letter numerics) and ResponseHandlers.
 * ReponseHandlers merely act on responses, Correspondents also send a reponse back.
 */
public class ResponseHandlerFactory {
	private ViewMediator mediator;
	private Map<String, ResponseHandler> map;

	public ResponseHandlerFactory(final ViewMediator mediator) {
		this.mediator = mediator;
		setMap();
	}

	/**
	 * Gets an action depending on the given command. If the command cannot be found, a default action will be return.
	 * @param command An IRC command (UPPER CASE or 3-letter numeric). See https://tools.ietf.org/html/rfc2812 for the full list
	 *
	 * @return The ResponseHandler corresponding the command, or if there is no corresponing command, an instance of the default
	 * handler.
	 */
	public ResponseHandler getAction(String command) {
		return map.getOrDefault(command, new DisplayHandler(mediator));
	}

	/**
	 * Initializes the mapping between commands and ResponseActions
	 */
	private void setMap() {
		map = new HashMap<>();
		map.put("PING", new PongHandler());
		map.put("JOIN", new JoinHandler(mediator));
		map.put("QUIT", new QuitHandler(mediator));
		map.put("PART", new QuitHandler(mediator));
		map.put("PRIVMSG", new PrivMsgHandler(mediator));

		// AppUser registration
		addMapRange(Numeric.RPL_WELCOME.getInt(), Numeric.RPL_CREATED.getInt(), new DisplayHandler(mediator));
		map.put(Numeric.RPL_MYINFO.getNumeric(), new ChannelDialogHandler(mediator));
		addMapRange(Numeric.ERR_NONICKNAMEGIVEN.getInt(), Numeric.ERR_UNAVAILABLERESOURCE.getInt(),
					new ServerDialogHandler(mediator));
		addMapList(new String[] {
				Numeric.ERR_ALREADYREGISTERED.getNumeric(),
				Numeric.ERR_NOTREGISTERED.getNumeric()
		}, new ServerDialogHandler(mediator));

		// Server user info
		addMapRange(Numeric.RPL_LUSERCLIENT.getInt(), Numeric.RPL_LUSERME.getInt(), new DisplayHandler(mediator));
		addMapList(new String[] {
					Numeric.RPL_LUSERLOCALUSER.getNumeric(),
					Numeric.RPL_LUSERGLOBALUSER.getNumeric()
		}, new DisplayHandler(mediator));
		addMapRange(Numeric.RPL_LUSEROP.getInt(), Numeric.RPL_LUSERCHANNELS.getInt(), new NumParamHandler(mediator));
		// Message of the Day (MOTD) & topic
		addMapList(new String[] {
					Numeric.RPL_MOTD.getNumeric(),
					Numeric.RPL_MOTDSTART.getNumeric(),
					Numeric.RPL_MOTDEND.getNumeric(),
					Numeric.RPL_TOPIC.getNumeric(),
					Numeric.RPL_NOTOPIC.getNumeric()
		}, new DisplayHandler(mediator));
		map.put(Numeric.RPL_TOPICSETBY.getNumeric(), new DontDisplayHandler());
		// Lists all users
		map.put(Numeric.RPL_NAMREPLY.getNumeric(), new NamesHandler(mediator));
		map.put(Numeric.RPL_ENDOFNAMES.getNumeric(), new EndNamesHandler(mediator));

		// Response to LIST command
		map.put(Numeric.RPL_LIST.getNumeric(), new ListHandler(mediator));
		map.put(Numeric.RPL_LISTEND.getNumeric(), new EndListHandler(mediator));
		addMapList(new String[] {
				Numeric.RPL_LISTSTART.getNumeric(),
				Numeric.RPL_LINKS.getNumeric(),
				Numeric.RPL_ENDOFLINKS.getNumeric(),
		}, new DontDisplayHandler());
	}

	/**
	 * Maps a response to all numerics within a range.
	 * @param start Start of range
	 * @param end End of range
	 * @param action The action to be mapped
	 */
	private void addMapRange(int start, int end, ResponseHandler action) {
		for (int i = start; i <= end; i++) {
			String numeric = String.format("%03d" , Integer.valueOf(i));
			map.put(numeric, action);
		}
	}

	/**
	 * Maps a response to all strings within an array
	 * @param command A list of commands to be mapped.
	 * @param action The action to be mapped
	 */
	private void addMapList(String[] command, ResponseHandler action) {
		for (String s : command) {
			map.put(s, action);
		}
	}
}

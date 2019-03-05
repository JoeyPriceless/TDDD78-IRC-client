package se.liu.ida.joshu135.tddd78.backend.response;

import se.liu.ida.joshu135.tddd78.frontend.ChatViewer;
import se.liu.ida.joshu135.tddd78.models.Numeric;

import java.util.HashMap;
import java.util.Map;

/**
 * Factory class for dynamically creating ResponseAction objects depending on the recieved command.
 * Contains a mapping between commands (Strings/3-letter numerics) and ResponseActions
 */
public class ResponseActionFactory {
	private ChatViewer chatViewer;
	private Map<String, ResponseAction> map;

	public ResponseActionFactory(final ChatViewer chatViewer) {
		this.chatViewer = chatViewer;
		setMap();
	}

	/**
	 * Gets an action depending on the given command. If the command cannot be found, a default action will be return.
	 * @param command An IRC command (UPPER CASE or 3-letter numeric). See https://tools.ietf.org/html/rfc2812 for the full list
	 *
	 * @return The ResponseAction corresponding the command, or if there is no corresponing command, a DefaultAction object.
	 */
	public ResponseAction getAction(String command) {
		return map.getOrDefault(command, new DefaultAction(chatViewer));
	}

	/**
	 * Initializes the mapping between commands and ResponseActions
	 */
	private void setMap() {
		map = new HashMap<>();
		map.put("PING", new PongAction());
		map.put("JOIN", new JoinAction(chatViewer));
		map.put("QUIT", new QuitAction(chatViewer));
		map.put("PART", new QuitAction(chatViewer));
		map.put("PRIVMSG", new PrivMsgAction(chatViewer));

		// User registration
		addMapRange(Numeric.RPL_WELCOME.getInt(), Numeric.RPL_CREATED.getInt(), new DisplayAction(chatViewer));
		addMapList(new int[] {
					Numeric.RPL_MYINFO.getInt(),
					Numeric.RPL_SERVERSUPPORTS.getInt()
		}, new DontDisplayAction());

		// Server user info
		addMapRange(Numeric.RPL_LUSERCLIENT.getInt(), Numeric.RPL_LUSERME.getInt(), new DisplayAction(chatViewer));
		addMapList(new int[] {
					Numeric.RPL_LUSERLOCALUSER.getInt(),
					Numeric.RPL_LUSERGLOBALUSER.getInt()
					}, new DisplayAction(chatViewer));
		addMapRange(Numeric.RPL_LUSEROP.getInt(), Numeric.RPL_LUSERCHANNELS.getInt(), new NumParamAction(chatViewer));
		// Message of the Day (MOTD) & topic
		addMapList(new int[] {
					Numeric.RPL_MOTD.getInt(),
					Numeric.RPL_MOTDSTART.getInt(),
					Numeric.RPL_MOTDEND.getInt(),
					Numeric.RPL_TOPIC.getInt(),
					Numeric.RPL_NOTOPIC.getInt()
					}, new DisplayAction(chatViewer));
		map.put(Numeric.RPL_TOPICSETBY.getNumeric(), new DontDisplayAction());
		// Lists all users on server
		addMapList(new int[] {
					Numeric.RPL_NAMREPLY.getInt(),
					Numeric.RPL_ENDOFNAMES.getInt()
					}, new DontDisplayAction());
	}

	/**
	 * Maps a response to all numerics within a range.
	 * @param start Start of range
	 * @param end End of range
	 * @param action The action to be mapped
	 */
	private void addMapRange(int start, int end, ResponseAction action) {
		for (int i = start; i <= end; i++) {
			//noinspection AutoBoxing not supporting pre 5.0 environments.
			String numeric = String.format("%03d" , i);
			map.put(numeric, action);
		}
	}

	/**
	 * Maps a response to all numerics within an array
	 * @param command A list of commands to be mapped.
	 * @param action The action to be mapped
	 */
	private void addMapList(int[] command, ResponseAction action) {
		for (int i : command) {
			//noinspection AutoBoxing not supporting pre 5.0 environments.
			String numeric = String.format("%03d" , i);
			map.put(numeric, action);
		}
	}

	/**
	 * Maps a response to all strings within an array
	 * @param command A list of commands to be mapped.
	 * @param action The action to be mapped
	 */
	private void addMapList(String[] command, ResponseAction action) {
		for (String s : command) {
			map.put(s, action);
		}
	}
}

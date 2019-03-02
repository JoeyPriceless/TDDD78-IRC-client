package se.liu.ida.joshu135.tddd78.backend.response;

import se.liu.ida.joshu135.tddd78.frontend.ChatViewer;

import java.util.HashMap;
import java.util.Map;

public class ResponseActionFactory {
	private ChatViewer chatViewer;
	private Map<String, ResponseAction> map;

	public ResponseActionFactory(final ChatViewer chatViewer) {
		this.chatViewer = chatViewer;
		setMap();
	}

	public ResponseAction getAction(String command) {
		return map.getOrDefault(command, new DefaultAction(chatViewer));
	}

	private void setMap() {
		map = new HashMap<>();
		map.put("PING", new PongAction());
		map.put("JOIN", new JoinAction(chatViewer));
		map.put("QUIT", new QuitAction(chatViewer));

		// User registration
		addMapRange(map, 1, 4, new DisplayAction(chatViewer));
		// Server user info
		addMapRange(map, 250, 255, new DisplayAction(chatViewer));
		addMapRange(map, 265, 266, new DisplayAction(chatViewer));
		// Message of the Day (MOTD)
		addMapRange(map, new int[] {372, 375, 376}, new DisplayAction(chatViewer));
		// Lists all users on server
		addMapRange(map, new int[] {333, 353, 366}, new DontDisplayAction());
	}

	private void addMapRange(Map<String, ResponseAction> map, int start, int end, ResponseAction action) {
		for (int i = start; i <= end; i++) {
			String numeric = String.format("%03d" , i);
			map.put(numeric, action);
		}
	}

	private void addMapRange(Map<String, ResponseAction> map, int[] command, ResponseAction action) {
		for (int i : command) {
			String numeric = String.format("%03d" , i);
			map.put(numeric, action);
		}
	}

	private void addMapRange(Map<String, ResponseAction> map, String[] command, ResponseAction action) {
		for (String s : command) {
			map.put(s, action);
		}
	}
}

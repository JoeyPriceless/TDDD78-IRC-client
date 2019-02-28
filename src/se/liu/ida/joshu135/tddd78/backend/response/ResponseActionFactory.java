package se.liu.ida.joshu135.tddd78.backend.response;

import java.util.Map;

public class ResponseActionFactory {
	private static Map<String, ResponseAction> map = Map.of(
		"PING", new PingResponse()
	);

	public static ResponseAction getAction(String command) {
		return map.getOrDefault(command, new NoResponse());
	}
}

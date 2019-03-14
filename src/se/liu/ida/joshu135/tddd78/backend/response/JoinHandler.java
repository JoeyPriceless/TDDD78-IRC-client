package se.liu.ida.joshu135.tddd78.backend.response;

import se.liu.ida.joshu135.tddd78.frontend.ViewMediator;
import se.liu.ida.joshu135.tddd78.models.Message;

public class JoinHandler extends AbstractViewEditor implements ResponseHandler {
	public JoinHandler(final ViewMediator mediator) {
		super(mediator);
	}

	@Override public void handle(final Message message)
	{
		// Varies between servers whether channel name is in params or trailing...
		String nickname = message.getNickname();
		String channelName = message.getParams().isEmpty() ? message.getTrailing() : message.getParams();
		displayChannelInfoMessage(String.format("%s (%s) joins %s", nickname, message.getUserHost(),
												channelName));
		mediator.addUserToList(nickname);
	}
}

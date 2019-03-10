package se.liu.ida.joshu135.tddd78.backend.response;

import se.liu.ida.joshu135.tddd78.backend.MessageComposer;
import se.liu.ida.joshu135.tddd78.frontend.ChatViewer;
import se.liu.ida.joshu135.tddd78.models.Message;

public class ChannelDialogHandler extends AbstractViewEditor implements Correspondent {

	public ChannelDialogHandler(final ChatViewer chatViewer) {
		super(chatViewer);
	}

	@Override public void handle(final MessageComposer composer, final Message message) {
		showChannelDialog();
	}

	@Override public void handle(final Message message) {
		throw new UnsupportedOperationException("Action requires access to MessageComposer.");
	}
}
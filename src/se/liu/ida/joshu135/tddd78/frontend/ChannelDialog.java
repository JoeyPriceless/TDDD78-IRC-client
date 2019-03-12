package se.liu.ida.joshu135.tddd78.frontend;

import se.liu.ida.joshu135.tddd78.backend.ConnectionHandler;
import se.liu.ida.joshu135.tddd78.backend.MessageComposer;
import se.liu.ida.joshu135.tddd78.models.Channel;
import se.liu.ida.joshu135.tddd78.models.Server;
import se.liu.ida.joshu135.tddd78.util.LogUtil;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Logger;

/**
 * Dialog that opens a channel browser. The object is created and continually supplied with channel names by LIST responses.
 * Once the end of the responses has been indicated, the dialog is updated with the results. Run in a seperate thread to
 * avoid blocking the receiving thread.
 */
public class ChannelDialog extends JScrollPane implements Runnable {
	private static final Channel PLACEHOLDER_CHANNEL = new Channel("Loading... Please wait :)", false);
	private static final int HEIGHT = 500;
	private static final int WIDTH = 150;
	private Server server;
	private Channel selectedChannel = null;
	private ChatViewer chatViewer;
	private MessageComposer composer;
	private JList<Channel> channelList;
	private DefaultListModel<Channel> channelListModel;

	public ChannelDialog(ConnectionHandler connectionHandler, MessageComposer composer, ChatViewer chatViewer) {
		super();
		this.composer = composer;
		this.chatViewer = chatViewer;
		this.server = connectionHandler.getServer();
		channelListModel = new DefaultListModel<>();
		DefaultListModel<Channel> placeholderModel = new DefaultListModel<>();
		placeholderModel.addElement(PLACEHOLDER_CHANNEL);
		channelList = new JList<>(placeholderModel);
		channelList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setViewportView(channelList);
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		channelList.addListSelectionListener(e -> selectedChannel = channelList.getSelectedValue());
	}

	@Override public void run() {
		// Loop re-opens dialog if no channel was selected.
		boolean isDone = false;
		while (!isDone) {
			int result = JOptionPane.showConfirmDialog(null, this, "Channel browser", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
			if (result == JOptionPane.OK_OPTION) {
				if (selectedChannel == null) continue;
				if (selectedChannel.equals(server.getActiveChild())) return;
				server.replaceChannel(selectedChannel);
				composer.joinChannel(selectedChannel.getName());
				chatViewer.changeViewSource(selectedChannel);
				isDone = true;
			} else {
				return;
			}
		}
	}

	public void addChannel(Channel channel) {
		channelListModel.addElement(channel);
	}

	public void endOfList() {
		// Remove placeholder element and replace it with the finalized list.
		if (!channelListModel.isEmpty() && channelListModel.getElementAt(0).equals(PLACEHOLDER_CHANNEL)) {
			channelListModel.removeElementAt(0);
		}
		channelList.setModel(channelListModel);
	}
}

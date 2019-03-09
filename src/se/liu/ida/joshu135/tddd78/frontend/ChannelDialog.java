package se.liu.ida.joshu135.tddd78.frontend;

import se.liu.ida.joshu135.tddd78.backend.ConnectionHandler;
import se.liu.ida.joshu135.tddd78.backend.MessageComposer;
import se.liu.ida.joshu135.tddd78.models.Channel;
import se.liu.ida.joshu135.tddd78.models.Server;
import se.liu.ida.joshu135.tddd78.util.LogConfig;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Logger;

/**
 * Dialog that opens a channel browser. The object is created and continually supplied with channel names by LIST responses.
 * Once the end of the responses has been indicated, the dialog is updated with the results. Run in a seperate thread to
 * avoid blocking the receiving thread.
 */
public class ChannelDialog extends JScrollPane implements Runnable {
	private static final Logger LOGGER = LogConfig.getLogger(ChannelDialog.class.getSimpleName());
	private static final int HEIGHT = 500;
	private static final int WIDTH = 150;
	private Server server;
	private Channel selectedChannel = null;
	private ConnectionHandler connectionHandler;
	private MessageComposer composer;
	private JList<Channel> channelList;
	private DefaultListModel<Channel> model;

	public ChannelDialog(final ConnectionHandler connectionHandler, MessageComposer composer) {
		super();
		this.connectionHandler = connectionHandler;
		this.composer = composer;
		this.server = connectionHandler.getServer();
		model = new DefaultListModel<>();
		DefaultListModel<Channel> placeholderModel = new DefaultListModel<>();
		placeholderModel.addElement(new Channel("Loading... Please wait :)"));
		channelList = new JList<>(placeholderModel);
		channelList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setViewportView(channelList);
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		channelList.addListSelectionListener(e -> selectedChannel = channelList.getSelectedValue());
	}

	@Override public void run() {
		int result = JOptionPane.showConfirmDialog(null, this, "Channel browser", JOptionPane.OK_CANCEL_OPTION,
																   JOptionPane.PLAIN_MESSAGE);
		if (result == JOptionPane.OK_OPTION) {
			selectedChannel.setServer(server);
			connectionHandler.setChannel(selectedChannel);
			composer.joinChannel(selectedChannel.getName());
		}
	}

	public void addChannel(Channel channel) {
		model.addElement(channel);
	}

	// TODO tree structure is not automatically updated.
	public void endOfList() {
		// Remove placeholder element and replace it with the finalized list.
		model.removeElementAt(0);
		channelList.setModel(model);
	}
}

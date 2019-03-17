package se.liu.ida.joshu135.tddd78.frontend;

import se.liu.ida.joshu135.tddd78.models.Channel;

import javax.swing.*;
import java.awt.*;

/**
 * Dialog that opens a channel browser. The object is created and continually supplied with channel names by LIST responses.
 * Once the end of the responses has been indicated, the dialog is updated with the results. Run in a seperate thread to
 * avoid blocking the receiving thread.
 */
public class ChannelDialog extends JScrollPane implements Runnable {
	private static final int HEIGHT = 500;
	private static final int WIDTH = 150;
	private Channel selectedChannel = null;
	private JList<Channel> channelList;
	private DefaultListModel<Channel> channelListModel;
	private ViewMediator mediator;
	private Component parentComponent;

	public ChannelDialog(ViewMediator mediator, Component parentComponent) {
		super();
		this.mediator = mediator;
		this.parentComponent = parentComponent;
		channelListModel = new DefaultListModel<>();
		channelList = new JList<>(channelListModel);
		channelList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setViewportView(channelList);
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		channelList.addListSelectionListener(e -> selectedChannel = channelList.getSelectedValue());
	}

	@Override public void run() {
		// Loop re-opens dialog if no channel was selected.
		boolean isDone = false;
		while (!isDone) {
			int result = JOptionPane.showConfirmDialog(parentComponent, this, "Channel browser", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
			if (result == JOptionPane.OK_OPTION) {
				if (selectedChannel == null) continue;
				if (selectedChannel.equals(mediator.getServer().getActiveNode())) return;
				mediator.changeChannel(selectedChannel);
				isDone = true;
			} else {
				return;
			}
		}
	}

	public void addChannel(Channel channel) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override public void run() {
				channelListModel.addElement(channel);
			}
		});
	}
}

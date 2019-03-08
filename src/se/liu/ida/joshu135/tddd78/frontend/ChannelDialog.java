package se.liu.ida.joshu135.tddd78.frontend;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import se.liu.ida.joshu135.tddd78.backend.ConnectionHandler;
import se.liu.ida.joshu135.tddd78.models.Channel;
import se.liu.ida.joshu135.tddd78.models.Server;
import se.liu.ida.joshu135.tddd78.util.LogConfig;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChannelDialog extends JScrollPane implements Runnable {
	private static final Logger LOGGER = LogConfig.getLogger(ChannelDialog.class.getSimpleName());
	private static final int HEIGHT = 500;
	private static final int WIDTH = 150;
	private Server server;
	private ConnectionHandler connectionHandler;
	private JList<Channel> channelList;
	private DefaultListModel<Channel> model;

	public ChannelDialog(final ConnectionHandler connectionHandler) {
		this.connectionHandler = connectionHandler;
		this.server = connectionHandler.getServer();
		model = new DefaultListModel<>();
		channelList = new JList<>(model);
		channelList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setViewportView(channelList);
		horizontalScrollBarPolicy = HORIZONTAL_SCROLLBAR_NEVER;
		verticalScrollBarPolicy = VERTICAL_SCROLLBAR_AS_NEEDED;
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
	}

	@Override public void run() {
		int result = JOptionPane.showConfirmDialog(null, this, "Channel browser", JOptionPane.OK_CANCEL_OPTION,
																   JOptionPane.PLAIN_MESSAGE);
	}

//	public static void showDialog(ConnectionHandler connectionHandler) {
//		ChannelDialog channelDialog = new ChannelDialog(connectionHandler);
//		int result = JOptionPane.showConfirmDialog(null, channelDialog, "Channel browser", JOptionPane.OK_CANCEL_OPTION,
//																   JOptionPane.PLAIN_MESSAGE);
//	}

	public void addChannel(Channel channel) {
		model.addElement(channel);
		if (model.getSize() % 10 == 0) {
			channelList.updateUI();
		}
	}

	public void endOfList() {
		// Sometimes the UI would not update at the end due to timing issues.
		// This method tries retries the operation until it goes through.
		while (true) {
			try {
				Thread.sleep(10);
				channelList.updateUI();
				return;
			} catch (NullPointerException ex) {
				LOGGER.log(Level.WARNING, ExceptionUtils.getStackTrace(ex));
			} catch (InterruptedException ex) {

			}
		}
	}
}

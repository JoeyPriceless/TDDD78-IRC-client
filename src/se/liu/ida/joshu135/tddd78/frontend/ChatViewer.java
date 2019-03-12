package se.liu.ida.joshu135.tddd78.frontend;

import net.miginfocom.swing.MigLayout;
import se.liu.ida.joshu135.tddd78.backend.ConnectionHandler;
import se.liu.ida.joshu135.tddd78.backend.MessageComposer;
import se.liu.ida.joshu135.tddd78.models.AbstractServerChild;
import se.liu.ida.joshu135.tddd78.models.AppUser;
import se.liu.ida.joshu135.tddd78.models.Channel;
import se.liu.ida.joshu135.tddd78.models.Message;
import se.liu.ida.joshu135.tddd78.models.Server;
import se.liu.ida.joshu135.tddd78.models.User;
import se.liu.ida.joshu135.tddd78.util.Time;

import javax.swing.*;
import java.awt.*;

/**
 * Contains the main JFrame and acts as the backend's interface for GUI management.
 */
public class ChatViewer {
	private static final int DEFAULT_WIDTH = 1400;
	private static final int DEFAULT_HEIGHT = 700;
	private JFrame frame;
	private ChatComponent chatComponent;
	private ServerTreeComponent serverTreeComponent;
	private UserListComponent userListComponent;
	private ChannelDialog channelDialog = null;
	private ConnectionHandler connectionHandler;
	private MessageComposer composer;
	private AppUser user;

	public UserListComponent getUserListComponent() {
		return userListComponent;
	}

	public ChatViewer(ConnectionHandler connectionHandler, AppUser user, MessageComposer composer) {
		this.connectionHandler = connectionHandler;
		this.user = user;
		this.composer = composer;
		frame = new JFrame("IRC");
		frame.setLayout(new MigLayout("fill"));
		frame.setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		chatComponent = new ChatComponent();
		final AuthorComponent authorComponent = new AuthorComponent(this);
		userListComponent = new UserListComponent(this, connectionHandler);
		serverTreeComponent = new ServerTreeComponent(userListComponent, chatComponent);
		frame.add(serverTreeComponent, "dock west");
		frame.add(userListComponent, "dock east");
		frame.add(authorComponent, "dock south");
		frame.add(chatComponent, "grow");
		createMenu();
		frame.pack();
		frame.setLocationRelativeTo(null); // Centralize on screen
		frame.setVisible(true);
		showServerDialog(true);
	}

	/**
	 * Create a menu bar and populate it
	 */
	private void createMenu() {
		final JMenuBar menuBar = new JMenuBar();
		JMenu connectionMenu = new JMenu("Connection");
		menuBar.add(connectionMenu);
		JMenuItem serverConfig = new JMenuItem("Server configuration");
		serverConfig.addActionListener(e -> showServerDialog(false));
		JMenuItem channelConfig = new JMenuItem("Channel browser");
		channelConfig.addActionListener(e -> showChannelDialog());
		connectionMenu.add(serverConfig);
		connectionMenu.add(channelConfig);

		JMenu statusMenu = new JMenu("Status");
		menuBar.add(statusMenu);
		String defaultAway = "Set Away status";
		JMenuItem awayStatus = new JMenuItem(defaultAway);
		awayStatus.addActionListener(e -> {
			String messageString;
			if (awayStatus.getText().equals(defaultAway)) {
				String input = JOptionPane.showInputDialog(frame, "Away message:");
				// Default message is "Away"
				String awayMessage = input.isEmpty() ? "Away" : input;
				awayStatus.setText("Remove Away status");
				messageString = MessageComposer.compose("AWAY", awayMessage);
			} else {
				awayStatus.setText(defaultAway);
				messageString = MessageComposer.compose("AWAY");
			}

			composer.queueMessage(new Message(messageString));
		});
		statusMenu.add(awayStatus);

		frame.setJMenuBar(menuBar);
	}

	public void changeViewSource(AbstractServerChild selectedChild) {
		Server server = connectionHandler.getServer();
		// Updates the tree data model and shows the new channel.
		userListComponent.clear();
		server.setActiveChild(selectedChild);
		serverTreeComponent.updateStructure(server.getNode(), selectedChild.getNode());
	}

	public void appendPrivMsg(String sender, String text) {
		 if (!sender.startsWith("#")) {
		 	appendToUser(sender, sender, text);
		 } else {
		 	appendToChannel(sender, text);
		 }
	}

	public void appendToChannel(String sender, String text) {
		Channel activeChannel = connectionHandler.getServer().getChannel();
		String message = formatMessage(sender, text);
		if (activeChannel == null) {
			chatComponent.appendText(message);
		} else {
			activeChannel.appendText(message);
		}
		chatComponent.update();
	}

	private void appendToUser(String sender, String target, String text) {
		Server server = connectionHandler.getServer();
		User user = server.getUser(target, false);
		user.appendText(formatMessage(sender, text));
		// Update tree display to display possible new conversations.
		serverTreeComponent.updateStructure(server.getNode(), user.getNode());
		chatComponent.update();
	}

	public void submitMessage(String text) {
		String target = connectionHandler.getServer().getActiveChild().getName();
		String userName = user.getNickname();
		composer.sendPrivMsg(target, text);
		if (!target.startsWith("#")) {
			appendToUser(userName, target, text);
		} else {
			appendToChannel(userName, text);
		}
	}

	private String formatMessage(String sender, String text) {
		if (sender == null) {
			return String.format("[%s] %s", Time.timeString(), text);
		} else {
			return String.format("[%s] <%s> %s", Time.timeString(), sender, text);
		}
	}

	public void showServerDialog(boolean inputRequired, String errorMessage) {
		ServerDialog.show(frame, user, connectionHandler, serverTreeComponent, inputRequired, errorMessage);
	}

	public void showServerDialog(boolean inputRequired) {
		ServerDialog.show(frame, user, connectionHandler, serverTreeComponent, inputRequired);
	}

	public void showChannelDialog() {
		channelDialog = new ChannelDialog(connectionHandler, composer, this);
		composer.listChannels();
		// Since this command is called on MessageReceiver's thread (SenderT), the dialog has to be opened on another thread
		// in order to not incoming server messages.
		Thread channelDialogThread = new Thread(channelDialog, "ChannelT");
		channelDialogThread.start();
	}

	public void addChannelToBrowser(Channel channel) {
		channelDialog.addChannel(channel);
	}

	public void endOfList() {
		channelDialog.endOfList();
		//serverTreeComponent.expandTree();
	}

	public void requestNames(boolean onlyChannel) {
		String messageString;
		// If user selects all names on server, it is always granted. If the user is an a channel and selects channel names,
		// request all names in channel. If the user is in a private conversation, simply set the UserList to the name of the
		// recipient.
		if (onlyChannel) {
			AbstractServerChild child = connectionHandler.getServer().getActiveChild();
			// Inspection: this is a clear case where different actions need to happen depending on if the child is a user or
			// channel. Can't simply abstract this away in AbstractServerChild.
			if (child instanceof Channel) {
				messageString = MessageComposer.compose("NAMES", child.getName());
			} else {
				userListComponent.clear();
				if (child != null) {
					userListComponent.addUser(child.getName());
				}
				return;
			}
		} else {
			messageString = MessageComposer.compose("NAMES");
		}
		composer.queueMessage(new Message(messageString));
	}
}

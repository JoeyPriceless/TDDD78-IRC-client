package se.liu.ida.joshu135.tddd78.frontend;

import net.miginfocom.swing.MigLayout;
import se.liu.ida.joshu135.tddd78.backend.ConnectionHandler;
import se.liu.ida.joshu135.tddd78.backend.MessageComposer;
import se.liu.ida.joshu135.tddd78.models.Channel;
import se.liu.ida.joshu135.tddd78.models.User;
import se.liu.ida.joshu135.tddd78.util.LogConfig;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Logger;

import se.liu.ida.joshu135.tddd78.util.Time;

/**
 * Contains the main Jframe and acts as the backend's interface for GUI management.
 */
public class ChatViewer {
	private static final Logger LOGGER = LogConfig.getLogger(ChatViewer.class.getSimpleName());
	private static final int DEFAULT_WIDTH = 1400;
	private static final int DEFAULT_HEIGHT = 700;
	private JFrame frame;
	private ChatComponent chatComponent;
	private AuthorComponent authorComponent;
	private ServerTreeComponent serverTreeComponent;
	private ChannelDialog channelDialog = null;
	private ConnectionHandler connectionHandler;
	private MessageComposer composer;
	private User user;

	public Channel getChannel() {
		return connectionHandler.getChannel();
	}

	public ChatViewer(ConnectionHandler connectionHandler, User user, MessageComposer composer) {
		this.connectionHandler = connectionHandler;
		this.user = user;
		this.composer = composer;
		frame = new JFrame("IRC");
		frame.setLayout(new MigLayout("fill"));
		frame.setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		chatComponent = new ChatComponent();
		authorComponent = new AuthorComponent(this);
		serverTreeComponent = new ServerTreeComponent();
		frame.add(serverTreeComponent, "dock west");
		frame.add(chatComponent, "grow");
		frame.add(authorComponent, "dock south");
		ServerDialog.show(user, connectionHandler, serverTreeComponent, true);
		createMenu();
		frame.pack();
		frame.setLocationRelativeTo(null); // Centralize on screen
		frame.setVisible(true);
	}

	/**
	 * Create a menu bar and populate it
	 */
	private void createMenu() {
		final JMenuBar menuBar = new JMenuBar();
		JMenu connectionMenu = new JMenu("Connection");
		menuBar.add(connectionMenu);
		JMenuItem serverConfig = new JMenuItem("Server configuration");
		serverConfig.addActionListener(e -> ServerDialog.show(user, connectionHandler, serverTreeComponent,
															  false));
		JMenuItem channelConfig = new JMenuItem("Channel browser");
		channelConfig.addActionListener(e -> showChannelDialog());
		connectionMenu.add(serverConfig);
		connectionMenu.add(channelConfig);

		frame.setJMenuBar(menuBar);
	}

	public void appendToChat(String text) {
		chatComponent.appendText(String.format("[%s] %s", Time.timeString(), text));
	}

	public void appendToChat(String sender, String text) {
		appendToChat(String.format("<%s> %s", sender, text));
	}

	public void submitMessage(String text) {
		composer.sendChannelMessage(connectionHandler.getChannel().getName(), text);
		appendToChat(user.getNickname(), text);
	}

	public void showChannelDialog() {
		channelDialog = new ChannelDialog(connectionHandler, composer, chatComponent, serverTreeComponent);
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
		serverTreeComponent.expandTree();
	}
}

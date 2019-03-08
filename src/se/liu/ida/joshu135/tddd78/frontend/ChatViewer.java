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
	private ChannelDialog channelDialog;
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
		JButton sendButton = new JButton("Send");
		sendButton.addActionListener(e -> {
			//authorComponent.submitMessage();
			composer.listChannels();
		});
		frame.add(serverTreeComponent, "cell 0 0 1 2, grow");
		frame.add(chatComponent, "cell 1 0 6 1, grow");
		frame.add(authorComponent, "cell 1 1 6 1, growx");
		frame.add(sendButton, "cell 1 1");
		ServerDialog.show(user, connectionHandler, chatComponent, serverTreeComponent, true);
		createMenu();
		frame.pack();
		frame.setLocationRelativeTo(null); // Centralize on screen
		frame.setVisible(true);
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
		channelDialog = new ChannelDialog(connectionHandler);
		Thread channelDialogThread = new Thread(channelDialog, "ChannelT");
		channelDialogThread.start();
		composer.listChannels();
	}

	public void addChannelToBrowser(Channel channel) {
		channelDialog.addChannel(channel);
	}

	public void endOfList() {
		channelDialog.endOfList();
	}

	/**
	 * Create a menu bar and populate it
	 */
	private void createMenu() {
		final JMenuBar menuBar = new JMenuBar();
		JMenu connectionMenu = new JMenu("Connection");
		menuBar.add(connectionMenu);
		JMenuItem serverConfig = new JMenuItem("Server configuration");
		serverConfig.addActionListener(e -> ServerDialog.show(user, connectionHandler, chatComponent, serverTreeComponent,
															  false));
		connectionMenu.add(serverConfig);

		frame.setJMenuBar(menuBar);
	}
}

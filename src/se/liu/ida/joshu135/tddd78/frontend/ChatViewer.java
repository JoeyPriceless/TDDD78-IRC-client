package se.liu.ida.joshu135.tddd78.frontend;

import net.miginfocom.swing.MigLayout;
import se.liu.ida.joshu135.tddd78.backend.ConnectionHandler;
import se.liu.ida.joshu135.tddd78.backend.MessageComposer;
import se.liu.ida.joshu135.tddd78.backend.MessageComposer.MessageLengthException;
import se.liu.ida.joshu135.tddd78.models.User;
import se.liu.ida.joshu135.tddd78.util.LogConfig;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.exception.ExceptionUtils;
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
	private ConnectionHandler connectionHandler;
	private MessageComposer composer;
	private User user;

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
		JButton sendButton = new JButton("Send");
		sendButton.addActionListener(e -> authorComponent.submitMessage());
		frame.add(chatComponent, "grow, span 2, wrap");
		frame.add(authorComponent, "growx");
		frame.add(sendButton);
		showServerDialog(true);
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
		try {
			composer.sendChannelMessage(connectionHandler.getChannel(), text);
		} catch (MessageLengthException ex) {
			LOGGER.warning(ex.getMessage());
		}
		appendToChat(user.getNickname(), text);
	}

	// TODO add port/channel formatting errors and handle IO exception
	// TODO add field for user mode and handle errors
	public void showServerDialog(boolean inputRequired) {
		final int fieldWidth = 10;
		final int shortFieldWidth = 4;
		// Includes default settings.
		JTextField serverField = new JTextField("irc.freenode.net", fieldWidth);
		JTextField portField = new JTextField("6667", shortFieldWidth);
		JTextField channelField = new JTextField("#freenode", fieldWidth);
		JPanel panel = new JPanel(new MigLayout());
		panel.add(new JLabel("Server configuration"), "span 2, align center, wrap 5");
		panel.add(new JLabel("Hostname :"), "align right");
		panel.add((serverField), "wrap");
		panel.add(new JLabel("Port : "), "align right");
		panel.add(portField, "wrap");
		panel.add(new JLabel("Channel :"), "align right");
		panel.add((channelField), "wrap 15");

		JTextField userNameField = new JTextField("LiULouie", fieldWidth);
		JTextField realNameField = new JTextField("Louis from LiU", fieldWidth);
		JTextField nickNameField = new JTextField("LiULou", fieldWidth);
		panel.add(new JLabel("User configuration"), "span 2, align center, wrap 5");
		panel.add(new JLabel("Username :"), "align right");
		panel.add((userNameField), "wrap");
		panel.add(new JLabel("Real Name :"), "align right");
		panel.add(realNameField, "wrap");
		panel.add(new JLabel("Nickname :"), "align right");
		panel.add((nickNameField));

		boolean isDone = false;
		while (!isDone) {
			try {
				int result = JOptionPane.showConfirmDialog(null, panel, "Connect to a server", JOptionPane.OK_CANCEL_OPTION);
				if (result == JOptionPane.OK_OPTION) {
					user.setNames(nickNameField.getText(), realNameField.getText(), userNameField.getText());
					connectionHandler.setServer(serverField.getText(), Integer.parseInt(portField.getText()), user);
					connectionHandler.setChannel(channelField.getText());
					chatComponent.clearChat();
					isDone = true;
				} else if (inputRequired) {
					// If input is required to continue and user dismisses the window, exit the program.
					System.exit(0);
				} else {
					return;
				}
			} catch (Exception ex) {
				LOGGER.log(Level.WARNING, ExceptionUtils.getStackTrace(ex));
			}
		}
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
		connectionMenu.add(serverConfig);

		frame.setJMenuBar(menuBar);
	}
}

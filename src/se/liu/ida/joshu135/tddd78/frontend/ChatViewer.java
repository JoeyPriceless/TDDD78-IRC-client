package se.liu.ida.joshu135.tddd78.frontend;

import net.miginfocom.swing.MigLayout;
import se.liu.ida.joshu135.tddd78.backend.ConnectionHandler;
import se.liu.ida.joshu135.tddd78.backend.MessageComposer;
import se.liu.ida.joshu135.tddd78.models.User;
import se.liu.ida.joshu135.tddd78.util.LogConfig;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
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
		composer.sendChannelMessage(connectionHandler.getChannel(), text);
		appendToChat(user.getNickname(), text);
	}

	// TODO add field for user mode and handle errors
	// TODO fix switching server/channel errors
	public void showServerDialog(boolean inputRequired) {
		final int fieldWidth = 10;
		final int shortFieldWidth = 4;
		final int errorWidth = 18;
		// Includes default settings.
		JTextField serverField = new JTextField("irc.freenode.net", fieldWidth);
		JTextField portField = new JTextField("6667", shortFieldWidth);
		JTextField channelField = new JTextField("#freenode", fieldWidth);
		JPanel panel = new JPanel(new MigLayout());
		panel.add(new JLabel("Server configuration"), "cell 0 0, span, align center");
		panel.add(new JLabel("Hostname :"), "cell 0 1, align right");
		panel.add((serverField), "cell 1 1");
		panel.add(new JLabel("Port : "), "cell 0 2, align right");
		panel.add(portField, "cell 1 2");
		panel.add(new JLabel("Channel :"), "cell 0 3, align right");
		panel.add((channelField), "cell 1 3");

		JTextField userNameField = new JTextField("LiULouie", fieldWidth);
		JTextField realNameField = new JTextField("Louis from LiU", fieldWidth);
		JTextField nickNameField = new JTextField("LiULou", fieldWidth);
		panel.add(new JLabel("User configuration"), "cell 0 4, span, align center");
		panel.add(new JLabel("Username :"), "cell 0 5, align right");
		panel.add((userNameField), "cell 1 5");
		panel.add(new JLabel("Real Name :"), "cell 0 6, align right");
		panel.add(realNameField, "cell 1 6");
		panel.add(new JLabel("Nickname :"), "cell 0 7, align right");
		panel.add((nickNameField), "cell 1 7");

		// Only visible when there is an error to show.
		JTextArea errorComp = new JTextArea();
		errorComp.setForeground(Color.RED);
		errorComp.setColumns(errorWidth);
		errorComp.setLineWrap(true);
		errorComp.setWrapStyleWord(true);
		errorComp.setEditable(false);
		errorComp.setVisible(false);
		panel.add(errorComp, "cell 0 8, span");

		boolean isDone = false;
		String errorMessage = null;
		while (!isDone) {
			if (errorMessage != null) {
				errorComp.setText(errorMessage);
				errorComp.setVisible(true);
			}
			try {
				int result = JOptionPane.showConfirmDialog(null, panel, "Connect to a server", JOptionPane.OK_CANCEL_OPTION,
														   JOptionPane.PLAIN_MESSAGE);
				if (result == JOptionPane.OK_OPTION) {
					String hostname = serverField.getText();
					int port = Integer.parseInt(portField.getText());
					String channel = channelField.getText();
					String nickname = nickNameField.getText();
					String realName = realNameField.getText();
					String username = userNameField.getText();
					if (hostname.isEmpty() || channel.isEmpty() || nickname.isEmpty() ||
						realName.isEmpty() || username.isEmpty()) {
						errorMessage = "There are empty fields.";
						continue;
					}
					// If user forgets "#" before channel, prepend it.
					if (channel.charAt(0) != '#') {
						channel = "#" + channel;
					}
					user.setNames(nickname, realName, username);
					connectionHandler.setServer(hostname, port, user);
					connectionHandler.setChannel(channel);
					chatComponent.clearChat();
					isDone = true;
				} else if (inputRequired) {
					// If input is required to continue and user dismisses the window, exit the program.
					System.exit(0);
				} else {
					return;
				}
			} catch (IOException ex) {
				LOGGER.log(Level.WARNING, ExceptionUtils.getStackTrace(ex));
				errorMessage = "Could not connect to server. Please check hostname and port.";
			} catch (NumberFormatException ex) {
				LOGGER.log(Level.WARNING, ExceptionUtils.getStackTrace(ex));
				errorMessage = "Could not parse server port.";
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

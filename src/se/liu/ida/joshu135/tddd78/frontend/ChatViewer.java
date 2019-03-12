package se.liu.ida.joshu135.tddd78.frontend;

import net.miginfocom.swing.MigLayout;
import se.liu.ida.joshu135.tddd78.backend.ConnectionHandler;
import se.liu.ida.joshu135.tddd78.backend.MessageComposer;
import se.liu.ida.joshu135.tddd78.models.AppUser;

import javax.swing.*;
import java.awt.*;

/**
 * Contains and manages the main JFrame and any dialogs that are anchored to the frame. Interaction to and from components
 * inside the JFrame are handled by a ViewMediator.
 */
public class ChatViewer {
	private static final int DEFAULT_WIDTH = 1400;
	private static final int DEFAULT_HEIGHT = 700;
	private JFrame frame;
	private ViewMediator mediator;

	public ViewMediator getMediator() {
		return mediator;
	}

	public ChatViewer(ConnectionHandler connectionHandler, AppUser user, MessageComposer composer) {
		mediator = new ViewMediator(this, connectionHandler, composer, user);
		frame = new JFrame("IRC");
		frame.setLayout(new MigLayout("fill"));
		frame.setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		ChatComponent chatComponent = new ChatComponent();
		AuthorComponent authorComponent = new AuthorComponent(mediator);
		UserListComponent userListComponent = new UserListComponent(mediator);
		ServerTreeComponent serverTreeComponent = new ServerTreeComponent(mediator);
		mediator.addComponents(chatComponent, serverTreeComponent, userListComponent);
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
		channelConfig.addActionListener(e -> mediator.showChannelDialog());
		connectionMenu.add(serverConfig);
		connectionMenu.add(channelConfig);

		JMenu statusMenu = new JMenu("Status");
		menuBar.add(statusMenu);
		String defaultAway = "Set Away status";
		JMenuItem awayStatus = new JMenuItem(defaultAway);
		awayStatus.addActionListener(e -> {
			MessageComposer composer = mediator.getComposer();
			if (awayStatus.getText().equals(defaultAway)) {
				String input = JOptionPane.showInputDialog(frame, "Away message:");
				// Default message is "Away"
				String awayMessage = input.isEmpty() ? "Away" : input;
				awayStatus.setText("Remove Away status");
				composer.setAway(awayMessage);
			} else {
				awayStatus.setText(defaultAway);
				composer.removeAway();
			}
		});
		statusMenu.add(awayStatus);

		frame.setJMenuBar(menuBar);
	}

	public void showServerDialog(boolean inputRequired, String errorMessage) {
		ServerDialog.show(frame, mediator, inputRequired, errorMessage);
	}

	public void showServerDialog(boolean inputRequired) {
		ServerDialog.show(frame, mediator, inputRequired);
	}
}

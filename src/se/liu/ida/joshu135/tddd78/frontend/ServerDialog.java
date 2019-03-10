package se.liu.ida.joshu135.tddd78.frontend;

import net.miginfocom.swing.MigLayout;
import org.apache.commons.lang3.exception.ExceptionUtils;
import se.liu.ida.joshu135.tddd78.backend.ConnectionHandler;
import se.liu.ida.joshu135.tddd78.models.Server;
import se.liu.ida.joshu135.tddd78.models.User;
import se.liu.ida.joshu135.tddd78.util.LogUtil;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Component that contains fields for registering to a server. The static method "show" can be used to display it's dialog.
 */
public class ServerDialog extends JPanel {
	private static final Logger LOGGER = LogUtil.getLogger(ServerDialog.class.getSimpleName());
	private static final int FIELD_WIDTH = 10;
	private static final int SHORT_FIELD_WIDTH = 4;
	private static final int ERROR_WIDTH = 18;
	// TODO remove these
	private static String defaultHostname = "irc.mibbit.net";
	private static String defaultPort = "6667";
	private static String defaultUsername = "LiULouie";
	private static String defaultRealName = "Louis from LiU";
	private static String defaultNickname = "LiULou";

	private JTextField serverField;
	private JTextField portField;
	private JTextField userNameField;
	private JTextField realNameField;
	private JTextField nickNameField;
	private JTextArea errorComp;

	public String getServer() {
		return serverField.getText();
	}

	public String getPort() {
		return portField.getText();
	}

	public String getUserName() {
		return userNameField.getText();
	}

	public String getRealName() {
		return realNameField.getText();
	}

	public String getNickname() {
		return nickNameField.getText();
	}

	public void showError(String error) {
		errorComp.setText(error);
		errorComp.setVisible(true);
	}

	public ServerDialog() {
		setLayout(new MigLayout());
		serverField = new JTextField(defaultHostname, FIELD_WIDTH);
		portField = new JTextField(defaultPort, SHORT_FIELD_WIDTH);
		add(new JLabel("Server configuration"), "cell 0 0, span, align center");
		add(new JLabel("Hostname :"), "cell 0 1, align right");
		add((serverField), "cell 1 1");
		add(new JLabel("Port : "), "cell 0 2, align right");
		add(portField, "cell 1 2");

		userNameField = new JTextField(defaultUsername, FIELD_WIDTH);
		realNameField = new JTextField(defaultRealName, FIELD_WIDTH);
		nickNameField = new JTextField(defaultNickname, FIELD_WIDTH);
		add(new JLabel("User configuration"), "cell 0 3, span, align center");
		add(new JLabel("Username :"), "cell 0 4, align right");
		add((userNameField), "cell 1 4");
		add(new JLabel("Real Name :"), "cell 0 5, align right");
		add(realNameField, "cell 1 5");
		add(new JLabel("Nickname :"), "cell 0 6, align right");
		add((nickNameField), "cell 1 6");

		// Only visible when there is an error to show.
		errorComp = new JTextArea();
		errorComp.setForeground(Color.RED);
		errorComp.setColumns(ERROR_WIDTH);
		errorComp.setLineWrap(true);
		errorComp.setWrapStyleWord(true);
		errorComp.setEditable(false);
		errorComp.setVisible(false);
		add(errorComp, "cell 0 8, span");
	}

	public static void show(Component parentComponent, User user, ConnectionHandler connectionHandler,
								ServerTreeComponent serverTreeComponent, boolean inputRequired)
	{
		show(parentComponent, user, connectionHandler, serverTreeComponent, inputRequired, null);
	}
	public static void show(Component parentComponent, User user, ConnectionHandler connectionHandler,
							ServerTreeComponent serverTreeComponent, boolean inputRequired, String errorMessage) {
		ServerDialog serverDialog = new ServerDialog();
		boolean isDone = false;
		while (!isDone) {
			if (errorMessage != null) {
				serverDialog.showError(errorMessage);
			}
			try {
				int result = JOptionPane.showConfirmDialog(parentComponent, serverDialog, "Connect to a server", JOptionPane.OK_CANCEL_OPTION,
														   JOptionPane.PLAIN_MESSAGE);
				if (result == JOptionPane.OK_OPTION) {
					String hostname = serverDialog.getServer();
					int port = Integer.parseInt(serverDialog.getPort());
					String nickname = serverDialog.getNickname();
					String realName = serverDialog.getRealName();
					String username = serverDialog.getUserName();
					if (hostname.isEmpty() || nickname.isEmpty() ||
						realName.isEmpty() || username.isEmpty()) {
						errorMessage = "There are empty fields.";
						continue;
					}
					Server newServer = new Server(hostname, port);
					Server currentServer = connectionHandler.getServer();
					user.setNames(nickname, realName, username);
					if (currentServer != null) {
						serverTreeComponent.removeServerNode(connectionHandler.getServer());
					}
					connectionHandler.setServer(newServer, user);
					serverTreeComponent.addServerNode(newServer);
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
}

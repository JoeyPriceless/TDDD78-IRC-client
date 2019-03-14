package se.liu.ida.joshu135.tddd78.frontend;

import se.liu.ida.joshu135.tddd78.backend.ConnectionHandler;
import se.liu.ida.joshu135.tddd78.backend.MessageComposer;
import se.liu.ida.joshu135.tddd78.models.AbstractServerChild;
import se.liu.ida.joshu135.tddd78.models.AppUser;
import se.liu.ida.joshu135.tddd78.models.Channel;
import se.liu.ida.joshu135.tddd78.models.Message;
import se.liu.ida.joshu135.tddd78.models.Scope;
import se.liu.ida.joshu135.tddd78.models.Server;
import se.liu.ida.joshu135.tddd78.models.User;

import java.io.IOException;

/**
 * A mediator class that acts as the interface for component-component interaction as well as backend-frontend interaction.
 * This is done to limit the dependencies between the different parts of the program and streamline certain processes.
 */
public class ViewMediator {
	private ChatViewer chatViewer;
	private ConnectionHandler conHandler;
	private MessageComposer composer;
	private AppUser user;
	private ChatComponent chatComponent = null;
	private ServerTreeComponent serverTreeComponent = null;
	private UserListComponent userListComponent = null;
	private ChannelDialog channelDialog = null;

	public MessageComposer getComposer() {
		return composer;
	}

	public Server getServer() {
		return conHandler.getServer();
	}

	public void setServer(Server newServer, String nickname, String realName, String username) throws IOException {
		Server currentServer = conHandler.getServer();
		user.setNames(nickname, realName, username);
		if (currentServer != null) {
			serverTreeComponent.removeServerNode(currentServer);
		}
		conHandler.setServer(newServer, user);
		chatComponent.setSource(null);
		serverTreeComponent.addServerNode(newServer);
		chatViewer.resetAwayStatus();
	}

	public ViewMediator(final ChatViewer chatViewer, final ConnectionHandler conHandler, final MessageComposer composer,
						final AppUser user) {
		this.chatViewer = chatViewer;
		this.conHandler = conHandler;
		this.composer = composer;
		this.user = user;
	}

	public void addComponents(final ChatComponent chatComponent, final ServerTreeComponent serverTreeComponent,
							  final UserListComponent userListComponent)
	{
		this.chatComponent = chatComponent;
		this.serverTreeComponent = serverTreeComponent;
		this.userListComponent = userListComponent;
	}

	public void setServerTreeNode(AbstractServerChild selectedChild) {
		Server server = conHandler.getServer();
		// Updates the tree data model and shows the new channel.
		server.setActiveChild(selectedChild);
		serverTreeComponent.updateStructure(server.getNode(), selectedChild.getNode());
	}

	public void setViewSource(AbstractServerChild source) {
		chatComponent.setSource(source);
		conHandler.getServer().setActiveChild(source);
		userListComponent.refreshIfNeeded();
	}

	public void changeChannel(Channel newChannel) {
		conHandler.getServer().replaceChannel(newChannel);
		composer.joinChannel(newChannel.getName());
		setServerTreeNode(newChannel);
	}

	public void submitMessage(String text) {
		String target = conHandler.getServer().getActiveChild().getName();
		String userName = user.getNickname();
		composer.sendPrivMsg(target, text);
		if (!target.startsWith("#")) {
			appendToUser(userName, target, text);
		} else {
			appendToChannel(userName, text);
		}
	}

	public void appendMessage(String sender, String text) {
		if (sender == null) {
			appendToActive(text);
		} else if (!sender.startsWith("#")) {
			appendToUser(sender, sender, text);
		} else {
			appendToChannel(sender, text);
		}
	}

	public void appendToActive(String text) {
		AbstractServerChild active = conHandler.getServer().getActiveChild();
		String message = Message.formatMessage(null, text);
		if (active == null) {
			chatComponent.appendText(message);
		} else {
			active.appendText(message);
		}
		chatComponent.update();
	}

	public void appendToChannel(String sender, String text) {
		Channel activeChannel = conHandler.getServer().getChannel();
		String message = Message.formatMessage(sender, text);
		if (activeChannel == null) {
			chatComponent.appendText(message);
		} else {
			activeChannel.appendText(message);
		}
		chatComponent.update();
	}

	private void appendToUser(String sender, String target, String text) {
		Server server = conHandler.getServer();
		User user = server.getUser(target, false);
		user.appendText(Message.formatMessage(sender, text));
		// Update tree display to display possible new conversations.
		serverTreeComponent.updateStructure(server.getNode(), user.getNode());
		chatComponent.update();
	}

	public void addUserToList(String nickname) {
		userListComponent.addUser(nickname);
	}

	public void addUsersToList(Iterable<String> nicknames) {
			userListComponent.addUsers(nicknames);
		}

	public void removeUserFromList(String nickname) {
		userListComponent.removeUser(nickname);
	}

	public void clearUserList() {
		userListComponent.clear();
	}

	public void requestNames(Scope scope) {
		String messageString;
		// If user selects all names on server, it is always granted. If the user is an a channel and selects channel names,
		// request all names in channel. If the user is in a private conversation, simply set the UserList to the name of the
		// recipient.
		if (scope == Scope.SERVERCHILD) {
			Server server = conHandler.getServer();
			AbstractServerChild child = server.getActiveChild();
			if (child == null) {
				return;
			}
			else if (child.equals(server.getChannel())) {
				messageString = MessageComposer.compose("NAMES", child.getName());
			} else {
				userListComponent.clear();
				userListComponent.addUser(child.getName());
				return;
			}
		} else {
			messageString = MessageComposer.compose("NAMES");
		}
		composer.queueMessage(new Message(messageString));
	}

	public void showChannelDialog() {
		channelDialog = new ChannelDialog(this, chatViewer.getFrame());
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
		userListComponent.forceRefresh();
	}

	public void endOfName() {
		userListComponent.endOfName();
	}

	public void showServerDialog(boolean inputRequired, String errorMessage) {
		chatViewer.showServerDialog(inputRequired, errorMessage, user, getServer());
	}
}

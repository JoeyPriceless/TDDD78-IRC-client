package se.liu.ida.joshu135.tddd78.frontend;

import net.miginfocom.swing.MigLayout;
import se.liu.ida.joshu135.tddd78.models.Scope;
import se.liu.ida.joshu135.tddd78.models.Server;
import se.liu.ida.joshu135.tddd78.models.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Swing component that contains the list of users in either the selected conversation or the entire server. To fill the list,
 * the client has to request the names from the server. If there are too many names to fit in one response, they are divided
 * into several responses.
 */
public class UserListComponent extends JPanel {
	private static final Scope[] SCOPE_OPTIONS = new Scope[] { Scope.SERVERCHILD, Scope.SERVER };
	private static final Dimension SIZE = new Dimension(170, 0);
	private JComboBox<Scope> scopeBox;
	private Scope selectedScope;
	private JList<String> userList;
	private DefaultListModel<String> userListModel;
	private ViewMediator mediator;

	public UserListComponent(ViewMediator mediator) {
		this.mediator = mediator;
		this.setLayout(new MigLayout());
		scopeBox = new JComboBox<>(SCOPE_OPTIONS);
		selectedScope = Scope.SERVERCHILD;
		scopeBox.setSelectedItem(selectedScope);
		scopeBox.addActionListener(e -> forceRefresh());
		userListModel = new DefaultListModel<>();
		userList = new JList<>();
		userList.addMouseListener(new DoubleClickListener());
		JScrollPane scrollPane = new JScrollPane(userList);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		add(scopeBox, "dock north");
		add(scrollPane, "push, grow");
		setPreferredSize(SIZE);
	}

	public void addUsers(Iterable<String> names) {
		for (String name : names) {
			addUser(name);
		}
	}

	public void addUser(String name) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override public void run() {
				if (!userListModel.contains(name)) {
					userListModel.addElement(name);
				}
			}
		});
	}

	public void removeUser(String name) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override public void run() {
				if (!userListModel.contains(name)) {
					userListModel.removeElement(name);
				}
			}
		});
	}

	public void clear() {
		userListModel.clear();
		// Temporarily disable scopeBox to avoid unwanted calls to the ActionListener
		scopeBox.setEnabled(false);
		selectedScope = Scope.SERVERCHILD;
		scopeBox.setSelectedItem(selectedScope);
		scopeBox.setEnabled(true);
	}


	public void endOfName() {
		userList.setModel(userListModel);
	}

	/**
	 * Checks that the UserList scope isn't set to "Server" before resetting as it isn't neccesary then.
	 */
	public void refreshIfNeeded() {
		Scope newScope = (Scope)scopeBox.getSelectedItem();
		if (selectedScope == Scope.SERVER && newScope == selectedScope) return;
		forceRefresh();
	}

	public void forceRefresh() {
		Scope newScope = (Scope)scopeBox.getSelectedItem();
		userListModel.clear();
		// Do not show Server as a user in the list.
		if (mediator.getViewSource().equals(mediator.getServer())) return;
		if (newScope == null) return;
		selectedScope = newScope;

		mediator.requestNames(newScope);
	}

	private class DoubleClickListener extends MouseAdapter {
		public void mouseClicked(MouseEvent event) {
			if (event.getClickCount() == 2) {
				int index = userList.locationToIndex(event.getPoint());
				String name = userList.getModel().getElementAt(index);
				Server server = mediator.getServer();
				User user = server.getUser(name, true);
				mediator.setServerTreeNode(user);
			}
		}
	}
}

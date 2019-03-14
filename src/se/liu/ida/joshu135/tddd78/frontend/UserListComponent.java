package se.liu.ida.joshu135.tddd78.frontend;

import net.miginfocom.swing.MigLayout;
import se.liu.ida.joshu135.tddd78.models.Scope;
import se.liu.ida.joshu135.tddd78.models.Server;
import se.liu.ida.joshu135.tddd78.models.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class UserListComponent extends JPanel {
	private static final Scope[] SCOPE_OPTIONS = new Scope[] { Scope.CHANNEL, Scope.SERVER };
	private static final Dimension SIZE = new Dimension(170, 0);
	private JComboBox<Scope> scopeBox;
	private Scope selectedScope;
	private JList<String> userList;
	private DefaultListModel<String> userListModel;
	private ViewMediator mediator;

	public void setScope(final Scope selectedScope) {
		this.selectedScope = selectedScope;
		scopeBox.setSelectedItem(selectedScope);
	}

	public UserListComponent(ViewMediator mediator) {
		this.mediator = mediator;
		this.setLayout(new MigLayout());
		scopeBox = new JComboBox<>(SCOPE_OPTIONS);
		selectedScope = Scope.CHANNEL;
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
		if (!userListModel.contains(name)) {
			userListModel.addElement(name);
		}
	}

	public void removeUser(String name) {
		if (userListModel.contains(name)) {
			userListModel.removeElement(name);
		}
	}

	public void clear() {
		userListModel.clear();
		// Temporarily disable scopeBox to avoid unwanted calls to the ActionListener
		scopeBox.setEnabled(false);
		selectedScope = Scope.CHANNEL;
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
		selectedScope = newScope;
		userListModel.clear();
		if (newScope == null) return;

		mediator.requestNames(newScope);
	}

	private class DoubleClickListener extends MouseAdapter {
		public void mouseClicked(MouseEvent event) {
				JList list = (JList)event.getSource();
				if (event.getClickCount() == 2) {
					int index = list.locationToIndex(event.getPoint());
					String name = (String)list.getModel().getElementAt(index);
					Server server = mediator.getServer();
					User user = server.getUser(name, true);
					mediator.setViewSource(user);
				}
			}
		}
}

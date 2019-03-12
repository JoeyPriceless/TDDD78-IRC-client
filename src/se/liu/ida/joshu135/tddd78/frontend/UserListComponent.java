package se.liu.ida.joshu135.tddd78.frontend;

import net.miginfocom.swing.MigLayout;
import se.liu.ida.joshu135.tddd78.models.Server;
import se.liu.ida.joshu135.tddd78.models.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class UserListComponent extends JPanel {
	private static final String[] SCOPE_OPTIONS = new String[] { "Channel", "Server" };
	private static final Dimension SIZE = new Dimension(170, 0);
	private JComboBox<String> scopeBox;
	private JList<String> userList;
	private DefaultListModel<String> userListModel;
	private ViewMediator mediator;

	public UserListComponent(ViewMediator mediator) {
		this.mediator = mediator;
		this.setLayout(new MigLayout());
		scopeBox = new JComboBox<>(SCOPE_OPTIONS);
		scopeBox.addActionListener(e -> refresh());
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
		scopeBox.setSelectedIndex(0);
		scopeBox.setEnabled(true);
	}


	public void endOfName() {
		userList.setModel(userListModel);
	}

	public void refresh() {
		String newScope = (String)scopeBox.getSelectedItem();
		if (newScope == null) return;

		userListModel.clear();
		if (newScope.equals(SCOPE_OPTIONS[0])) { // Channel/User
			mediator.requestNames(true);
		} else if (newScope.equals(SCOPE_OPTIONS[1])) { // Server
			mediator.requestNames(false);
		}
	}

	private class DoubleClickListener extends MouseAdapter {
		public void mouseClicked(MouseEvent event) {
				JList list = (JList)event.getSource();
				if (event.getClickCount() == 2) {
					int index = list.locationToIndex(event.getPoint());
					String name = (String)list.getModel().getElementAt(index);
					Server server = mediator.getServer();
					User user = server.getUser(name, true);
					mediator.changeViewSource(user);
				}
			}
		}
}

package se.liu.ida.joshu135.tddd78.frontend;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class UserListComponent extends JPanel {
	private static final String[] SCOPE_OPTIONS = new String[] { "Channel", "Server" };
	private static final Dimension SIZE = new Dimension(170, 0);
	private JComboBox<String> scopeBox;
	private String selectedScope;
	private JList<String> userList;
	private DefaultListModel<String> userListModel;
	private ChatViewer chatViewer;

	public UserListComponent(ChatViewer chatViewer) {
		this.chatViewer = chatViewer;
		this.setLayout(new MigLayout());
		scopeBox = new JComboBox<>(SCOPE_OPTIONS);
		scopeBox.addActionListener(new ScopeBoxListener());
		userListModel = new DefaultListModel<>();
		userList = new JList<>();
		JScrollPane scrollPane = new JScrollPane(userList);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		add(scopeBox, "dock north");
		add(scrollPane, "push, grow");
		setPreferredSize(SIZE);
	}

	public void addUsers(ArrayList<String> names) {
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
		selectedScope = (String)scopeBox.getSelectedItem();
	}


	public void endOfName() {
		userList.setModel(userListModel);
		scopeBox.setEnabled(true);
	}

	private class ScopeBoxListener implements ActionListener {
		@Override public void actionPerformed(final ActionEvent e) {
			String newScope = (String)scopeBox.getSelectedItem();
			if (newScope == null || newScope.equals(selectedScope)) return;

			userListModel.clear();
			if (newScope.equals(SCOPE_OPTIONS[0])) { // Channel
				chatViewer.requestNames(true);
			} else if (newScope.equals(SCOPE_OPTIONS[1])) { // Server
				chatViewer.requestNames(false);
			}
			selectedScope = newScope;
		}
	}
}

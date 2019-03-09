package se.liu.ida.joshu135.tddd78.frontend;

import net.miginfocom.swing.MigLayout;
import se.liu.ida.joshu135.tddd78.models.Channel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class UserListComponent extends JPanel {
	private static final String[] SCOPE_OPTIONS = new String[] { "Channel", "Server" };
	private static final Dimension SIZE = new Dimension(170, 0);
	private JComboBox<String> scopeBox;
	private JList<String> userList;
	private DefaultListModel<String> userListModel;


	public UserListComponent() {
		this.setLayout(new MigLayout());
		scopeBox = new JComboBox<>(SCOPE_OPTIONS);
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
	}


	public void endOfName() {
		userList.setModel(userListModel);
	}
}

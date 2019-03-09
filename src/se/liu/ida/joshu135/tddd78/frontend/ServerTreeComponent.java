package se.liu.ida.joshu135.tddd78.frontend;

import se.liu.ida.joshu135.tddd78.models.Server;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;

/**
 * Scrollable JTree that displays the user's selected server and channel.
 */
public class ServerTreeComponent extends JScrollPane {
	private static final Dimension MIN_SIZE = new Dimension(170, 0);
	private JTree serverTree;
	private DefaultMutableTreeNode root;
	private DefaultTreeModel model;

	public ServerTreeComponent() {
		root = new DefaultMutableTreeNode("Servers");
		model = new DefaultTreeModel(root);
		serverTree = new JTree(model);
		setViewportView(serverTree);
		setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_AS_NEEDED);
		setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_AS_NEEDED);
		//serverTree.setRootVisible(false);
		setMinimumSize(MIN_SIZE);
	}

	public void addServerNode(Server server) {
		root.add(server.getNode());
		// For some reason, this can't happen at root initialization but doesn't need to happen on every channel.
		expandTree();
	}

	public void expandTree() {
		for (int i = 0; i < serverTree.getRowCount(); i++) {
			serverTree.expandRow(i);
		}
	}
}

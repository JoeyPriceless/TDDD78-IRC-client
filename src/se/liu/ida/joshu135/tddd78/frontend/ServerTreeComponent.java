package se.liu.ida.joshu135.tddd78.frontend;

import se.liu.ida.joshu135.tddd78.models.Server;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import java.awt.*;

public class ServerTreeComponent extends JScrollPane {
	private JTree serverTree;
	private DefaultMutableTreeNode root;
	private static final int DEPTH = 2;

	public ServerTreeComponent() {
		root = new DefaultMutableTreeNode("Servers");
		TreeModel model = new DefaultTreeModel(root);
		serverTree = new JTree(model);
		setViewportView(serverTree);
		setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_AS_NEEDED);
		setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_AS_NEEDED);
		//serverTree.setRootVisible(false);
	}

	public void addServerNode(Server server) {
		root.add(server.getNode());
		// For some reason, this can't happen at root initialization but doesn't need to happen on every channel.
		expandTree();
	}

	private void expandTree() {
		for (int i = 0; i < DEPTH; i++) {
			serverTree.expandRow(i);
		}
	}
}

package se.liu.ida.joshu135.tddd78.frontend;

import se.liu.ida.joshu135.tddd78.models.AbstractServerChild;
import se.liu.ida.joshu135.tddd78.models.Server;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;

/**
 * Scrollable JTree that displays the user's selected server and channel.
 */
public class ServerTreeComponent extends JScrollPane {
	private static final Dimension SIZE = new Dimension(170, 0);
	private JTree serverTree;
	private DefaultMutableTreeNode root;
	private DefaultTreeModel model;

	public ServerTreeComponent(TreeSelectionListener listener) {
		root = new DefaultMutableTreeNode("Servers");
		model = new DefaultTreeModel(root);
		serverTree = new JTree(model);
		setViewportView(serverTree);
		setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_AS_NEEDED);
		setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_AS_NEEDED);
		serverTree.setRootVisible(false);
		setPreferredSize(SIZE);
		serverTree.addTreeSelectionListener(listener);
	}

	public void addServerNode(Server server) {
		root.add(server.getNode());
		updateStructure(root);
		expandTree();
	}

	public void removeServerNode(Server server) {
		server.destroyNode();
		updateStructure(root);
	}

	public void expandTree() {
		for (int i = 0; i < serverTree.getRowCount(); i++) {
			serverTree.expandRow(i);
		}
	}

	public void updateStructure(DefaultMutableTreeNode atNode) {
		model.nodeStructureChanged(atNode);
	}

	public void updateStructure(DefaultMutableTreeNode atNode, DefaultMutableTreeNode selectedNode) {
		updateStructure(atNode);
		serverTree.setSelectionPath(new TreePath(selectedNode.getPath()));
	}
}

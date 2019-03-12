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

	public ServerTreeComponent(UserListComponent userListComponent, ChatComponent chatComponent) {
		root = new DefaultMutableTreeNode("Servers");
		model = new DefaultTreeModel(root);
		serverTree = new JTree(model);
		setViewportView(serverTree);
		setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_AS_NEEDED);
		setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_AS_NEEDED);
		serverTree.setRootVisible(false);
		setPreferredSize(SIZE);
		serverTree.addTreeSelectionListener(new SelectionListener(userListComponent, chatComponent));
	}

	public void addServerNode(Server server) {
		root.add(server.getNode());
		updateStructure(root);
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
		expandTree();
	}

	public void updateStructure(DefaultMutableTreeNode atNode, DefaultMutableTreeNode selectedNode) {
		serverTree.setSelectionPath(new TreePath(selectedNode.getPath()));
		updateStructure(atNode);
	}

	private class SelectionListener implements TreeSelectionListener {
		private UserListComponent userListComponent;
		private ChatComponent chatComponent;

		public SelectionListener(final UserListComponent userListComponent, final ChatComponent chatComponent) {
			this.userListComponent = userListComponent;
			this.chatComponent = chatComponent;
		}

		@Override public void valueChanged(final TreeSelectionEvent e) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode)e.getPath().getLastPathComponent();
			DefaultMutableTreeNode parent = (DefaultMutableTreeNode)node.getParent();
			Object userObject = node.getUserObject();
			// If selection is not a channel or user, don't do anything.
			// Inspection: need to be able to differentiate between channels/users and server nodes since nothing is meant to
			// 			   happen when you select a server node in the tree.
			if (!(userObject instanceof AbstractServerChild)) {
				return;
			} else if (parent == null) {
				userListComponent.clear();
				return;
			}
			AbstractServerChild selectedChild = (AbstractServerChild)userObject;
			chatComponent.setSource(selectedChild);
			((Server)parent.getUserObject()).setActiveChild(selectedChild);
			userListComponent.refresh();
		}
	}
}

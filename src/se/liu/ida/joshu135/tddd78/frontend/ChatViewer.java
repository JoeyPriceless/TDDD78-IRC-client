package se.liu.ida.joshu135.tddd78.frontend;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class ChatViewer {
	private JFrame frame;
	private ChatComponent chatComponent;

	public ChatViewer() {
		frame = new JFrame("IRC");
		frame.setLayout(new MigLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		chatComponent = new ChatComponent();
		frame.getContentPane().add(chatComponent);
		frame.pack();
		frame.setVisible(true);
	}

	public void appendToChat(String text) {
		chatComponent.appendText(text);
	}
}

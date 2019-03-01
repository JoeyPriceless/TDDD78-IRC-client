package se.liu.ida.joshu135.tddd78.frontend;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class ChatViewer {
	private JFrame frame;

	public ChatViewer() {
		frame = new JFrame("IRC");
		frame.setLayout(new MigLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ChatComponent chatComp = new ChatComponent();
		//frame.add(new JTextArea("lines\nlines", 10, 10));
		frame.getContentPane().add(chatComp);
		frame.pack();
		frame.setVisible(true);
	}
}

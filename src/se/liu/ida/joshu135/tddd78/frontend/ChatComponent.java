package se.liu.ida.joshu135.tddd78.frontend;

import se.liu.ida.joshu135.tddd78.models.Message;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ChatComponent extends JScrollPane {
	private JTextArea messageArea = new JTextArea("lines\nlines\nlines", 10, 25);

	public ChatComponent() {
		messageArea.setEditable(false);
		setViewportView(messageArea);
		setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
		setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_AS_NEEDED);
	}

	@Override protected void paintComponent(final Graphics g) {
		super.paintComponent(g);
//		messageArea.paint(g);
	}
}

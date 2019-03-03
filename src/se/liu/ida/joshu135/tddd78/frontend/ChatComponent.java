package se.liu.ida.joshu135.tddd78.frontend;

import javax.swing.*;
import java.awt.*;

/**
 * Swing component that contains the actual chat. Consists of a JTextArea within a JScrollPane as well as methods to manipulate
 * what's displayed in that chat.
 */
public class ChatComponent extends JScrollPane {
	private JTextArea messageArea;

	public ChatComponent() {
		messageArea = new JTextArea(40, 150);
		messageArea.setEditable(false);
		setViewportView(messageArea);
		setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
		setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_AS_NEEDED);
		messageArea.setLineWrap(true);
		messageArea.setWrapStyleWord(true);
	}

	@Override protected void paintComponent(final Graphics g) {
		super.paintComponent(g);
	}

	/**
	 * Appends a string to the chat window.
	 * @param text The string to append.
	 */
	public void appendText(String text) {
		// Ensure that there's a new line at the end.
		messageArea.append(text.stripTrailing().concat("\n"));
		// TODO block this if user scrolls up willingly.
		// Set caret to end of text so that the TextArea scrolls down with the text.
		//messageArea.setCaretPosition(messageArea.getText().length() - 1);
	}

	public void clearChat() {
		messageArea.setText("");
	}
}

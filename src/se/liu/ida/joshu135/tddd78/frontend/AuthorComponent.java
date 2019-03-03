package se.liu.ida.joshu135.tddd78.frontend;

import javax.swing.*;

public class AuthorComponent extends JScrollPane {
	private JTextArea authorField;

	public AuthorComponent() {
		authorField = new JTextArea(2, 150);
		authorField.setEditable(true);
		setViewportView(authorField);
		setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
		setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_AS_NEEDED);
		authorField.setLineWrap(true);
		authorField.setWrapStyleWord(true);
	}
}

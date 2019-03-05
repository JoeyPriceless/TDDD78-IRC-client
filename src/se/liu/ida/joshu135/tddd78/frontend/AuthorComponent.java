package se.liu.ida.joshu135.tddd78.frontend;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Component which contains the field for authoring messages. Submits messages to the ChatViewer.
 */
public class AuthorComponent extends JScrollPane {
	private JTextArea authorField;
	private ChatViewer chatViewer;
	private static final int HEIGHT = 1;

	// TODO block text submit before connection to server/channel.
	public AuthorComponent(ChatViewer chatViewer) {
		this.chatViewer = chatViewer;
		authorField = new JTextArea();
		authorField.setRows(HEIGHT);
		authorField.setEditable(true);
		setViewportView(authorField);
		setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
		setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_AS_NEEDED);
		authorField.setLineWrap(true);
		authorField.setWrapStyleWord(true);
		setInputMap();
	}

	private void setInputMap() {
		final String textSubmit = "text-submit";
		// Change the JTextArea's input map.
		// SHIFT + ENTER is given ENTER's default action (line break)
		// Enter is put to text-submit
		InputMap inputMap = authorField.getInputMap();
		KeyStroke enter = KeyStroke.getKeyStroke("ENTER");
		//KeyStroke shiftEnter = KeyStroke.getKeyStroke("shift ENTER");
		inputMap.put(enter, textSubmit);

		ActionMap actionMap = authorField.getActionMap();
		actionMap.put(textSubmit, new AbstractAction() {
			@Override public void actionPerformed(final ActionEvent e) {
				submitMessage();
			}
		});
	}

	public void submitMessage() {
		String noNewlines = authorField.getText().replace("\n", "").replace("\r", "");
		chatViewer.submitMessage(noNewlines);
		authorField.setText("");
	}
}
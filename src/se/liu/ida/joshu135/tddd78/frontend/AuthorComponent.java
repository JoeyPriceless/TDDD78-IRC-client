package se.liu.ida.joshu135.tddd78.frontend;

import se.liu.ida.joshu135.tddd78.backend.MessageComposer;
import se.liu.ida.joshu135.tddd78.models.Message;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class AuthorComponent extends JScrollPane {
	private JTextArea authorField;
	private ChatViewer chatViewer;

	// TODO block text submit before connection to server/channel.
	public AuthorComponent(ChatViewer chatViewer) {
		this.chatViewer = chatViewer;
		authorField = new JTextArea(1, 150);
		authorField.setEditable(true);
		setViewportView(authorField);
		setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
		setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_AS_NEEDED);
		authorField.setLineWrap(true);
		authorField.setWrapStyleWord(true);
		setInputMap();
	}

	private void setInputMap() {
		final String TEXT_SUBMIT = "text-submit";
		// Change the JTextArea's input map.
		// SHIFT + ENTER is given ENTER's default action (line break)
		// Enter is put to text-submit
		InputMap inputMap = authorField.getInputMap();
		KeyStroke enter = KeyStroke.getKeyStroke("ENTER");
		//KeyStroke shiftEnter = KeyStroke.getKeyStroke("shift ENTER");
		inputMap.put(enter, TEXT_SUBMIT);

		ActionMap actionMap = authorField.getActionMap();
		actionMap.put(TEXT_SUBMIT, new AbstractAction() {
			@Override public void actionPerformed(final ActionEvent e) {
				String noNewlines = authorField.getText().replace("\n", "").replace("\r", "");
				chatViewer.submitMessage(noNewlines);
				authorField.setText("");
			}
		});
	}
}
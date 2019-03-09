package se.liu.ida.joshu135.tddd78.frontend;

import net.miginfocom.swing.MigLayout;
import se.liu.ida.joshu135.tddd78.backend.MessageComposer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Component which contains the field for authoring messages. Submits messages to the ChatViewer.
 */
public class AuthorComponent extends JPanel {
	private JTextArea authorField;
	private ChatViewer chatViewer;
	private static final int HEIGHT = 1;

	// TODO block text submit before connection to server/channel.
	public AuthorComponent(ChatViewer chatViewer) {
		this.chatViewer = chatViewer;
		this.setLayout(new MigLayout());
		authorField = new JTextArea();
		authorField.setRows(HEIGHT);
		authorField.setEditable(true);
		authorField.setLineWrap(true);
		authorField.setWrapStyleWord(true);
		JScrollPane scrollPane = new JScrollPane(authorField);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		JButton sendButton = new JButton("Send");
		sendButton.addActionListener(e -> {
			if (isValidMessage()) submitMessage();
		});
		add(scrollPane, "push, grow");
		add(sendButton);

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

		// Don't allow more characters if message is too long.
		// borrowedcode: https://coderanch.com/t/330598/java/limit-text
		authorField.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent evt) {
				if (isTooLong()) evt.consume();
			}
		});
	}

	public void submitMessage() {
		String noNewlines = authorField.getText().replace("\n", "").replace("\r", "");
		chatViewer.submitMessage(noNewlines);
		authorField.setText("");
	}

	public boolean isTooLong() {
		// Calculate the max length of an authored message, including the command, IRC's length limit of 510 and a channel name
		// of up to 50 characters. Don't allow more characters if user's message exceeds this length
		final int channelMaxLength = 50;
		int allowance = MessageComposer.lengthAllowance(MessageComposer.MAX_LENGTH - channelMaxLength, "PRIVMSG", ":");
		int length = authorField.getText().length();
		return length > allowance;
	}

	public boolean isValidMessage() {
		return (!isTooLong() && authorField.getText().length() > 0);
	}
}
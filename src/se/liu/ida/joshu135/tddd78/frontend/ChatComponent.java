package se.liu.ida.joshu135.tddd78.frontend;

import se.liu.ida.joshu135.tddd78.models.AbstractChildNode;

import javax.swing.*;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

/**
 * Swing component that contains the actual chat. Consists of a JTextArea within a JScrollPane as well as methods to manipulate
 * what's displayed in that chat.
 */
public class ChatComponent extends JScrollPane {
	private AbstractChildNode source = null;
	private JTextArea messageArea;
	private boolean doAutoScroll = true;

	public ChatComponent() {
		messageArea = new JTextArea();
		messageArea.setEditable(false);
		setViewportView(messageArea);
		scrollSetupborrowedcode();
		messageArea.setLineWrap(true);
		messageArea.setWrapStyleWord(true);
	}

	public void setSource(final AbstractChildNode source) {
		clearChat();
		this.source = source;
		update();
	}

	/**
	 * Appends a string to the chat window.
	 * @param text The string to append.
	 */
	public void appendText(String text) {
		// Ensure that there's a new line at the end.
		messageArea.append(text.stripTrailing() + "\n");
	}

	public void clearChat() {
		messageArea.setText("");
	}

	public void update() {
		if (source != null) {
			messageArea.setText(source.getHistory());
		}
	}

	/**
	 * Configures the scroll bar to only scroll automatically when it's at the bottom.
	 * Makes sure that user isn't brought to the bottom when viewing old messages.
	 * Source: https://stackoverflow.com/a/39410581
	 */
	private void scrollSetupborrowedcode() {
		setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
		setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_AS_NEEDED);

		getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
			private BoundedRangeModel brm = getVerticalScrollBar().getModel();
		        @Override
		        public void adjustmentValueChanged(AdjustmentEvent e) {
		            // Invoked when user select and move the cursor of scroll by mouse explicitly.
		            if (!brm.getValueIsAdjusting()) {
		                if (doAutoScroll) brm.setValue(brm. getMaximum());
		            } else {
		                // doAutoScroll will be set to true when user reaches at the bottom of document.
		                doAutoScroll = ((brm.getValue() + brm.getExtent()) == brm.getMaximum());
		            }
		        }
		    });

		    addMouseWheelListener(new MouseWheelListener() {
				private BoundedRangeModel brm = getVerticalScrollBar().getModel();
		        @Override
		        public void mouseWheelMoved(MouseWheelEvent e) {
		            // Invoked when user use mouse wheel to scroll
		            if (e.getWheelRotation() < 0) {
		                // If user trying to scroll up, doAutoScroll should be false.
		                doAutoScroll = false;
		            } else {
		                // doAutoScroll will be set to true when user reaches at the bottom of document.
		                doAutoScroll = ((brm.getValue() + brm.getExtent()) == brm.getMaximum());
		            }
		        }
		    });
	}
}

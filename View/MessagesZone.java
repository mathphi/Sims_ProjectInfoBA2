package View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;

public class MessagesZone extends JPanel {
	private static final long serialVersionUID = 4955776412313787929L;
	
	private JPanel mainPanel;
	private JScrollPane scrollView;

	public MessagesZone() {
		setLayout(new BorderLayout());
		
		JLabel zoneTitle = new JLabel("Historique des messages");
		zoneTitle.setBorder(new EmptyBorder(5, 5, 0, 5));
		
		mainPanel = new JPanel();
		mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

		scrollView = new JScrollPane(mainPanel);
		scrollView.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollView.setWheelScrollingEnabled(true);
		scrollView.setBorder(null);

		add(zoneTitle, BorderLayout.NORTH);
		add(scrollView, BorderLayout.CENTER);
	}
	
	public void setMessagesList(ArrayList<String> msgList) {
		mainPanel.removeAll();
		
		for (String msg : msgList) {
			addMessage(msg);
		}

		refreshList();
	}
	
	public void appendMessage(String msg) {
		addMessage(msg);
		refreshList();
	}
	
	private void addMessage(String msg) {
		JLabel l = new JLabel(msg);
		l.setOpaque(true);
		l.setBackground(Color.CYAN);
		l.setBorder(new EmptyBorder(10, 10, 10, 10));
		l.setMinimumSize(new Dimension(10, 25));

		mainPanel.add(l);
		mainPanel.add(new Box.Filler(
				new Dimension(0, 10), 
                new Dimension(0, 10), 
                new Dimension(0, 10)));
	}

	private void refreshList() {
		// Refresh the list and ensure that the scroll bar is
		// up to date to be scrolled at bottom
		scrollView.repaint();
		scrollView.validate();
		scrollView.revalidate();

		// Scroll to bottom
		JScrollBar scrollbar = scrollView.getVerticalScrollBar();
		scrollbar.setValue(scrollbar.getMaximum());
	}
}
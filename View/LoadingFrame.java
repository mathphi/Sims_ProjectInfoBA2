package View;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

public class LoadingFrame extends JFrame {
	private static final long serialVersionUID = -1049415178483011593L;

	public LoadingFrame() {
		super("Chargement...");

		setPreferredSize(new Dimension(350, 200));
		setLayout(new BorderLayout());
		setUndecorated(true);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		JLabel loadingLabel = new JLabel("Chargement...", JLabel.CENTER);
		loadingLabel.setFont(loadingLabel.getFont().deriveFont((float) 28.0));
		
		add(loadingLabel, BorderLayout.CENTER);
	}
	
	public void showFrame() {
		pack();
		setLocationRelativeTo(getOwner());
		setVisible(true);
		repaint();
	}
	
	public void hideFrame() {
		setVisible(false);
		pack();
		repaint();
	}
}

package View;

import Model.Editor;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class EditorMenu extends JDialog {
	private static final long serialVersionUID = 4870573801345257186L;
	
	public EditorMenu(Frame parent, Editor editor) {
		super(parent, "Menu principal", true);
		
		setPreferredSize(new Dimension(300, 300));
		
		JPanel mainPanel = new JPanel();
		
		mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		mainPanel.setLayout(new GridLayout(0, 1, 0, 10));
		
		JLabel titleLabel = new JLabel("Menu éditeur", JLabel.CENTER);
		JButton continueButton = new JButton("Continuer");
		JButton saveButton = new JButton("Enregistrer la carte");
		JButton loadButton = new JButton("Charger une carte");
		JButton exitButton = new JButton("Quitter");

		mainPanel.add(titleLabel);
		mainPanel.add(continueButton);
		mainPanel.add(saveButton);
		mainPanel.add(loadButton);
		mainPanel.add(exitButton);
		
		add(mainPanel);
		
		titleLabel.setFont(titleLabel.getFont().deriveFont((float)28));
		
		continueButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeMenu();
			}
		});
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editor.saveMap();
			}
		});
		loadButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editor.loadMap();
			}
		});
		exitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editor.quit();
			}
		});
	}
	
	public void showMenu() {
		pack();
		setLocationRelativeTo(getOwner());
		setVisible(true);
	}
	
	public void closeMenu() {
		setVisible(false);
	}
}

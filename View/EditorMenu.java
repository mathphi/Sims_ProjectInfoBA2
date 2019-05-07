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

	private JButton continueButton;
	private JButton startGameButton;
	private JButton resetButton;
	private JButton saveButton;
	private JButton loadButton;
	private JButton exitButton;
	
	public EditorMenu(Frame parent, Editor editor) {
		super(parent, "Menu principal", true);
		
		setPreferredSize(new Dimension(300, 350));
		setUndecorated(true);
		
		JPanel mainPanel = new JPanel();
		
		mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		mainPanel.setLayout(new GridLayout(0, 1, 0, 10));
		
		JLabel titleLabel = new JLabel("Menu éditeur", JLabel.CENTER);
		continueButton = new JButton("Continuer l'édition");
		startGameButton = new JButton("Démarrer le jeu");
		resetButton = new JButton("Nouvelle carte");
		saveButton = new JButton("Enregistrer la carte");
		loadButton = new JButton("Charger une carte");
		exitButton = new JButton("Quitter");

		mainPanel.add(titleLabel);
		mainPanel.add(continueButton);
		mainPanel.add(startGameButton);
		mainPanel.add(resetButton);
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
		resetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeMenu();
				editor.resetEditor();
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
	
	public void setStartGameAction(ActionListener a) {
		startGameButton.addActionListener(a);
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

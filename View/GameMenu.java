package View;

import Model.Game;

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

public class GameMenu extends JDialog {
	private static final long serialVersionUID = 4870573801345257186L;
	
	private JLabel titleLabel;
	private JButton continueButton;
	private JButton creatorButton;
	private JButton openButton;
	private JButton saveButton;
	private JButton restoreButton;
	private JButton exitButton;
	
	public GameMenu(Frame parent, Game game) {
		super(parent, "Menu principal", true);
		
		setPreferredSize(new Dimension(300, 350));
		setUndecorated(true); // Avoid the close method from the cross button
		
		JPanel mainPanel = new JPanel();
		
		mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		mainPanel.setLayout(new GridLayout(0, 1, 0, 10));
		
		titleLabel = new JLabel("Jeu en pause", JLabel.CENTER);
		continueButton = new JButton("Continuer la partie");
		creatorButton = new JButton("Éditeur de carte");
		openButton = new JButton("Ouvrir une carte");
		saveButton = new JButton("Enregistrer la partie");
		restoreButton = new JButton("Charger une partie");
		exitButton = new JButton("Quitter");

		mainPanel.add(titleLabel);
		mainPanel.add(continueButton);
		mainPanel.add(creatorButton);
		mainPanel.add(openButton);
		mainPanel.add(saveButton);
		mainPanel.add(restoreButton);
		mainPanel.add(exitButton);
		
		add(mainPanel);
		
		titleLabel.setFont(titleLabel.getFont().deriveFont((float)28));

		continueButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeMenu();
				game.resumeGame();
			}
		});
		openButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				game.openGame();
			}
		});
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				game.saveGame();
			}
		});
		restoreButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				game.restoreGame();
			}
		});
		exitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				game.quit();
			}
		});
	}
	
	public void setCreatorAction(ActionListener a) {
		creatorButton.addActionListener(a);
	}
	
	public void showGamePausedMenu() {
		titleLabel.setText("Jeu en pause");
		continueButton.setEnabled(true);
		showMenu();
	}
	
	public void showGameOverMenu() {
		titleLabel.setText("Game Over !");
		continueButton.setEnabled(false);
		showMenu();
	}
	
	private void showMenu() {
		pack();
		setLocationRelativeTo(getOwner());
		setVisible(true);
	}
	
	public void closeMenu() {
		setVisible(false);
	}
}

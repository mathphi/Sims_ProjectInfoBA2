package View;

import Model.Adult;

import Model.Person;
import Model.Teenager;

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

public class InteractionMenu extends JDialog {
	private static final long serialVersionUID = 4870573801345257186L;

	private JButton discussButton;
	private JButton playButton;
	private JButton inviteButton;
	private JButton drinkButton;
	private JButton embrassButton;
	private JButton marryButton;

	public InteractionMenu(Frame parent, Person player, Person otherPerson) {
		// player is the sender of the request
		super(parent, otherPerson.getName(), true); // Print the name of the people

		setPreferredSize(new Dimension(300, 320));

		JPanel mainPanel = new JPanel();

		mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		mainPanel.setLayout(new GridLayout(0, 1, 0, 10));

		JLabel interactionLabel = new JLabel("Intéractions", JLabel.CENTER);
		discussButton = new JButton("Discuter");
		playButton = new JButton("Jouer");
		inviteButton = new JButton("Inviter");
		drinkButton = new JButton("Aller boire un verre");
		embrassButton = new JButton("Embrasser");
		marryButton = new JButton("Se marier");
		mainPanel.add(interactionLabel);

		switch (player.getRelationship(otherPerson)) {
		case (1):
			// can play with
			mainPanel.add(playButton);
			break;
		case (2):
			mainPanel.add(playButton);
			mainPanel.add(inviteButton);
			break;
		default:
			break;
		}

		if (otherPerson instanceof Teenager) {
			if (player instanceof Teenager) {
				switch (player.getRelationship(otherPerson)) {
				case (2):
					mainPanel.add(drinkButton);
					break;
				case (3):
					mainPanel.add(drinkButton);
					mainPanel.add(embrassButton);
					break;
				default:
					break;
				}
			}
		} 
		else if (otherPerson instanceof Adult) {
			if (player instanceof Teenager) {
				switch (player.getRelationship(otherPerson)) {
				case (2):
					mainPanel.add(drinkButton);
					break;
				case (3):
					mainPanel.add(drinkButton);
					mainPanel.add(embrassButton);
					break;
				default:
					break;
				}
			} 
			else if (player instanceof Adult) {
				switch (player.getRelationship(otherPerson)) {
				case (2):
					mainPanel.add(drinkButton);
					break;
				case (3):
					mainPanel.add(drinkButton);
					mainPanel.add(embrassButton);
					mainPanel.add(marryButton);
					break;
				default:
					break;
				}
			}
		}

		mainPanel.add(discussButton);

		add(mainPanel);

		interactionLabel.setFont(interactionLabel.getFont().deriveFont((float) 28));

		discussButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeMenu();
				player.characterInteraction(otherPerson, "discuss");
			}
		});

		playButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeMenu();
				player.characterInteraction(otherPerson, "playWith");
			}
		});

		inviteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeMenu();
				player.characterInteraction(otherPerson, "invite");
			}
		});

		embrassButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeMenu();
				player.characterInteraction(otherPerson, "embrass");
			}
		});

		drinkButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeMenu();
				player.characterInteraction(otherPerson, "goTodrink");
			}
		});

		marryButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeMenu();
				player.characterInteraction(otherPerson, "marry");
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

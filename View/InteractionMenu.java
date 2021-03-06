package View;

import Model.Adult;
import Model.Person;
import Model.Person.InteractionType;
import Model.Person.Relationship;
import Model.Teenager;

import java.awt.BorderLayout;
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

	private static enum PersonType {
		Kid, Teenager, Adult
	}
	
	private InteractionType selectedInterraction = InteractionType.None;

	public InteractionMenu(
			Frame parent,
			String name,
			Relationship relationship,
			Person p1,
			Person p2)
	{
		super(parent, "Intéractions", true);
		setUndecorated(true);

		JPanel mainPanel = new JPanel();

		mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		mainPanel.setLayout(new GridLayout(0, 1, 0, 10));

		JLabel nameLabel = new JLabel(name, JLabel.CENTER);
		nameLabel.setFont(nameLabel.getFont().deriveFont((float) 20));
		mainPanel.add(nameLabel);
		
		JButton discussButton = new JButton("Discuter");
		JButton playButton = new JButton("Jouer");
		JButton drinkButton = new JButton("Aller boire un verre");
		JButton kissButton = new JButton("Embrasser");
		JButton marryButton = new JButton("Se marier");
		
		JPanel controlPanel = new JPanel();
		controlPanel.setLayout(new BorderLayout());
		controlPanel.setBorder(new EmptyBorder(10, 80, 10, 80));
		
		JButton cancelButton = new JButton("Annuler");
		controlPanel.add(cancelButton, BorderLayout.CENTER);

		PersonType p1_type = getPersonType(p1);
		PersonType p2_type = getPersonType(p2);

		/*
		 * Cascading switch-case. SO DON'T ADD BREAK HERE !
		 * For example a close friend can do the same things as an acquaintance
		 * and more (invite and drink).
		 */
		switch (relationship) {
		case VerySeriousRelation:
			if (p1_type == PersonType.Adult && p2_type == PersonType.Adult) {
				mainPanel.add(marryButton);
			}
		case SeriousRelation:
			if (p1_type == p2_type) {
				// Don't kiss a Person of another level (Kid and Adult for example...)
				mainPanel.add(kissButton);
			}
		case Parent: 
			// This section comes after SeriousRelation because a
			// Person cannot marry or kiss his parents...
		case CloseFriend:
			if (p1_type != PersonType.Kid && p2_type != PersonType.Kid) {
				// A Kid cannot drink
				mainPanel.add(drinkButton);
			}
		case Acquaintance:
			mainPanel.add(playButton);
		default:
			mainPanel.add(discussButton);
			break;
		}

		mainPanel.add(controlPanel);

		add(mainPanel);
		
		// Adapt the window height to content
		setPreferredSize(new Dimension(350, 75 + 50 * (mainPanel.getComponentCount() - 1)));

		ActionListener buttonsAction = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Object src = e.getSource();
				
				if (src == discussButton) {
					selectedInterraction = InteractionType.Discuss;
				} else if (src == playButton) {
					selectedInterraction = InteractionType.Play;
				} else if (src == kissButton) {
					selectedInterraction = InteractionType.Kiss;
				} else if (src == drinkButton) {
					selectedInterraction = InteractionType.Drink;
				} else if (src == marryButton) {
					selectedInterraction = InteractionType.Marry;
				}
				
				closeMenu();
			}
		};

		discussButton.addActionListener(buttonsAction);
		playButton.addActionListener(buttonsAction);
		kissButton.addActionListener(buttonsAction);
		drinkButton.addActionListener(buttonsAction);
		marryButton.addActionListener(buttonsAction);
		cancelButton.addActionListener(buttonsAction);
	}
	
	public static InteractionType showInterractionMenu(
			Frame parent,
			String name,
			Relationship relationship,
			Person p1,
			Person p2)
	{
		InteractionMenu menu = new InteractionMenu(parent, name, relationship, p1, p2);
		menu.showMenu();
		
		return menu.getSelectedInterraction();
	}
	
	private InteractionType getSelectedInterraction() {
		return selectedInterraction;
	}
	
	private PersonType getPersonType(Person p) {
		PersonType type;
		
		if (p instanceof Adult) {
			type = PersonType.Adult;
		} else if (p instanceof Teenager) {
			type = PersonType.Teenager;
		} else {
			type = PersonType.Kid;
		}
		
		return type;
	}

	private void showMenu() {
		pack();
		setLocationRelativeTo(getOwner());
		setVisible(true);
	}

	private void closeMenu() {
		setVisible(false);
	}
}

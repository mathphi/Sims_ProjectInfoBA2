package View;

import Model.Adult;
import Model.Person;
import Model.Person.InteractionType;
import Model.Person.Relationship;
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
import Model.Game;

public class PersonalMenu extends JDialog {
	private static final long serialVersionUID = 4870573801345257186L;

	private JButton bank;
	private JButton inventory;
	private JButton catalog;

	private Game game;
	public

	PersonalMenu(Frame parent, Person activePerson, Game game) {
		super(parent, "Argent:" + activePerson.getMoney(), true);

		setPreferredSize(new Dimension(300, 320));

		JPanel mainPanel = new JPanel();
		this.game = game;
		mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		mainPanel.setLayout(new GridLayout(0, 1, 0, 10));

		JLabel nameLabel = new JLabel("Gestion argent", JLabel.CENTER);
		nameLabel.setFont(nameLabel.getFont().deriveFont((float) 28));
		mainPanel.add(nameLabel);

		bank = new JButton("Banque");
		inventory = new JButton("Inventaire personnel");
		catalog = new JButton("Catalogue");

		mainPanel.add(bank);
		mainPanel.add(inventory);
		mainPanel.add(catalog);

		add(mainPanel);

		ActionListener buttonsAction = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Object src = e.getSource();

				if (src == bank) {
					// eventualy TODO: open bank
				} else if (src == inventory) {
					InventoryMenu inventoryMenu = new InventoryMenu(parent, activePerson, game);
					inventoryMenu.showMenu();
				} else if (src == catalog) {
					CatalogMenu catalogMenu = new CatalogMenu(parent, activePerson, game);

				}
				closeMenu();

			}

		};

		bank.addActionListener(buttonsAction);
		catalog.addActionListener(buttonsAction);
		inventory.addActionListener(buttonsAction);

	}

	public void showMenu() {
		pack();
		setLocationRelativeTo(getOwner());
		setVisible(true);
	}

	public void closeMenu() {
		setVisible(false);
		game.resumeGame();

	}
}

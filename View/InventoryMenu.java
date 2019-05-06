package View;

import Model.Adult;
import Model.Game;
import Model.Person;
import Model.Person.InteractionType;
import Model.Person.Relationship;
import Model.Teenager;
import Products.Product;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class InventoryMenu extends JDialog {
	private static final long serialVersionUID = 4870573801345257186L;
	private Game game;
	private JButton item1;
	private JButton item2;
	private JButton item3;
	private JButton item4;
	private JButton item5;

	public InventoryMenu(Frame parent, Person activePerson, Game game) {
		super(parent, "Argent:" + activePerson.getMoney(), true);
		this.game = game;
		setPreferredSize(new Dimension(300, 320));

		JPanel mainPanel = new JPanel();

		mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		mainPanel.setLayout(new GridLayout(0, 1, 0, 10));

		JLabel nameLabel = new JLabel("Inventaire", JLabel.CENTER);
		nameLabel.setFont(nameLabel.getFont().deriveFont((float) 28));
		mainPanel.add(nameLabel);
		ArrayList<Product> inventoryList = activePerson.getInventory();

		item1 = new JButton("Vide");
		item2 = new JButton("Vide");
		item3 = new JButton("Vide");
		item4 = new JButton("Vide");
		item5 = new JButton("Vide");

		int inventorySize = inventoryList.size();
		if (inventorySize >= 1) {
			item1 = new JButton(inventoryList.get(0).getName());
		}
		if (inventorySize >= 2) {
			item2 = new JButton(inventoryList.get(1).getName());
		}
		if (inventorySize >= 3) {
			item3 = new JButton(inventoryList.get(2).getName());
		}
		if (inventorySize >= 4) {
			item4 = new JButton(inventoryList.get(3).getName());
		}
		if (inventorySize >= 5) {
			item5 = new JButton(inventoryList.get(4).getName());
		}

		mainPanel.add(item1);
		mainPanel.add(item2);
		mainPanel.add(item3);
		mainPanel.add(item4);
		mainPanel.add(item5);

		add(mainPanel);

		ActionListener buttonsAction = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Object src = e.getSource();

				if (src == item1 && inventorySize >= 1) {
					activePerson.useInventory(inventoryList.get(0));
				} else if (src == item2 && inventorySize >= 2) {
					activePerson.useInventory(inventoryList.get(1));
				} else if (src == item3 && inventorySize >= 3) {
					activePerson.useInventory(inventoryList.get(2));
				} else if (src == item4 && inventorySize >= 4) {
					activePerson.useInventory(inventoryList.get(3));
				} else if (src == item5 && inventorySize >= 5) {
					activePerson.useInventory(inventoryList.get(4));
				}

				closeMenu();
			}

		};

		item1.addActionListener(buttonsAction);
		item2.addActionListener(buttonsAction);
		item3.addActionListener(buttonsAction);
		item4.addActionListener(buttonsAction);
		item5.addActionListener(buttonsAction);

	}

	void showMenu() {
		pack();
		setLocationRelativeTo(getOwner());
		setVisible(true);
	}

	private void closeMenu() {
		setVisible(false);
		game.resumeGame();
	}
}

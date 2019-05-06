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

public class CatalogMenu extends JDialog {
	private static final long serialVersionUID = 4870573801345257186L;

	private JButton food;
	private JButton clothes;
	private JButton toys;
	private JButton others;
	private Game game;

	public CatalogMenu(Frame parent, Person activePerson, Game game) {
		super(parent, "Argent:" + activePerson.getMoney(), true);

		setPreferredSize(new Dimension(300, 320));
		this.game = game;
		JPanel mainPanel = new JPanel();

		mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		mainPanel.setLayout(new GridLayout(0, 1, 0, 10));

		JLabel nameLabel = new JLabel("Catalogue d'achat", JLabel.CENTER);
		nameLabel.setFont(nameLabel.getFont().deriveFont((float) 28));
		mainPanel.add(nameLabel);
		

		food = new JButton("Nourritures");
		clothes = new JButton("Vêtements");
		toys = new JButton("Jouets");
		others = new JButton("Autres");

		mainPanel.add(food);
		mainPanel.add(clothes);
		mainPanel.add(toys);
		mainPanel.add(others);

		add(mainPanel);

		ActionListener buttonsAction = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Object src = e.getSource();
				if(src == food) {
				BuyableMenu productMenu = new BuyableMenu(parent, "Nourriture", activePerson);
				productMenu.showMenu();
				
				}
				if(src == clothes) {
					BuyableMenu productMenu = new BuyableMenu(parent, "Vêtements", activePerson);
					productMenu.showMenu();
					
					}
				if(src == toys) {
					BuyableMenu productMenu = new BuyableMenu(parent, "Jeux", activePerson);
					productMenu.showMenu();
					
					}
				if(src == others) {
					BuyableMenu productMenu = new BuyableMenu(parent, "Autres", activePerson);
					productMenu.showMenu();
					
					}
				
				
				closeMenu();
			}

		};

		
		food.addActionListener(buttonsAction);
		clothes.addActionListener(buttonsAction);
		toys.addActionListener(buttonsAction);
		others.addActionListener(buttonsAction);
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

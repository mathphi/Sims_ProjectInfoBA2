package View;

import Model.Adult;
import Model.Person;
import Model.Person.InteractionType;
import Model.Person.Relationship;
import Model.Teenager;
import Products.Cloth;
import Products.Nourriture;
import Products.Other;
import Products.Product;
import Products.Toy;

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

public class BuyableMenu extends JDialog {
	private static final long serialVersionUID = 4870573801345257186L;

	private JButton item1;
	private JButton item2;
	private JButton item3;
	private JButton item4;
	private JButton item5;

	public BuyableMenu(Frame parent, String objectType, Person activePerson) {
		super(parent, "Argent:" + activePerson.getMoney(), true);

		setPreferredSize(new Dimension(500, 300));
		JPanel mainPanel = new JPanel();
		JLabel nameLabel = new JLabel("", JLabel.CENTER);
		mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		mainPanel.setLayout(new GridLayout(0, 1, 0, 10));

		
		
		ActionListener buttonsAction = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object src = e.getSource();
				if (src == item1) {
					//TODO sera remplacé par la création de l'objet -> buy recevra objet directement
					activePerson.buy(new Nourriture("café", 3,5,10,5));

				}
				if (src == item2) {
					activePerson.buy(new Nourriture("Collation", 4,7,15,5));
				}
				if (src == item3) {
					activePerson.buy(new Nourriture("Salade", 5,10,17,10));

				}
				if (src == item4) {
					activePerson.buy(new Nourriture("Burger", 7,12,17,15));

				}
				if (src == item5) {
					activePerson.buy(new Nourriture("Repas complet", 10,15,20,20));

				}
			

			}

		};
		
		
		switch (objectType) {
		case ("Nourriture"): {
			nameLabel = new JLabel("Nourriture", JLabel.CENTER);

			item1 = new JButton("Café (restore peu l'énergie, nourrit peu et coute 3€)");
			item2 = new JButton("Collation (restore l'énergie, nourrit peu et coute 4€)");
			item3 = new JButton("Salade (restore  l'énergie, nourrit et coute 5€)");
			item4 = new JButton("Burger (restore l'énergie, nourrit bien et coute 7€)"); // TODO réduire santé
			item5 = new JButton("Repas complet (restore bien l'énergie, nourrit bien et coute 10€)");

			

			break;
		}
		case ("Jeux"): {
			nameLabel = new JLabel("Jeux", JLabel.CENTER);
			item1 = new JButton("Jeu de cartes (vous amusera un peu, coute 5€)");
			item2 = new JButton("Ballon de foot  (vous amusera un peu plus  coute  6€)");
			item3 = new JButton("Jeu de société (vous amusera bien, coute 10€)");
			item4 = new JButton("Télévision (vous amusera vraiment! Coute 100€)");
			item5 = new JButton("PS4 (vous amusera définitivement! Coute 250€)");

			 buttonsAction = new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Object src = e.getSource();
					if (src == item1) {
						activePerson.buy(new Toy("Jeu de carte",5, 10, -5));

					}
					if (src == item2) {
						activePerson.buy(new Toy("Ballon de foot", 6,17, -15));

					}
					if (src == item3) {
						activePerson.buy(new Toy("Jeux de société",10, 20, -8));

					}
					if (src == item4) {
						activePerson.buy(new Toy("Télévision",100, 25,-5));
					}
					if (src == item5) {
						activePerson.buy(new Toy("PS4",200, 50, -10));

					}
			

				}

			};
			break;
		}
		case ("Autres"): {
			nameLabel = new JLabel("Autres", JLabel.CENTER);
			item1 = new JButton("Livres (permet d'augmenter vos connaissances intelectuelles, coute 12€");
			item2 = new JButton(
					"Likes sur instagram  (vous permettra d'augmenter l'image que les autres ont de vous! 30€)");
			item3 = new JButton("Médicaments (permet de vous soigner en cas de maladie 10€)");
			item4 = new JButton("Déodorant (remonte votre hygiène et vous fait sentir bon 5€)");
			item5 = new JButton("Somnifère (permet de récupérer de l'énergie 15€)");
buttonsAction = new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Object src = e.getSource();
					if (src == item1) {
						activePerson.buy(new Other("Livres", 12, -5, -5,0, 0,5,5));

					}
					if (src == item2) {
						activePerson.buy(new Other("Like sur instagram", 30, 10,0, 0, 0,-1, 15));
					}
					if (src == item3) {
						activePerson.buy(new Other("Médicament", 10, 0, 0,0, 5,0, 20));
					}
					if (src == item4) {
						activePerson.buy(new Other("Déodorant", 5,5, 0, 0, 15,0,0));

					}
					if (src == item5) {
						activePerson.buy(new Other("Somnifère", 15, -5, 15, 0,-5,0,-5));

					}
			

				}

			};
			break;
		}
		case ("Vêtements"): {
			nameLabel = new JLabel("Vêtements", JLabel.CENTER);
			item1 = new JButton("t-shirt 5€");
			item2 = new JButton("Pull 15€");
			item3 = new JButton("Chemise 30€");
			item4 = new JButton("Veste 60€");
			item5 = new JButton("Smoking 120€");

			buttonsAction = new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Object src = e.getSource();
					if (src == item1) {
						activePerson.buy(new Cloth("T-shirt", 5, 10,10) );

					}
					if (src == item2) {
						activePerson.buy(new Cloth("Pull", 15, 15, 15));
					}
					if (src == item3) {
						activePerson.buy(new Cloth("Chemise", 30, 25, 17));

					}
					if (src == item4) {
						activePerson.buy(new Cloth("Veste", 60, 30, 20));

					}
					if (src == item5) {
						activePerson.buy(new Cloth("Smoking", 300,50, 50));
					}
		

				}

			};
			break;

		}
		}
		nameLabel.setFont(nameLabel.getFont().deriveFont((float) 28));
		mainPanel.add(nameLabel);
		mainPanel.add(item1);
		mainPanel.add(item2);
		mainPanel.add(item3);
		mainPanel.add(item4);
		mainPanel.add(item5);


		item1.addActionListener(buttonsAction);
		item2.addActionListener(buttonsAction);
		item3.addActionListener(buttonsAction);
		item4.addActionListener(buttonsAction);
		item5.addActionListener(buttonsAction);
	

		add(mainPanel);

	}

	void showMenu() {
		pack();
		setLocationRelativeTo(getOwner());
		setVisible(true);
	}

	private void closeMenu() {
		setVisible(false);
	}
}

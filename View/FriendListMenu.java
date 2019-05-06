package View;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.JButton;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import javax.swing.border.EmptyBorder;

import Model.Game;
import Model.Person;
import Model.Person.Relationship;

public class FriendListMenu extends JDialog {
	private static final long serialVersionUID = -6014304203322845327L;

	private boolean validated = false;
	private Map<Person, Double>friendList;

	private JComboBox<String> acquaintance;
	private JComboBox<String> friend;
	private JComboBox<String> closeFriend;
	private JComboBox<String> seriousRelation;
	private JComboBox<String> verySeriousRelation;
	private Game game;

	public FriendListMenu(Frame parent,Person activePlayer, Game game) {
		super(parent, "Liste d'amis", true);

		friendList = activePlayer.getFriendList();

		setPreferredSize(new Dimension(300, 250));
		setLayout(new BorderLayout());

		JPanel mainPanel = new JPanel();

		mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		mainPanel.setLayout(new GridBagLayout());

		for(Person friend:friendList.keySet()) {
			Relationship relation =activePlayer.getRelationship(friend);
			switch(relation) {
			case Acquaintance:{
				
			}
			}
		}
		
		JLabel acquaintanceLabel = new JLabel("Connaissances");
		String[] acquaintanceListName = { "Homme", "Femme" };
		acquaintance = new JComboBox<String>(acquaintanceListName);

		JLabel friendLabel = new JLabel("Amis");
		String[] friendListName = { "Homme", "Femme" };
		friend = new JComboBox<String>(friendListName);

		JLabel closeFriendLabel = new JLabel("Amis proches");
		String[] closeFriendListName = { "Homme", "Femme" };
		closeFriend = new JComboBox<String>(closeFriendListName);
		
		
		//TODO
		//seriousRelation JComboBox<String> seriousRelation;
		//private JComboBox<String> verySeriousRelation;

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.5;
		c.weighty = 0.5;

		c.gridx = 0;
		c.gridy = 0;
		mainPanel.add(acquaintanceLabel, c);

		c.gridx = 0;
		c.gridy = 1;
		mainPanel.add(acquaintance, c);

		c.gridx = 1;
		c.gridy = 0;
		mainPanel.add(friendLabel, c);

		c.gridx = 1;
		c.gridy = 1;
		mainPanel.add(friend, c);

		c.gridx = 2;
		c.gridy = 0;
		mainPanel.add(closeFriendLabel, c);

		c.gridx = 2;
		c.gridy = 1;
		mainPanel.add(closeFriend, c);

		JPanel buttonsContainer = new JPanel();
		buttonsContainer.setLayout(new GridLayout(1, 2, 10, 10));
		buttonsContainer.setBorder(new EmptyBorder(10, 10, 10, 10));

		JButton btnCancel = new JButton("Annuler");
		JButton btnOk = new JButton("Voir fiche");

		buttonsContainer.add(btnCancel);
		buttonsContainer.add(btnOk);

		add(mainPanel, BorderLayout.CENTER);
		add(buttonsContainer, BorderLayout.SOUTH);

		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				closeMenu();
			}
		});

		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				// TODO open windows with more details about the selected people
				closeMenu();
			}
		});
	}



	public void closeMenu() {
		setVisible(false);
		game.resumeGame();
	}

	public void showMenu() {
		pack();
		setLocationRelativeTo(getOwner());
		setVisible(true);
		
	}
}





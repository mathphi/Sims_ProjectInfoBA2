package View;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import Model.Person;
import Model.Person.Gender;
import Model.Person.Relationship;
import Model.PsychologicalFactors.CharacterTraits;

public class FriendListDialog extends JDialog {
	private static final long serialVersionUID = -6014304203322845327L;

	private Map<Person, Double> friendList;
	private Person activePerson;

	public FriendListDialog(Frame parent, Person person) {
		super(parent, "Liste d'amis", true);

		this.friendList = person.getFriendList();
		this.activePerson = person;
		
		setPreferredSize(new Dimension(700, 275));
		setUndecorated(true);
		setLayout(new BorderLayout());
		
		JLabel titleLabel = new JLabel("Liste d'amis de " + person.getName(), JLabel.CENTER);
		titleLabel.setFont(titleLabel.getFont().deriveFont((float) 20));
		titleLabel.setBorder(new EmptyBorder(10, 10, 20, 10));
		add(titleLabel, BorderLayout.NORTH);

		JPanel mainPanel = new JPanel();
		mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		mainPanel.setLayout(new BorderLayout());
		
		JPanel scrollPanel = new JPanel();
		scrollPanel.setLayout(new BoxLayout(scrollPanel, BoxLayout.Y_AXIS));
		
		// Set scrollable panel
		JScrollPane scrollView = new JScrollPane(scrollPanel);
		scrollView.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollView.setBorder(null);
		
		if (friendList.size() > 0) {
			mainPanel.add(createHeader(), BorderLayout.NORTH);
			
			for (Person friend : friendList.keySet()) {
				scrollPanel.add(createFriendRow(friend));

				scrollPanel.add(new Box.Filler(
						new Dimension(0, 10), 
		                new Dimension(0, 10), 
		                new Dimension(0, 10)));
			}
		}
		else {
			JPanel panel = new JPanel();
			panel.setLayout(new BorderLayout());
			
			JLabel emptyLabel = new JLabel("Vous n'avez pas encore fait de rencontre");
			emptyLabel.setHorizontalAlignment(JLabel.CENTER);
			emptyLabel.setFont(emptyLabel.getFont().deriveFont(Font.ITALIC, (float) 14.0));
			
			panel.add(emptyLabel, BorderLayout.CENTER);
			scrollPanel.add(panel);
		}

		JPanel buttonsContainer = new JPanel();
		buttonsContainer.setLayout(new GridLayout(1, 3, 10, 10));
		buttonsContainer.setBorder(new EmptyBorder(20, 10, 10, 10));

		JButton closeButton = new JButton("Fermer");

		buttonsContainer.add(Box.createHorizontalStrut(20));
		buttonsContainer.add(closeButton);
		buttonsContainer.add(Box.createHorizontalStrut(20));
		
		mainPanel.add(scrollView, BorderLayout.CENTER);
		mainPanel.add(buttonsContainer, BorderLayout.SOUTH);

		add(mainPanel, BorderLayout.CENTER);
		
		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeDialog();
			}
		});
	}
	
	private JPanel createHeader() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		
		JPanel hPanel = new JPanel();
		hPanel.setLayout(new GridLayout(1, 0, 10, 0));
		hPanel.setBorder(new EmptyBorder(0, 0, 10, 0));

		JLabel nameLabel = new JLabel("Nom");
		JLabel genderLabel = new JLabel("Sexe");
		JLabel ageLabel = new JLabel("Age");
		JLabel realtionLabel = new JLabel("Relation");
		JLabel characterLabel = new JLabel("Caractère");

		nameLabel.setFont(nameLabel.getFont().deriveFont(Font.ITALIC + Font.BOLD));
		genderLabel.setFont(genderLabel.getFont().deriveFont(Font.ITALIC + Font.BOLD));
		ageLabel.setFont(ageLabel.getFont().deriveFont(Font.ITALIC + Font.BOLD));
		realtionLabel.setFont(realtionLabel.getFont().deriveFont(Font.ITALIC + Font.BOLD));
		characterLabel.setFont(realtionLabel.getFont().deriveFont(Font.ITALIC + Font.BOLD));

		hPanel.add(nameLabel);
		hPanel.add(genderLabel);
		hPanel.add(ageLabel);
		hPanel.add(realtionLabel);
		hPanel.add(characterLabel);
		
		panel.add(hPanel, BorderLayout.CENTER);
		panel.add(new JSeparator(SwingConstants.HORIZONTAL), BorderLayout.SOUTH);
		
		return panel;
	}
	
	private JPanel createFriendRow(Person friend) {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1, 0, 10, 0));
		panel.setBorder(new EmptyBorder(10, 0, 10, 0));

		JLabel nameLabel = new JLabel(friend.getName());
		JLabel genderLabel = new JLabel(friend.getGender() == Gender.Male ? "Homme" : "Femme");
		JLabel ageLabel = new JLabel(String.format("%d ans", friend.getAge()));
		JLabel realtionLabel = new JLabel(getRelationToStr(friend));
		JLabel characterLabel = new JLabel(getCharacterToStr(friend));
		
		nameLabel.setFont(nameLabel.getFont().deriveFont(Font.BOLD));
		genderLabel.setFont(genderLabel.getFont().deriveFont(Font.PLAIN));
		ageLabel.setFont(ageLabel.getFont().deriveFont(Font.PLAIN));
		realtionLabel.setFont(realtionLabel.getFont().deriveFont(Font.PLAIN));
		characterLabel.setFont(realtionLabel.getFont().deriveFont(Font.PLAIN));

		panel.add(nameLabel);
		panel.add(genderLabel);
		panel.add(ageLabel);
		panel.add(realtionLabel);
		panel.add(characterLabel);
		
		// Constrain the height of the row to his minimum
		Dimension d = new Dimension(Integer.MAX_VALUE, (int)panel.getMinimumSize().getHeight());
		panel.setMaximumSize(d);
		
		return panel;
	}
	
	private String getCharacterToStr(Person p) {
		CharacterTraits ct = p.getPsychologicalFactor().getPrincipleCharacterTrait();
		String char_str = "/";
		
		switch (ct) {
		case Mood:
			char_str = "Sensible";
			break;
		case Hygiene:
			char_str = "Hypocondriaque";
			break;
		case GeneralKnowledge:
			char_str = "Intellectuel";
			break;
		case OthersImpression:
			char_str = "Extraverti";
			break;
		default:
			break;
		}
		
		return char_str;
	}
	
	private String getRelationToStr(Person p) {
		Relationship rs = activePerson.getRelationship(p);
		String rel_str = "/";
		
		switch (rs) {
		case Acquaintance:
			rel_str = "Connaissance";
			break;
		case CloseFriend:
			rel_str = "Ami proche";
			break;
		case SeriousRelation:
			rel_str = "Relation sérieuse";
			break;
		case VerySeriousRelation:
			rel_str = "Très sérieuse";
			break;
		case Married:
			rel_str = "Époux/Épouse";
			break;
		case Parent:
			if (activePerson.getFather() == p)
				rel_str = "Père";
			else if (activePerson.getMother() == p)
				rel_str = "Mère";
			else if (p.getFather() == activePerson || p.getMother() == activePerson)
				rel_str = "Enfant";
			break;
		default:
			break;
		}
		
		return rel_str;
	}

	public void closeDialog() {
		setVisible(false);
	}

	public void showDialog() {
		pack();
		setLocationRelativeTo(getOwner());
		setVisible(true);
	}
}

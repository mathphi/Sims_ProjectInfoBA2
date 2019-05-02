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
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;

import Model.Adult;
import Model.Person;
import Model.Person.Gender;

public class NewPersonForm extends JDialog {
	private static final long serialVersionUID = -6014304203322845327L;
	
	private boolean validated = false;
	private ArrayList<Person> population;
	
	private JTextField nameField;
	private JComboBox<String> genderField;
	private JComboBox<String> fatherField;
	private JComboBox<String> motherField;
	private JSpinner ageField;
	private JCheckBox unplayableField;

	public NewPersonForm(Frame parent, ArrayList<Person> population) {
		super(parent, "Nouveau personnage", true);
		
		this.population = population;
		
		setPreferredSize(new Dimension(300, 250));
		setLayout(new BorderLayout());
		
		JPanel mainPanel = new JPanel();
		
		mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		mainPanel.setLayout(new GridBagLayout());

		JLabel nameLabel = new JLabel("Nom complet");
		nameField = new JTextField();
		
		JLabel genderLabel = new JLabel("Genre");
		String[] gendersList = {"Homme", "Femme"};
		genderField = new JComboBox<String>(gendersList);
		
		JLabel fatherLabel = new JLabel("Père");
		fatherField = new JComboBox<String>(getAdultsNames(population, Gender.Male));
		
		JLabel motherLabel = new JLabel("Mère");
		motherField = new JComboBox<String>(getAdultsNames(population, Gender.Female));

		JLabel ageLabel = new JLabel("Age initial");
		SpinnerNumberModel spinnerModel = new SpinnerNumberModel(10, 4, 99, 1);
		ageField = new JSpinner(spinnerModel);
		
		unplayableField = new JCheckBox("Personnage non-joueur");

		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.5;
		c.weighty = 0.5;
		
		c.gridx = 0;
		c.gridy = 0;
		mainPanel.add(nameLabel, c);
		
		c.gridx = 1;
		c.gridy = 0;
		mainPanel.add(nameField, c);
		
		c.gridx = 0;
		c.gridy = 1;
		mainPanel.add(genderLabel, c);

		c.gridx = 1;
		c.gridy = 1;
		mainPanel.add(genderField, c);

		c.gridx = 0;
		c.gridy = 2;
		mainPanel.add(fatherLabel, c);

		c.gridx = 1;
		c.gridy = 2;
		mainPanel.add(fatherField, c);

		c.gridx = 0;
		c.gridy = 3;
		mainPanel.add(motherLabel, c);

		c.gridx = 1;
		c.gridy = 3;
		mainPanel.add(motherField, c);

		c.gridx = 0;
		c.gridy = 4;
		mainPanel.add(ageLabel, c);
		
		c.gridx = 1;
		c.gridy = 4;
		mainPanel.add(ageField, c);

		c.gridx = 0;
		c.gridy = 5;
		c.gridwidth = 2;
		mainPanel.add(unplayableField, c);
		c.gridwidth = 0;
		
		
		JPanel buttonsContainer = new JPanel();
		buttonsContainer.setLayout(new GridLayout(1,2, 10, 10));
		buttonsContainer.setBorder(new EmptyBorder(10, 10, 10, 10));

		JButton btnCancel = new JButton("Annuler");
		JButton btnOk = new JButton("Ok");
		
		buttonsContainer.add(btnCancel);
		buttonsContainer.add(btnOk);
		
		add(mainPanel, BorderLayout.CENTER);
		add(buttonsContainer, BorderLayout.SOUTH);

		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				validated = false;
				closeForm();
			}
		});

		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!nameField.getText().isEmpty()) { //Attention j'ai du changer le isBlanck car était pas défini pour les str
					validated = true;
					closeForm();
				}
			}
		});
	}
	
	private Vector<String> getAdultsNames(ArrayList<Person> pop, Gender g) {
		Vector<String> lst = new Vector<String>();
		
		lst.add("Aucun");
		
		for (Person p : pop) {
			if (p instanceof Adult && p.getGender() == g) {
				lst.add(p.getName());
			}
		}
		
		return lst;
	}
	
	private Adult getAdultWithName(String name, Gender g) {
		for (Person p : population) {
			if (p instanceof Adult && p.getGender() == g && p.getName() == name) {
				return (Adult) p;
			}
		}
		
		return null;
	}

	public boolean showForm() {
		pack();
		setLocationRelativeTo(getOwner());
		setVisible(true);
		
		return validated;
	}
	
	public void closeForm() {
		setVisible(false);
	}
	
	public String getName() {
		return nameField.getText();
	}
	
	public int getAge() {
		return (int) ageField.getValue();
	}
	
	public Gender getGender() {
		return (genderField.getSelectedItem() == "Homme" ? Gender.Male : Gender.Female);
	}
	
	public Adult getFather() {
		return getAdultWithName((String)fatherField.getSelectedItem(), Gender.Male);
	}

	public Adult getMother() {
		return getAdultWithName((String)motherField.getSelectedItem(), Gender.Female);
	}
	
	public boolean getPlayable() {
		return !unplayableField.isSelected();
	}
}

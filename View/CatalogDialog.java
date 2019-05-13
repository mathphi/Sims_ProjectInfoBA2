package View;

import Model.Person;
import Model.Person.Gender;
import Products.AgeLimited;
import Products.Cloth;
import Products.Food;
import Products.GenderConstrained;
import Products.Other;
import Products.GenderConstrained.GenderConstraint;
import Resources.ResourceLoader;
import Products.Product;
import Products.Toy;
import Tools.Size;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

public class CatalogDialog extends JDialog {
	private static final long serialVersionUID = 4870573801345257186L;
	private static final String PRODUCTS_FILE_PATH = "Data/Products.csv";
	private static final Size ICON_SIZE = new Size(48, 48);
	
	// Static list to load the file once
	private static ArrayList<Product> catalogProductsList;
	
	private Person activePerson;
	private JLabel accountLabel;

	public CatalogDialog(Frame parent, Person person) {
		super(parent, "Catalogue", true);
		
		this.activePerson = person;

		// Read the CSV file only at first opening of the catalog
		if (catalogProductsList == null)
			loadProductsList();
		
		setPreferredSize(new Dimension(800, 600));
		setLayout(new BorderLayout());
		setUndecorated(true);

		JLabel titleLabel = new JLabel("Catalogue d'achats", JLabel.CENTER);
		titleLabel.setBorder(new EmptyBorder(10, 0, 10, 0));
		titleLabel.setFont(titleLabel.getFont().deriveFont((float) 24));
		
		accountLabel = new JLabel(
				String.format("<html><b>Solde restant :</b> %d€</html>", person.getMoney()));
		accountLabel.setFont(accountLabel.getFont().deriveFont(Font.PLAIN, (float) 18));
		accountLabel.setBorder(new EmptyBorder(0, 10, 0, 10));

		JPanel mainPanel = new JPanel();
		mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		mainPanel.setLayout(new BorderLayout());
		
		VerticalScrollPanel contentPanel = new VerticalScrollPanel();
		contentPanel.setLayout(new GridLayout(0, 1, 0, 40));
		contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		// Set scrollable panel
		JScrollPane contentScrollView = new JScrollPane(
				contentPanel,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		contentScrollView.setBorder(null);

		contentPanel.add(createCategoryList("Nourriture", Food.class));
		contentPanel.add(createCategoryList("Jeux", Toy.class));
		contentPanel.add(createCategoryList("Vêtements", Cloth.class));
		contentPanel.add(createCategoryList("Divers", Other.class));

		JPanel buttonsContainer = new JPanel();
		buttonsContainer.setLayout(new GridLayout(1, 0, 20, 0));
		buttonsContainer.setBorder(new EmptyBorder(10, 10, 20, 10));

		JButton closeButton = new JButton("Fermer");
		
		buttonsContainer.add(Box.createHorizontalStrut(20));
		buttonsContainer.add(closeButton);
		buttonsContainer.add(Box.createHorizontalStrut(20));

		mainPanel.add(accountLabel, BorderLayout.NORTH);
		mainPanel.add(contentScrollView, BorderLayout.CENTER);
		
		add(titleLabel, BorderLayout.NORTH);
		add(mainPanel, BorderLayout.CENTER);
		add(buttonsContainer, BorderLayout.SOUTH);

		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeDialog();
			}
		});
	}
	
	private void updateRemainingAccount() {
		accountLabel.setText(
				String.format(
						"<html><b>Solde restant :</b> %d€</html>",
						activePerson.getMoney()));
	}
	
	private JPanel createCategoryList(String title, Class<?> categoryCls) {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout(0, 10));
		
		JLabel titleLabel = new JLabel(title, JLabel.CENTER);
		titleLabel.setFont(titleLabel.getFont().deriveFont((float) 18));

		JPanel hPanel = new JPanel();
		hPanel.setLayout(new GridLayout(1, 0, 20, 0));
		hPanel.setBorder(new EmptyBorder(10, 0, 30, 0));
		
		// Set scrollable panel
		JScrollPane hScrollView = new JScrollPane(hPanel,
				JScrollPane.VERTICAL_SCROLLBAR_NEVER,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		hScrollView.setBorder(null);
		
		for (Product prod : catalogProductsList) {
			// Limit products to category
			if (prod.getClass() != categoryCls)
				continue;
			
			// Limit gender for GenderConstrained products
			if (prod instanceof GenderConstrained) {
				GenderConstrained gc = (GenderConstrained) prod;
				
				if (!matchGenderConstrain(activePerson, gc.getGenderConstraint()))
					continue;
			}

			JButton prodBt = createProductButton(prod);
			
			hPanel.add(prodBt);
		}

		panel.add(titleLabel, BorderLayout.NORTH);
		panel.add(hScrollView, BorderLayout.CENTER);

		return panel;
	}
	
	private JButton createProductButton(Product prod) {
		// To access to this in the ActionListener inline class
		CatalogDialog that = this;
		
		JButton bt = new JButton(
				String.format(
						"<html><body style='text-align: center;'>"
						+ "<p style='padding-bottom: 5px;'>%s</p>"
						+ "<p>%s€</p>"
						+ "</body></html>",
						prod.getName(), prod.getPrice()));
		
		ImageIcon icon = prod.getIcon(ICON_SIZE);
		
		if (icon != null) {
			bt.setIcon(icon);
			bt.setHorizontalAlignment(JButton.CENTER);
			bt.setIconTextGap(20);
		}
		
		if (prod instanceof AgeLimited) {
			AgeLimited al = (AgeLimited) prod;
			
			if (al.getMinimumAge() > activePerson.getAge()) {
				bt.setEnabled(false);
				bt.setToolTipText("Vous êtes trop jeune pour acheter ce produit !");
			}
		}

		bt.setPreferredSize(new Dimension(175, 90));
		bt.setFocusable(false);
		
		// Set product button action
		bt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ProductInfoDialog infoDialog = new ProductInfoDialog(that, prod);
				
				// Set dialog Buy button action
				infoDialog.addBuyActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						infoDialog.closeDialog();
						buyProductAction(prod);
					}
				});
				
				infoDialog.showDialog();
			}
		});
		
		return bt;
	}
	
	private void buyProductAction(Product product) {
		if (activePerson.getMoney() < product.getPrice()) {
			JOptionPane.showMessageDialog(
					this,
					"Vous n'avez pas assez d'argent pour acheter ce produit.",
				    "Achat impossible",
				    JOptionPane.ERROR_MESSAGE);
		}
		else if (activePerson.isInventoryFull()) {
			JOptionPane.showMessageDialog(
					this,
					String.format(
							"<html>"
							+ "<p>Impossible d'ajouter un produit dans votre inventaire.</p>"
							+ "<p>Votre inventaire personnel est plein."
							+ "</html>"),
				    "Achat impossible",
				    JOptionPane.ERROR_MESSAGE);
		}
		else {
			activePerson.buyProduct(product);

			if (product instanceof Cloth) {
				JOptionPane.showMessageDialog(
						this,
						String.format(
								"<html>"
								+ "<p>Vous venez d'enfiler un(e) %s.</p>"
								+ "</html>",
								product.getName().toLowerCase()),
						"Achat effectué",
						JOptionPane.INFORMATION_MESSAGE);
			}
			else {
				JOptionPane.showMessageDialog(
						this,
						String.format(
								"<html>"
								+ "<p>Vous avez acheté un(e) %s.</p>"
								+ "<p>Il a été placé dans votre inventaire personnel."
								+ "</html>",
								product.getName().toLowerCase()),
						"Achat effectué",
						JOptionPane.INFORMATION_MESSAGE);
			}
		}
		
		updateRemainingAccount();
	}

	private void loadProductsList() {
		catalogProductsList = new ArrayList<Product>();
		
		BufferedReader buffer;
		
		try {
            // Get the Products csv file 
            FileReader file = new FileReader(ResourceLoader.getResourcePath(PRODUCTS_FILE_PATH));
            buffer = new BufferedReader(file);

            String line = null;
            int lineIndex = 0;
            
            while((line = buffer.readLine()) != null) {
            	lineIndex++;
            	
            	// Ignore comments and empty lines
            	if (line.startsWith("//") || line.trim().isEmpty())
            		continue;
            	
            	// The -1 is to prevent to remove trailing empty strings
            	String[] data = line.split("\t", -1);
            	
            	if (data.length != 13) {
                    buffer.close();
                    
            		throw new IllegalArgumentException(
            				String.format("Bad line in the Products.csv file (at line %d)",
            						lineIndex));
            	}
            	
            	Product prod = createProductFromData(data);
            	
            	if (prod != null) {
            		catalogProductsList.add(prod);
            	}
            }   

            buffer.close();
        }
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private Product createProductFromData(String[] data) {
		Product prod = null;
		
    	// Switch on the Product's type
    	switch (data[0]) {
		case "Food":
			prod = new Food(
					data[1],
					data[11],
					convertDataToInt(data[2]),
					data[12],
					convertDataToInt(data[5]),
					convertDataToInt(data[6]),
					convertDataToInt(data[7]));
			break;
		case "Toy":
			prod = new Toy(
					data[1],
					data[11],
					convertDataToInt(data[2]),
					data[12],
					convertDataToInt(data[5]),
					convertDataToInt(data[6]));
			break;
		case "Cloth":
			prod = new Cloth(
					data[1],
					data[11],
					convertDataToInt(data[2]),
					data[12],
					convertDataToInt(data[5]),
					convertDataToInt(data[10]),
					convertGenderConstrain(data[4]));
			break;
		case "Other":
			prod = new Other(
					data[1],
					data[11],
					convertDataToInt(data[2]),
					data[12],
					convertDataToInt(data[5]),
					convertDataToInt(data[6]),
					convertDataToInt(data[7]),
					convertDataToInt(data[8]),
					convertDataToInt(data[9]),
					convertDataToInt(data[10]),
					convertDataToInt(data[3]));
			break;
		default:
			break;
		}
    	
    	return prod;
	}
	
	private GenderConstraint convertGenderConstrain(String str) {
		GenderConstraint gc = GenderConstraint.Both;
		
		switch (str.toUpperCase()) {
		case "M":
			gc = GenderConstraint.Male;
			break;
		case "F":
			gc = GenderConstraint.Female;
			break;
		default:
			break;
		}
		
		return gc;
	}
	
	private boolean matchGenderConstrain(Person p, GenderConstraint c) {
		return !((c == GenderConstraint.Male && p.getGender() == Gender.Female) ||
				 (c == GenderConstraint.Female && p.getGender() == Gender.Male));
	}
	
	private int convertDataToInt(String str) {
		int val = 0;
		
		try {
			val = Integer.parseInt(str);
		} catch (Exception e) {
			val = 0;
		}
		
		return val;
	}
	
	public void showDialog() {
		pack();
		setLocationRelativeTo(getOwner());
		setVisible(true);
	}

	public void closeDialog() {
		setVisible(false);
	}


}

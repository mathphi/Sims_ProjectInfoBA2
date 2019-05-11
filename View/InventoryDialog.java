package View;

import Model.Game;
import Model.Person;
import Products.Product;

import java.awt.BorderLayout;
import java.awt.Color;
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

//TODO: add icons !!!
public class InventoryDialog extends JDialog {
	private static final long serialVersionUID = 4870573801345257186L;
	private static final int INVENTORY_SIZE = 5;

	private ArrayList<JButton> itemButtons = new ArrayList<JButton>();
	private ArrayList<Product> productsList = new ArrayList<Product>();
	
	private JButton useButton;
	private JButton catalogButton;
	private JButton closeButton;
	
	public InventoryDialog(Frame parent, Person person, Game game) {
		super(parent, "Inventaire", true);		
		setPreferredSize(new Dimension(600, 250));
		setUndecorated(true);

		JPanel mainPanel = new JPanel();

		mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		mainPanel.setLayout(new BorderLayout(0, 20));

		JPanel listPanel = new JPanel();
		listPanel.setLayout(new GridLayout(1, 0, 10, 00));

		JLabel titleLabel = new JLabel("Inventaire de " + person.getName(), JLabel.CENTER);
		titleLabel.setFont(titleLabel.getFont().deriveFont((float) 20));
		
		productsList = person.getInventory();

		for (int i = 0 ; i < INVENTORY_SIZE ; i++) {
			Product prod = null;

			if (i < productsList.size()) {
				prod = productsList.get(i);
			}
			
			JButton item = createItem(prod);
			
			itemButtons.add(item);
			listPanel.add(item);
		}
		
		JPanel buttonsContainer = new JPanel();
		buttonsContainer.setLayout(new GridLayout(1, 0, 20, 0));
		buttonsContainer.setBorder(new EmptyBorder(10, 10, 10, 10));

		useButton = new JButton("Utiliser");
		catalogButton = new JButton("Catalogue");
		closeButton = new JButton("Fermer");

		useButton.setToolTipText("Utiliser le produit sélectionné");
		catalogButton.setToolTipText("Acheter de nouveaux produits pour l'inventaire");
		closeButton.setToolTipText("Fermer l'inventaire");
		
		buttonsContainer.add(useButton);
		buttonsContainer.add(catalogButton);
		buttonsContainer.add(closeButton);

		mainPanel.add(titleLabel, BorderLayout.NORTH);
		mainPanel.add(listPanel, BorderLayout.CENTER);
		mainPanel.add(buttonsContainer, BorderLayout.SOUTH);
		
		add(mainPanel);
		
		checkUseButtonState();

		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeDialog();
			}
		});
		
		useButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Product selectedProd = getSelectedProduct();
				
				if (selectedProd != null) {
					closeDialog();
					person.useInventory(selectedProd);
				}
			}
		});
		
		catalogButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				closeDialog();
				game.openCatalogDialog();
			}
		});
	}
	
	private JButton createItem(Product prod) {
		String btName = "Vide";
		
		if (prod != null)
			btName = prod.getName();
		
		JButton bt = new JButton(
				String.format(
					"<html><body style='text-align: center;'>"
					+ "<p>%s</p>"
					+ "</body></html>",
					btName));
		
		bt.setFocusable(false);
		bt.setEnabled(prod != null);
		
		bt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				toggleSelectedButton(bt);
			}
		});
		
		return bt;
	}
	
	private void toggleSelectedButton(JButton bt) {
		boolean selected = bt.isSelected();
		
		unselectAll();
		
		bt.setSelected(!selected);
		bt.setBackground(bt.isSelected() ? new Color(100, 200, 255) : null);
		
		checkUseButtonState();
	}
	
	private void checkUseButtonState() {
		useButton.setEnabled(getSelectedProduct() != null);
		useButton.setBackground(useButton.isEnabled() ? null : Color.LIGHT_GRAY);
	}
	
	private void unselectAll() {
		for (JButton bt : itemButtons) {
			bt.setSelected(false);
			bt.setBackground(null);
		}
	}
	
	private Product getSelectedProduct() {
		for (int i = 0 ; i < itemButtons.size() && i < productsList.size() ; i++) {
			JButton bt = itemButtons.get(i);
			Product prod = productsList.get(i);
			
			if (bt.isSelected()) {
				return prod;
			}
		}
		
		return null;
	}

	public void showDialog() {
		pack();
		setLocationRelativeTo(getOwner());
		setVisible(true);
	}

	private void closeDialog() {
		setVisible(false);
	}
}

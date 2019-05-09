package View;

import Products.Product;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class ProductInfoDialog extends JDialog {
	private static final long serialVersionUID = 4870573801345257186L;
	
	private JButton buyButton;
	private JButton closeButton;

	public ProductInfoDialog(JDialog parent, Product product) {
		super(parent, "Fiche produit", true);

		setPreferredSize(new Dimension(400, 200));
		setLayout(new BorderLayout());
		setUndecorated(true);

		JLabel titleLabel = new JLabel(product.getName(), JLabel.CENTER);
		titleLabel.setBorder(new EmptyBorder(10, 0, 10, 0));
		titleLabel.setFont(titleLabel.getFont().deriveFont((float) 20));
		
		JPanel mainPanel = new JPanel();
		mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		mainPanel.setLayout(new BorderLayout());
		
		JLabel iconLabel = new JLabel("ICONE", JLabel.CENTER);
		iconLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		JLabel infosLabel = new JLabel(
				String.format(
						"<html>"
						+ "<p style='padding-bottom: 10px;'><b>Coût :</b> %d€</p>"
						+ "<p><b>Description :</b> %s</p>"
						+ "</html>",
				product.getPrice(),
				product.getDescription()), JLabel.CENTER);
		infosLabel.setFont(infosLabel.getFont().deriveFont(Font.PLAIN));
		
		mainPanel.add(iconLabel, BorderLayout.LINE_START);
		mainPanel.add(infosLabel, BorderLayout.CENTER);

		JPanel buttonsContainer = new JPanel();
		buttonsContainer.setLayout(new GridLayout(1, 0, 20, 0));
		buttonsContainer.setBorder(new EmptyBorder(10, 70, 20, 70));

		buyButton = new JButton("Acheter");
		closeButton = new JButton("Fermer");
		
		buttonsContainer.add(buyButton);
		buttonsContainer.add(closeButton);

		add(titleLabel, BorderLayout.NORTH);
		add(mainPanel, BorderLayout.CENTER);
		add(buttonsContainer, BorderLayout.SOUTH);
		
		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeDialog();
			}
		});
	}
	
	public void addBuyActionListener(ActionListener a) {
		buyButton.addActionListener(a);
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

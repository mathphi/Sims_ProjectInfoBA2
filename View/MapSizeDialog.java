package View;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;

import Tools.Size;

public class MapSizeDialog extends JDialog {
	private static final long serialVersionUID = -6014304203322845327L;

	private JSpinner widthField;
	private JSpinner heightField;

	public MapSizeDialog(Frame parent, Size actualSize) {
		super(parent, "Taille de la carte", true);
		
		setPreferredSize(new Dimension(250, 150));
		setLayout(new BorderLayout());

		JPanel mainPanel = new JPanel();
		
		mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		mainPanel.setLayout(new GridLayout(0, 1, 0, 10));

		JLabel label = new JLabel("Taille de la carte");

		SpinnerNumberModel widthSpinnerModel = 
				new SpinnerNumberModel(actualSize.getWidth(), 20, 200, 5);
		widthField = new JSpinner(widthSpinnerModel);
		SpinnerNumberModel heightSpinnerModel = 
				new SpinnerNumberModel(actualSize.getHeight(), 20, 200, 5);
		heightField = new JSpinner(heightSpinnerModel);

		JLabel labelSpacer = new JLabel(" X ");
		labelSpacer.setHorizontalAlignment(JLabel.CENTER);
		
		JPanel fieldsPanel = new JPanel();
		fieldsPanel.setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.5;
		c.weighty = 0.5;

		c.gridx = 0;
		c.gridy = 0;
		fieldsPanel.add(Box.createHorizontalStrut(20), c);

		c.gridx = 1;
		c.gridy = 0;
		fieldsPanel.add(widthField, c);

		c.gridx = 2;
		c.gridy = 0;
		fieldsPanel.add(labelSpacer, c);

		c.gridx = 3;
		c.gridy = 0;
		fieldsPanel.add(heightField, c);

		c.gridx = 4;
		c.gridy = 0;
		fieldsPanel.add(Box.createHorizontalStrut(20), c);
		
		mainPanel.add(label);
		mainPanel.add(fieldsPanel);
		
		JPanel buttonsContainer = new JPanel();
		buttonsContainer.setLayout(new GridLayout(1, 0, 10, 0));
		buttonsContainer.setBorder(new EmptyBorder(10, 10, 10, 10));

		JButton btnOk = new JButton("Ok");
		
		buttonsContainer.add(Box.createHorizontalStrut(20));
		buttonsContainer.add(btnOk);
		buttonsContainer.add(Box.createHorizontalStrut(20));
		
		add(mainPanel, BorderLayout.CENTER);
		add(buttonsContainer, BorderLayout.SOUTH);

		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeForm();
			}
		});
	}
	
	public void showForm() {
		pack();
		setLocationRelativeTo(getOwner());
		setVisible(true);
	}
	
	public void closeForm() {
		setVisible(false);
	}

	public Size getMapSize() {
		return new Size((int) widthField.getValue(), (int) heightField.getValue());
	}
}

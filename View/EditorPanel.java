package View;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class EditorPanel extends JPanel {
	private static final long serialVersionUID = -1396817768662844134L;
	
	private ArrayList<ActionListener> actionListeners = new ArrayList<ActionListener>();
	
	public EditorPanel() {
		setLayout(new BorderLayout());

		JPanel mainPanel = new JPanel();

		mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

		JPanel mapCateg = createCategory(mainPanel, "Éléments de terrain");

		// Event id 0 is for « add ground object to map » action
		addButtonToCateg(mapCateg, "Herbe 1", "Model.GroundGrass\t1", 0);
		addButtonToCateg(mapCateg, "Herbe 2", "Model.GroundGrass\t2", 0);
		addButtonToCateg(mapCateg, "Herbe 5", "Model.GroundGrass\t5", 0);
		addButtonToCateg(mapCateg, "Parquet 1", "Model.GroundFlooring\t1", 0);
		addButtonToCateg(mapCateg, "Parquet 2", "Model.GroundFlooring\t2", 0);
		addButtonToCateg(mapCateg, "Parquet 5", "Model.GroundFlooring\t5", 0);
		addButtonToCateg(mapCateg, "Mur 1", "Model.WallBlock\t1", 0);
		addButtonToCateg(mapCateg, "Mur 2", "Model.WallBlock\t2", 0);
		addButtonToCateg(mapCateg, "Mur 5", "Model.WallBlock\t5", 0);

		JPanel mobCateg = createCategory(mainPanel, "Éléments du mobilier");

		// Event id 1 is for « add furniture object to map » action
		addButtonToCateg(mobCateg, "WC", "Model.WaterClosed", 1);
		addButtonToCateg(mobCateg, "Lit", "Model.Bed", 1);
		addButtonToCateg(mobCateg, "Fauteuil", "Model.Sofa", 1);
		addButtonToCateg(mobCateg, "Ordinateur", "Model.Computer", 1);

		JPanel persCateg = createCategory(mainPanel, "Personnages");

		// Event id 2 is for « add person to map » action
		addButtonToCateg(persCateg, "Enfant", "Model.Kid", 2);
		addButtonToCateg(persCateg, "Ado", "Model.Teenager", 2);
		addButtonToCateg(persCateg, "Adulte", "Model.Adult", 2);

		mainPanel.add(Box.createVerticalStrut(2000));

		add(mainPanel, BorderLayout.CENTER);
		
		JPanel infoPanel = new JPanel();

		infoPanel.setBorder(new EmptyBorder(10, 10, 30, 10));
		infoPanel.setLayout(new BorderLayout());
		
		JLabel infoLabel = new JLabel(
				"<html>" +
				"<b>Clic gauche :</b> Placer l'objet<br>" +
				"<b>Clic droit :</b> Supprimer l'objet<br>" +
				"<b>Bouton R :</b> Tourner l'objet<br>" + 
				"<b>Flèches :</b> Naviguer sur la carte" +
				"</html>"
				);
		infoLabel.setFont(infoLabel.getFont().deriveFont(Font.PLAIN));
		
		infoPanel.add(infoLabel, BorderLayout.CENTER);
		
		add(infoPanel, BorderLayout.SOUTH);
	}
	
	private JPanel createCategory(JPanel containerPanel, String title) {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0, 1, 0, 0));
		
		JLabel categLabel = new JLabel(title);
		categLabel.setBorder(new EmptyBorder(20, 0, 10, 0));
		categLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel.add(categLabel);
		
		JPanel categButtons = new JPanel();
		categButtons.setLayout(new GridLayout(0, 3, 10, 10));
		panel.add(categButtons);

		containerPanel.add(panel);
		
		return categButtons;
	}
	
	private void addButtonToCateg(JPanel categ, String text, String className, int id) {
		EditorPanel that = this;
		JButton btn = new JButton(text);
		
		// Avoid the bold text for the buttons
		btn.setFont(btn.getFont().deriveFont(Font.PLAIN));
		
    	// Disable focus to avoid the loss of focus for map, resulting in KeyListener not working
		btn.setFocusable(false);
		
		btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ActionEvent event = new ActionEvent(that, id, className);
				
				for (ActionListener a : actionListeners) {
					a.actionPerformed(event);
				}
			}
		});
		
		categ.add(btn);
	}
	
	public void addPlacementActionListener(ActionListener a) {
		actionListeners.add(a);
	}
}

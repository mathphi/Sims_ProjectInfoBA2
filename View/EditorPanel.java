package View;

import java.awt.BorderLayout;
import java.awt.Component;
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

		addButtonToCateg(mapCateg, "Mur", "Model.WallBlock", 1);
		addButtonToCateg(mapCateg, "Mur", "Model.WallBlock", 1);
		addButtonToCateg(mapCateg, "Mur", "Model.WallBlock", 1);
		addButtonToCateg(mapCateg, "Mur", "Model.WallBlock", 1);
		addButtonToCateg(mapCateg, "Mur", "Model.WallBlock", 1);

		JPanel mobCateg = createCategory(mainPanel, "Mobilier");

		addButtonToCateg(mobCateg, "WC", "Model.WaterClosed", 1);
		addButtonToCateg(mobCateg, "WC", "Model.WaterClosed", 1);
		addButtonToCateg(mobCateg, "WC", "Model.WaterClosed", 1);

		JPanel persCateg = createCategory(mainPanel, "Personnages");

		addButtonToCateg(persCateg, "Enfant", "Model.Kid", 2);
		addButtonToCateg(persCateg, "Ado", "Model.Teenager", 2);
		addButtonToCateg(persCateg, "Adulte", "Model.Adult", 2);

		mainPanel.add(Box.createVerticalStrut(2000));
		
		add(mainPanel, BorderLayout.CENTER);
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
		
    	// Disable focus to avoid the loss of focus for map, resulting in KeyListener not working
		btn.setFocusable(false);
		
		btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Event id 1 is for « add object to map » action
				// Event id 2 is for « add person to map » action
				
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

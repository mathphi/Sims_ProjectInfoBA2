package View;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import Model.Person;

public class Status extends JPanel {
	private static final long serialVersionUID = -1155139786520862134L;
	
	private final int BAR_LENGTH = 80;
	private final int BAR_HEIGHT = 15;
	private final int AVATAR_SIZE = 100;
		
	private Person p;
	private String gameTimeStr = "";

    public Status() {
        this.setPreferredSize(new Dimension(300, 320));
        //this.setBackground(Color.LIGHT_GRAY);
        this.setOpaque(true);
    }
    
	public void paint(Graphics g) {
		super.paintComponent(g);
        
        // Draw game time
        g.setColor(Color.BLACK);
        g.drawString(gameTimeStr, 20, 25);

        // Don't paint if there is not active player yet
        if (p == null)
        	return;
        
		// Draw avatar
        g.setColor(Color.BLUE);
        g.fillRect(20, 50, AVATAR_SIZE, AVATAR_SIZE);
        
		// Draw person infos
        g.setColor(Color.BLACK);
        g.drawString(p.getName(), 130, 70);
        g.drawString((p.getGender() == Person.Gender.Male) ? "Homme" : "Femme", 130, 100);
        g.drawString(p.getAge() + " ans", 130, 100);

		// Draw bars
        paintLevelBar(g, 20, 200, BAR_LENGTH*2 + 30, BAR_HEIGHT, "Énergie", p.getEnergy());
        paintLevelBar(g, 20, 240, BAR_LENGTH, BAR_HEIGHT, "Humeur",  p.getMood());
        paintLevelBar(g, 130, 240, BAR_LENGTH, BAR_HEIGHT, "Faim", 	 p.getHunger());
        paintLevelBar(g, 20, 280, BAR_LENGTH, BAR_HEIGHT, "Hygiène", p.getHygiene());
        paintLevelBar(g, 130, 280, BAR_LENGTH, BAR_HEIGHT, "Vessie",  p.getBladder());
    }
	
	private void paintLevelBar(Graphics g, int posX, int posY, int length, int height, String title, double val) {
		g.setColor(Color.BLACK);
        g.drawString(title, posX, posY);
        g.setColor(Color.RED);
        g.fillRect(posX, posY + 5, length, height);
        g.setColor(Color.GREEN);
        int length_ok = (int) Math.round(length * val);
        g.fillRect(posX, posY + 5, length_ok, height);
	}

    public void redraw() {
        this.repaint();
    }

	public void setActivePerson(Person p2) {
		this.p = p2;
	}
	
	public void setGameTimeStr(String t_str) {
		gameTimeStr = t_str;
	}
}

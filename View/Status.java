package View;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import Model.Person;
import Tools.Point;
import Tools.Size;

public class Status extends JPanel {
	private Person p;
	private int BAR_LENGTH = 80;
	private int BAR_HEIGHT = 15;
	private int AVATAR_SIZE = 100;
	
	private String gameTimeStr = "";

    public Status() {
        this.setPreferredSize(new Dimension(450, 600));
        this.setBackground(Color.LIGHT_GRAY);
        this.setOpaque(true);
    }
    
	public void paint(Graphics g) {
		super.paintComponent(g);
        
        // Draw game time
        g.setColor(Color.BLACK);
        g.drawString(gameTimeStr, 10, 20);
        
		// Draw avatar
        g.setColor(Color.BLUE);
        g.fillRect(10, 40, AVATAR_SIZE, AVATAR_SIZE);
        
		// Draw person infos
        g.setColor(Color.BLACK);
        g.drawString(p.getFirstName(), 120, 60);
        g.drawString(p.getLastName(), 120, 80);
        g.drawString((p.getGender() == Person.Gender.Male) ? "Homme" : "Femme", 120, 110);

		// Draw bars
        paintLevelBar(g, 10, 200, BAR_LENGTH, BAR_HEIGHT, "Énergie", p.getEnergy());
        paintLevelBar(g, 10, 240, BAR_LENGTH, BAR_HEIGHT, "Humeur",  p.getMood());
        paintLevelBar(g, 10, 280, BAR_LENGTH, BAR_HEIGHT, "Faim", 	 p.getHunger());
        paintLevelBar(g, 10, 320, BAR_LENGTH, BAR_HEIGHT, "Hygiène", p.getHygiene());
        paintLevelBar(g, 10, 360, BAR_LENGTH, BAR_HEIGHT, "Vessie",  p.getBladder());
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

package View;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import Model.Person;

public class Status extends JPanel {
	private Person p;
	private int BAR_LENGTH = 80;
	private int BAR_WIDTH = 15;
	private int AVATAR_SIZE = 100;

    public Status() {
        this.setPreferredSize(new Dimension(450, 600));
        this.setBackground(Color.LIGHT_GRAY);
        this.setOpaque(true);
    }
    
	public void paint(Graphics g) {
		super.paintComponent(g);
		// draw avatar
        g.setColor(Color.BLUE);
        g.fillRect(150, 25, AVATAR_SIZE, AVATAR_SIZE);

		// bars 
        // Energy 
        g.setColor(Color.BLACK);
        g.drawString("Energy", 10, 195);
        g.setColor(Color.RED);
        g.fillRect(10, 200, BAR_LENGTH, BAR_WIDTH);
        g.setColor(Color.GREEN);
        int length_ok = (int) Math.round(BAR_LENGTH*p.getEnergy());
        g.fillRect(10, 200, length_ok, BAR_WIDTH);
    }

    public void redraw() {
        this.repaint();
    }

	public void setPlayer(Person p2) {
		this.p = p2;
	}
}

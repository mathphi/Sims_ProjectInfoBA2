package View;

import Model.GameObject;
import Tools.Size;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JPanel;

import Controller.MouseController;

public class Map extends JPanel {
	private static final long serialVersionUID = 1282569928891942835L;
	
	private ArrayList<GameObject> objects = new ArrayList<GameObject>();
    private final Size MAP_SIZE = new Size(80, 80);
    private final int BLOC_SIZE = 20;

    public Map() {
        this.setFocusable(true);
        this.requestFocusInWindow();
        this.setPreferredSize(new Dimension(MAP_SIZE.getWidth()*BLOC_SIZE, MAP_SIZE.getHeight()*BLOC_SIZE));
    }

    public void paint(Graphics g) {
		super.paintComponent(g);
		
        for (int i = 0; i < MAP_SIZE.getWidth(); i++) { 
            for (int j = 0; j < MAP_SIZE.getHeight(); j++) {
                int x = i;
                int y = j;
                g.setColor(Color.LIGHT_GRAY);
                g.fillRect(x * BLOC_SIZE, y * BLOC_SIZE, BLOC_SIZE - 2, BLOC_SIZE - 2);
                g.setColor(Color.BLACK);
                g.drawRect(x * BLOC_SIZE, y * BLOC_SIZE, BLOC_SIZE - 2, BLOC_SIZE - 2);
            }
        }

        for (GameObject object : this.objects) {
            object.paint(g, BLOC_SIZE);
        }
    }

    public void setObjects(ArrayList<GameObject> objects) {
        this.objects = objects;
    }

    public void redraw() {
        this.repaint();
    }

	public void addMouse(MouseController m) {
		addMouseListener((MouseListener) m);
		addMouseMotionListener((MouseMotionListener) m);
	}
	
	public Size getMapSize() {
		return MAP_SIZE;
	}
	
	public Size getBlockSize() {
		return new Size(BLOC_SIZE, BLOC_SIZE);
	}
}

package View;

import Model.GameObject;
import Model.GroundObject;
import Tools.Point;
import Tools.Rect;
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
    private final Size MAP_SIZE = new Size(80, 80); //TODO: this may be a setting in editor !!!
    private final int BLOC_SIZE = 20;
    
    private boolean isGridVisible = false;
    private Point viewOffset = new Point(0, 0);

    public Map() {
        this.setFocusable(true);
        this.requestFocusInWindow();
        this.setPreferredSize(new Dimension(MAP_SIZE.getWidth()*BLOC_SIZE, MAP_SIZE.getHeight()*BLOC_SIZE));
    }
    
    public Rect getRect() {
    	return new Rect(0, 0, (int)getSize().getWidth(), (int)getSize().getHeight());
    }
    
    public void resetViewOffset() {
    	viewOffset = new Point(0, 0);
    }
    
    public void scrollToObject(GameObject o) {
    	scrollToPos(o.getPos());
    }
    
    public void scrollToPos(Point pos) {
    	Point offset = pos
    			.multiply(BLOC_SIZE)
    			.add(-this.getVisibleRect().getWidth() / 2.0,
    				 -this.getVisibleRect().getHeight() / 2.0);

    	setViewOffset(offset);
    }
    
    public void moveView(Point dp) {
    	setViewOffset(viewOffset.add(dp.multiply(BLOC_SIZE)));
    }
    
    private void setViewOffset(Point offset) {
    	// Don't exceed the map's view with the offset
    	double maxX = MAP_SIZE.getWidth() * BLOC_SIZE - this.getVisibleRect().getWidth();
    	double maxY = MAP_SIZE.getHeight() * BLOC_SIZE - this.getVisibleRect().getHeight();

    	maxX = (maxX < 0 ? 0 : maxX);
    	maxY = (maxY < 0 ? 0 : maxY);
    	
    	offset = new Point(
    			(offset.getX() < 0 ? 0 : offset.getX()),
    			(offset.getY() < 0 ? 0 : offset.getY()));
    	
    	offset = new Point(
    			(offset.getX() > maxX ? maxX : offset.getX()),
    			(offset.getY() > maxY ? maxY : offset.getY()));
    	
    	viewOffset = offset;
    }
    
    public Point getViewOffset() {
    	return viewOffset;
    }
    
    public void setGridVisible(boolean visible) {
    	isGridVisible = visible;
    }
    
    public void paintGrid(Graphics g) {
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
    }

    public void paint(Graphics g) {
    	g.translate(-viewOffset.getXInt(), -viewOffset.getYInt());
    	
		super.paintComponent(g);
		
		if (isGridVisible) {
			paintGrid(g);
		}

        // Separate GroundObject from others to avoid to paint ground objects over the other objects
        ArrayList<GameObject> groundObjects = new ArrayList<GameObject>();
        ArrayList<GameObject> otherObjects = new ArrayList<GameObject>();

        // Filter GoundObject and others
        for (GameObject object : this.objects) {
        	if (object instanceof GroundObject) {
        		groundObjects.add(object);
        	}
        	else {
        		otherObjects.add(object);
        	}
        }

        // Paint GoundObject before other (paint them under the others)
        ArrayList<GameObject> ordered = new ArrayList<GameObject>();
        ordered.addAll(groundObjects);
        ordered.addAll(otherObjects);
        
        for (GameObject o : ordered) {
        	o.paint(g, BLOC_SIZE);
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

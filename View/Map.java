package View;

import Model.GameObject;
import Model.GroundTile;
import Model.Person;
import Model.Vegetation;
import Tools.Point;
import Tools.Rect;
import Tools.Size;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JPanel;

import Controller.MouseController;

public class Map extends JPanel {
	private static final long serialVersionUID = 1282569928891942835L;
	
	private ArrayList<GameObject> objects = new ArrayList<GameObject>();
	private final Size MINIMAP_SIZE = new Size(250, 250);
	private final int MINIMAP_MARGIN = 5;
    private final int BLOC_SIZE = 20;
    
    private Size mapSize = new Size(80, 80);
    
    private boolean isGridVisible = false;
    private Point viewOffset = new Point(0, 0);

    public Map() {
        this.setFocusable(true);
        this.requestFocusInWindow();
        
        // To receive key Tab events
        this.setFocusTraversalKeysEnabled(false);
    }
    
    public void setMapSize(Size sz) {
    	mapSize = sz;
    }
    
    public Rect getViewRect() {
    	return new Rect(0, 0, (int)getSize().getWidth(), (int)getSize().getHeight());
    }
    
    public void resetViewOffset() {
    	viewOffset = new Point(0, 0);
    }
    
    public void centerViewToObject(GameObject o) {
    	centerViewToPos(o.getPos());
    }
    
    public void centerViewToPos(Point pos) {
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
    	double maxX = mapSize.getWidth() * BLOC_SIZE - this.getVisibleRect().getWidth();
    	double maxY = mapSize.getHeight() * BLOC_SIZE - this.getVisibleRect().getHeight();

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

    public Point getMinimapOffset() {
    	// Position for the minimap
    	double minimapOffsetX = MINIMAP_MARGIN;
    	double minimapOffsetY = getVisibleRect().getHeight() - MINIMAP_SIZE.getHeight() - MINIMAP_MARGIN;
    	
    	return new Point(minimapOffsetX, minimapOffsetY);
    }
    
    public Rect getMinimapRect() {
    	Point pos = getMinimapOffset().add(new Point(-MINIMAP_MARGIN,-MINIMAP_MARGIN));
    	
    	return new Rect(pos, MINIMAP_SIZE.add(2*MINIMAP_MARGIN, 2*MINIMAP_MARGIN));
    }
    
    public double getMinimapScale() {
		// Get map total size
		int w = getMapSize().getWidth() * getBlockSize().getWidth();
		int h = getMapSize().getHeight() * getBlockSize().getHeight();
		
		// Compute scale
    	double hscale = (double) MINIMAP_SIZE.getWidth() / w;
    	double vscale = (double) MINIMAP_SIZE.getHeight() / h;

    	// Use the same scale h&v to keep aspect ratio
    	double scale = Math.min(hscale, vscale);

    	return scale;
    }
    
    public void setGridVisible(boolean visible) {
    	isGridVisible = visible;
    }
    
    public void paintGrid(Graphics g) {
        for (int i = 0; i < mapSize.getWidth(); i++) { 
            for (int j = 0; j < mapSize.getHeight(); j++) {
                int x = i;
                int y = j;
                g.setColor(Color.LIGHT_GRAY);
                g.fillRect(x * BLOC_SIZE, y * BLOC_SIZE, BLOC_SIZE - 2, BLOC_SIZE - 2);
                g.setColor(Color.BLACK);
                g.drawRect(x * BLOC_SIZE, y * BLOC_SIZE, BLOC_SIZE - 2, BLOC_SIZE - 2);
            }
        }
    }
    
    public void generateMap(Graphics g) {
        // Separate GroundObject from others to avoid to paint ground objects over the other objects
        ArrayList<GameObject> groundObjects = new ArrayList<GameObject>();
        ArrayList<GameObject> personObjects = new ArrayList<GameObject>();
        ArrayList<GameObject> vegetationObjects = new ArrayList<GameObject>();
        ArrayList<GameObject> otherObjects = new ArrayList<GameObject>();

        // Filter GoundObject and others
        for (GameObject object : this.objects) {
        	if (object instanceof GroundTile) {
        		groundObjects.add(object);
        	}
        	else if (object instanceof Person) {
        		personObjects.add(object);
        	}
        	else if (object instanceof Vegetation) {
        		vegetationObjects.add(object);
        	}
        	else {
        		otherObjects.add(object);
        	}
        }

        // Paint GoundObject before other (paint them under the others)
        ArrayList<GameObject> ordered = new ArrayList<GameObject>();
        ordered.addAll(groundObjects);
        ordered.addAll(otherObjects);
        ordered.addAll(personObjects);
        ordered.addAll(vegetationObjects);		

        for (GameObject o : ordered) {
        	o.paint(g, BLOC_SIZE);
        }
    }

    public void paint(Graphics g) {
    	Graphics2D g2d = (Graphics2D) g;
    	
    	super.paintComponent(g2d);
    	
    	// Translate the map to simulate a scrolling
    	g2d.translate(-viewOffset.getXInt(), -viewOffset.getYInt());
    	
    	// Paint the grid if needed (for editor)
		if (isGridVisible) {
			paintGrid(g);
		}
		
    	// Paint the map at full size
		generateMap(g2d);

		double scale = getMinimapScale();
		
    	// Position for the minimap
    	double minimapOffsetX = getMinimapOffset().getX();
    	double minimapOffsetY = getMinimapOffset().getY();
    	
    	Rect rm = getMinimapRect();

    	g2d.translate(viewOffset.getXInt(), viewOffset.getYInt());
    	
    	Stroke oldStroke = g2d.getStroke();
    	g2d.setStroke(new BasicStroke(2));
    	
    	// Paint the minimap rectangle
    	g2d.setColor(Color.LIGHT_GRAY);
    	g2d.fillRect((int)rm.getX(), (int)rm.getY(), rm.getWidth(), rm.getHeight());
    	g2d.setColor(Color.BLACK);
    	g2d.drawRect((int)rm.getX(), (int)rm.getY(), rm.getWidth(), rm.getHeight());
    	
    	g2d.setStroke(oldStroke);

    	// Paint the minimap
    	g2d.translate(minimapOffsetX, minimapOffsetY);
    	g2d.scale(scale, scale);
		generateMap(g2d);
		
		// Paint the view rectangle
		Point vo = getViewOffset();
		Size vs = new Size((int)getVisibleRect().getWidth(), (int)getVisibleRect().getHeight());
		vs.setWidth(Math.min(vs.getWidth(), mapSize.getWidth() * BLOC_SIZE));
		vs.setHeight(Math.min(vs.getHeight(), mapSize.getWidth() * BLOC_SIZE));
    	g2d.setColor(Color.BLUE);
    	g2d.drawRect((int)vo.getX(), (int)vo.getY(), vs.getWidth(), vs.getHeight());
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
		return mapSize;
	}
	
	public Size getBlockSize() {
		return new Size(BLOC_SIZE, BLOC_SIZE);
	}
}

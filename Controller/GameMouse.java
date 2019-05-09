package Controller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import Model.Game;

import Tools.Point;
import Tools.Size;

public class GameMouse extends MouseController implements MouseListener, MouseMotionListener {
    private Game game;

    public GameMouse(Game game) {
        this.game = game;
    }
    private Point getMapEventPos(MouseEvent e) {
    	Size blocSize = game.getMapBlockSize();
    	Point offset = game.getMapViewOffset();
    	
    	return new Point(
    			(int)((e.getX() + offset.getX()) / blocSize.getWidth()), 
    			(int)((e.getY() + offset.getY()) / blocSize.getHeight()));
    }
    
    public Point getMinimapEventPos(MouseEvent e) {
    	Size blocSize = game.getMapBlockSize();
    	Point delta = game.getMinimapOffset();
    	double scale = game.getMinimapScale();
    	
    	return new Point(
    			(int)((e.getX() - delta.getX()) / scale / blocSize.getWidth()),
    			(int)((e.getY() - delta.getY()) / scale / blocSize.getHeight()));
    }

	@Override
	public void mousePressed(MouseEvent e) {
		synchronized(game) {
			if (!game.isRunning())
				return;
			
			// If the click is done on the minimap -> get the pos on the real map
			if (game.getMinimapRect().contains(e.getX(), e.getY())) {
				// Left click
				if (e.getButton() == MouseEvent.BUTTON1) {
					game.mouseLeftClickEvent(getMinimapEventPos(e));
				}
				
				return;
			}
			
			// Left click
			if (e.getButton() == MouseEvent.BUTTON1) {
				game.mouseLeftClickEvent(getMapEventPos(e));
			}
			// Right click
			else if (e.getButton() == MouseEvent.BUTTON3) {
				game.mouseRightClickEvent(getMapEventPos(e));
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mouseDragged(MouseEvent e) {}

	@Override
	public void mouseMoved(MouseEvent e) {}
}

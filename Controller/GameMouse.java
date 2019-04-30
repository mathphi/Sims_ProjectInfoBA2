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
    	return new Point(
    			e.getX() / blocSize.getWidth(), 
    			e.getY() / blocSize.getHeight());
    }

	@Override
	public void mousePressed(MouseEvent e) {
		synchronized(game) {
			if (!game.isRunning())
				return;
			
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

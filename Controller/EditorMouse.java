package Controller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import Model.Editor;

import Tools.Point;
import Tools.Size;

public class EditorMouse extends MouseController implements MouseListener, MouseMotionListener {
    private Editor editor;

    public EditorMouse(Editor editor) {
        this.editor = editor;
    }
    
    private Point getMapEventPos(MouseEvent e) {
    	Size blocSize = editor.getMapBlockSize();
    	return new Point(
    			e.getX() / blocSize.getWidth(), 
    			e.getY() / blocSize.getHeight());
    }

	@Override
	public void mousePressed(MouseEvent e) {
		synchronized(editor) {
			if (!editor.isActive())
				return;
			
			// Left click
			if (e.getButton() == MouseEvent.BUTTON1) {
				editor.mouseLeftClickEvent(getMapEventPos(e));
			}
			// Right click
			else if (e.getButton() == MouseEvent.BUTTON3) {
				editor.mouseRightClickEvent(getMapEventPos(e));
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
	public void mouseMoved(MouseEvent e) {
		synchronized(editor) {
			if (!editor.isActive())
				return;
			
			editor.mouseMoveEvent(getMapEventPos(e));
		}
	}
}

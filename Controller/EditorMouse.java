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
    	Point offset = editor.getMapViewOffset();
    	return new Point(
    			(int)((e.getX() + offset.getX()) / blocSize.getWidth()), 
    			(int)((e.getY() + offset.getY()) / blocSize.getHeight()));
    }
    
	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseClicked(MouseEvent e) {
		synchronized(editor) {
			if (!editor.isActive())
				return;

			// We don't care of any mouseEvent from the minimap
			if (editor.getMinimapRect().contains(e.getX(), e.getY()))
				return;
			
			// Simple click
			if (e.getClickCount() == 1) {
				// Left click
				if (e.getButton() == MouseEvent.BUTTON1) {
					editor.mouseLeftClickEvent(getMapEventPos(e));
				}
				// Right click
				else if (e.getButton() == MouseEvent.BUTTON3) {
					editor.mouseRightClickEvent(getMapEventPos(e));
				}
			}
			else {
				// Left click
				if (e.getButton() == MouseEvent.BUTTON1) {
					editor.mouseLeftDoubleClickEvent(getMapEventPos(e));
				}
				// Right click
				else if (e.getButton() == MouseEvent.BUTTON3) {
					editor.mouseRightDoubleClickEvent(getMapEventPos(e));
				}
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {
		synchronized(editor) {
			if (!editor.isActive())
				return;
			
			// We don't care of any mouseEvent from the minimap
			if (editor.getMinimapRect().contains(e.getX(), e.getY()))
				return;
			
			editor.mouseExitedEvent();
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {}

	@Override
	public void mouseMoved(MouseEvent e) {
		synchronized(editor) {
			if (!editor.isActive())
				return;

			// We don't care of any mouseEvent from the minimap
			if (editor.getMinimapRect().contains(e.getX(), e.getY()))
				return;
			
			editor.mouseMoveEvent(getMapEventPos(e));
		}
	}
}

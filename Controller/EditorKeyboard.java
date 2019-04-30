package Controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import Model.Editor;

public class EditorKeyboard implements KeyListener {
	private Editor editor;

	public EditorKeyboard(Editor editor) {
		this.editor = editor;
	}

	@Override
	public void keyPressed(KeyEvent event) {
		if (!editor.isActive())
			return;
		
		int key = event.getKeyCode();

		switch (key) {
		case KeyEvent.VK_RIGHT:
			editor.moveView(1, 0);
			break;
		case KeyEvent.VK_LEFT:
			editor.moveView(-1, 0);
			break;
		case KeyEvent.VK_DOWN:
			editor.moveView(0, 1);
			break;
		case KeyEvent.VK_UP:
			editor.moveView(0, -1);
			break;
		case KeyEvent.VK_ESCAPE:
			editor.openEditorMenu();
			break;
		case KeyEvent.VK_S:
			if (event.isControlDown()) {
				editor.saveMap();
			}
			break;
		case KeyEvent.VK_R:
			editor.rotatePlacement();
			break;
		default:
			break;
		}

		editor.setControlKeyPressed(event.isControlDown());
	}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyReleased(KeyEvent e) {
		editor.setControlKeyPressed(e.isControlDown());
	}
}

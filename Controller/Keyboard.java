package Controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import Model.Game;

public class Keyboard implements KeyListener {
	private Game game;

	public Keyboard(Game game) {
		this.game = game;
	}

	@Override
	public void keyPressed(KeyEvent event) {
		int key = event.getKeyCode();

		switch (key) {
		case KeyEvent.VK_RIGHT:
			game.moveActivePlayer(1, 0);
			break;
		case KeyEvent.VK_LEFT:
			game.moveActivePlayer(-1, 0);
			break;
		case KeyEvent.VK_DOWN:
			game.moveActivePlayer(0, 1);
			break;
		case KeyEvent.VK_UP:
			game.moveActivePlayer(0, -1);
			break;
		case KeyEvent.VK_ESCAPE:
			game.openGameMenu();
			break;
		case KeyEvent.VK_S:
			if (event.isControlDown())
				game.saveGame();
			break;
		case KeyEvent.VK_R:
			if (event.isControlDown())
				game.restoreGame();
			break;
		default:
			break;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}
}

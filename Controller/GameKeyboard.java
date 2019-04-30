package Controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import Model.Game;

public class GameKeyboard implements KeyListener {
	private Game game;

	public GameKeyboard(Game game) {
		this.game = game;
	}

	@Override
	public void keyPressed(KeyEvent event) {
		if (!game.isRunning())
			return;

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
			if (event.isControlDown()) {
				game.pauseGame();
				game.saveGame();
				game.resumeGame();
			}
			break;
		case KeyEvent.VK_R:
			if (event.isControlDown()) {
				game.pauseGame();
				game.restoreGame();
				game.resumeGame();
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyReleased(KeyEvent e) {}
}

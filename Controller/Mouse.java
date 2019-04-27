package Controller;

import Model.Game;

import Tools.Point;

public class Mouse {
    private Game game;

    public Mouse(Game game) {
        this.game = game;
    }

	public void mapLeftClick(int x, int y) {
		synchronized(game) {
			game.mouseLeftClickEvent(new Point(x, y));
		}
	}

	public void mapRightClick(int x, int y) {
		synchronized(game) {
			game.mouseRightClickEvent(new Point(x, y));
		}
	}
}

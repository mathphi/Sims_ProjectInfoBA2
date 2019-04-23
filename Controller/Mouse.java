package Controller;

import Model.Game;

import Tools.Point;

public class Mouse {
    private Game game;

    public Mouse(Game game) {
        this.game = game;
    }

	public void mapEvent(int x, int y) {
		synchronized(game) {
			game.sendPlayer(new Point(x, y));
		}
	}
}

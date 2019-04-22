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
            game.movePlayer(1, 0);
            break;
        case KeyEvent.VK_LEFT:
            game.movePlayer(-1, 0);
            break;
        case KeyEvent.VK_DOWN:
            game.movePlayer(0, 1);
            break;
        case KeyEvent.VK_UP:
            game.movePlayer(0, -1);
             break;
         case KeyEvent.VK_SPACE:
             game.action();
             break;
         case KeyEvent.VK_Q:
             game.stop();
             break;
         case KeyEvent.VK_T:
             game.tirePlayer();
             break;
        case KeyEvent.VK_P:
             game.playerPos();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Controller.EditorKeyboard;
import Controller.EditorMouse;
import Controller.GameKeyboard;
import Controller.GameMouse;

import Model.Editor;
import Model.Game;

import View.Window;

public class Main {
	private static final String DEFAULT_LOAD_PATH = "src/Data/sample1.map";
	
	public static void main(String[] args) {
		Window window = new Window("Game");

		// Initialise game
		Game game = new Game(window);
		GameKeyboard gameKeyboard = new GameKeyboard(game);
		GameMouse gameMouse = new GameMouse(game);
		window.addMapKeyListener(gameKeyboard);
		window.addMapMouseListener(gameMouse);

		// Initialise editor
		Editor editor = new Editor(window);
		EditorKeyboard editorKeyboard = new EditorKeyboard(editor);
		EditorMouse editorMouse = new EditorMouse(editor);
		window.addMapKeyListener(editorKeyboard);
		window.addMapMouseListener(editorMouse);

		game.setCreatorAction(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				game.stopGame();
				game.closeGameMenu();
				editor.start();
			}
		});
		
		editor.setStartGameAction(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				editor.stop();
				editor.closeEditorMenu();
				game.loadGameMapPacket(editor.getGameMapPacket());
				game.startGame();
			}
		});

		/*
		 * Load a sample map file
		 */
		game.restoreFromFile(DEFAULT_LOAD_PATH);
		game.openGameMenu();
	}
}

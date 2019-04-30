import Controller.EditorKeyboard;
import Controller.EditorMouse;
import Controller.GameKeyboard;
import Controller.GameMouse;

import Model.Editor;
import Model.Game;

import View.Window;

public class Main {
	public static void main(String[] args) {
		Window window = new Window("Game");

		// Initialize game
		Game game = new Game(window);
		GameKeyboard gameKeyboard = new GameKeyboard(game);
		GameMouse gameMouse = new GameMouse(game);
		window.addMapKeyListener(gameKeyboard);
		window.addMapMouseListener(gameMouse);

		// Initialize editor
		Editor editor = new Editor(window);
		EditorKeyboard editorKeyboard = new EditorKeyboard(editor);
		EditorMouse editorMouse = new EditorMouse(editor);
		window.addMapKeyListener(editorKeyboard);
		window.addMapMouseListener(editorMouse);
		
		window.switchEditorMode();
		
		//game.startGame();
		
		editor.start();
	}
}

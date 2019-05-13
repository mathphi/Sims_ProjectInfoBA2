import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Controller.EditorKeyboard;
import Controller.EditorMouse;
import Controller.GameKeyboard;
import Controller.GameMouse;
import Controller.ImagesFactory;
import Model.Editor;
import Model.Game;
import Resources.ResourceLoader;
import View.LoadingFrame;
import View.Window;

public class Main {
	private static final String DEFAULT_LOAD_PATH = "Data/sample2.map";
	
	public static void main(String[] args) {
		/*
		 * Try to load all resources in a temporary file (if we are running in a JAR)
		 */
		LoadingFrame splash = new LoadingFrame();
		splash.showFrame();
		
		try {
			ResourceLoader.loadAll("Data");
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		/* Build and run the game */
		
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
		 * Load all images at starting of the program
		 */
		try {
			ImagesFactory.loadAllImages();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		
		/*
		 * Load a sample map file
		 */
		game.restoreFromFile(ResourceLoader.getResourcePath(DEFAULT_LOAD_PATH));
		
		splash.hideFrame();
		window.setVisible(true);
		
		game.openGameMenu();
	}
}

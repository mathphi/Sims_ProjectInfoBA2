import Controller.Keyboard;

import Controller.Mouse;
import GameObject.Adult;
import GameObject.EatableObject;
import GameObject.GameObject;
import GameObject.Kid;
import GameObject.Person;
import GameObject.Teenager;
import Model.Game;
import View.Window;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.sql.Date;
import java.text.DateFormat;

public class Main {
	public static int year=0;
	public static Person player;
	public static void main(String[] args) {
		newPart();
		endOfDay();
		// Window window = new Window("Game");
		// Game game = new Game(window);
		// Keyboard keyboard = new Keyboard(game);
		// Mouse mouse = new Mouse(game);
		// window.setKeyListener(keyboard);
		// window.setMouseListener(mouse);
	}

	public static void newPart() {
		//creation of the player character
		Date date = new Date(23 / 07 / 1999);
		ArrayList<GameObject> startInventory = new ArrayList<GameObject>();
		startInventory.add(new EatableObject(0, 0, 2, "bar de céréale", 2));
		Person player = new Kid(0, 0, "Dardenne", "Corentin", date, "m", 50, startInventory);
	}
	
	public static void endOfDay() {
		// function that restore all the caracteristic like energy,...
		// nextPlayer() -> in multi mode will allow other players to play
		player.setCaracteristic(); // modification of energy CE SRA UN UPDATE TODO
		year += 1; // 1 turns = 1year
		
		switch (year) {
		//check if the type of the player chanfe
		case 14: {
			player = (Teenager) player;
			break;
		}
		case 20: {
			player = (Adult) player;
			break;
		}

		}


	}
}

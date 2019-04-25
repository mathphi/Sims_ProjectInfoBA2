import Controller.Keyboard;

import Controller.Mouse;
import Model.Adult;
import Model.EatableObject;
import Model.Game;
import Model.GameObject;
import Model.Kid;
import Model.Person;
import Model.Teenager;
import View.Window;
import Tools.Point;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.sql.Date;
import java.text.DateFormat;

public class Main {
	public static int year;
	public static Person player;
	public static ArrayList<Person> personList; //list of all the people in the game
	public static void main(String[] args) {
		Window window = new Window("Game");
		Game game = new Game(window);
		Keyboard keyboard = new Keyboard(game);
		Mouse mouse = new Mouse(game);
		window.setKeyListener(keyboard);
		window.setMouseListener(mouse);
		
		//newPart();
		//endOfDay();
	}

	public static void newPart() {
		//creation of the player character
		Date date = new Date(23 / 07 / 1999);
		year = date.getYear()+10; //make the game start when the player is 10 YO
		Kid player = new Kid(new Point(0, 0), "Dardenne", "Corentin", "m", date, 10, null, null, null);
		// TODO basic inentory and parents to add
		//TODO generated all the other peope 
	
	}
	
	public static void endOfDay() {
		// function that restore all the caracteristic like energy,...
		// nextPlayer() -> in multi mode will allow other players to play
		player.modifyProperties(); // modification of energy CE SRA UN UPDATE TODO
		
		//TODO: NOOO! Check the age of each person... Not years from start of game
		year += 1; // 1 turns = 1year
		/*
		for(Person people : personList) {
			int age = year - people.getBirthDate().getYear();
		
		switch (year) {
		//check if the type of the player change
		case 14: {
			//TODO (in progress!)
		}
			//personList.add(new Teenager(newPeople));
			break;
		}
		case 20: {
			player = (Adult) player;
			break;
		}
		default:
			break;
		} */


	}
}

package GameObject;

import java.util.GregorianCalendar;

import java.util.ArrayList;
import java.util.Date;

public class OtherCharacter extends Person implements AutomaticCharacter{
	// contains all the PNJ that are never commanded by the player
	//specific caracteristic 
	
	
	public OtherCharacter(int X, int Y, String type, String gender) {

		// general information decided in character, most of them are randomly generated
		super(X, Y, getRandomName(), getRandomFirstName(gender), getRandomBirthDay(type), gender,
				getRandomMoney(type), getRandomInventoryHouse(type));

	}
	
	
	
	
	
	
	
	public void doAutomaticThing() {
		//TODO
	}
	
	

	protected static String getRandomName() {
		//TODO
		String randomName = "Darde   nne"; // hum yes
		return randomName;
	}

	private static String getRandomFirstName(String gender) {
		//TODO
		String randomFirstName = "Corentin"; // hum yes
		return randomFirstName;
	}

	private static Date getRandomBirthDay(String type) {
		// function that give a random birthday depending of the type of people (child,
		// teenager, adult)
		
		int year = 0; // defaultValue
		switch (type) {
		// year depends of the type of character 
		case "child": {
			year = randBetween(2003, 2010); // TODO inclure année du jeu si création personnage par après C
		}
		case "teenager": {
			year = randBetween(1997, 2003); // TODO inclure année du jeu si création personnage par après
		}
		case "adult": {
			year = randBetween(1975, 1997); // TODO inclure année du jeu si création personnage par après
		}

		}
		int month = randBetween(1, 12);
		int day = randBetween(1, daysInMonth(month, year)); //need to check if its 30,31 or leapyear
		GregorianCalendar BirthDay = new GregorianCalendar(year, month, day);
		return  BirthDay.getTime();

	}

	private static int daysInMonth(int month, int year) {
		//function come from TP6, slides
		int value = 0;
		int[] daysInMonths = new int[]
				{31,28,31,30,31,30,31,31,30,31,30,31};
		if (isLeapYear(year) && month == 2) {
			value = 0;
		} else {
			value = 1;
		}
		return daysInMonths[month - 1] + value;
	}

	private static boolean isLeapYear(int year) {
		Boolean res = false;
		if (year%4 == 0 && year%100!=0) {
			res = true;
		}
		if(year%400==0){
			res = true;
		}
		return res;
	}

	protected static int randBetween(int start, long l) {
		//TODO comment la rendre access partout?
		return start + (int) Math.round(Math.random() * (l - start));
	}
	

	private static int getRandomMoney(String type) {
		int money = 0;

		switch (type) {
		// money depends of the type of personnage
		// will probably be adapted with the price of things
		case "child": {
			money = randBetween(5, 50);
		}
		case "teenager": {
			money = randBetween(20, 150);
		}
		case "adult": {
			money = randBetween(100, 1000);
		}

		}
		return money;
	}

	private static ArrayList<GameObject> getRandomInventoryHouse(String type) {
		ArrayList<GameObject> randomInventoryHouse = new ArrayList<GameObject>();
		// create a random inventory in function of the type of character
		// adult will have more things than child
		return randomInventoryHouse;
	}
}

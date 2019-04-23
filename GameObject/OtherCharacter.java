package GameObject;

import java.util.GregorianCalendar;

import Tools.Random;

import Model.GameObject;
import Model.Person;

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
			year = Random.range(2003, 2010); // TODO inclure ann�e du jeu si cr�ation personnage par apr�s C
		}
		case "teenager": {
			year = Random.range(1997, 2003); // TODO inclure ann�e du jeu si cr�ation personnage par apr�s
		}
		case "adult": {
			year = Random.range(1975, 1997); // TODO inclure ann�e du jeu si cr�ation personnage par apr�s
		}

		}
		int month = Random.range(1, 12);
		int day = Random.range(1, daysInMonth(month, year)); //need to check if its 30,31 or leapyear
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

	private static int getRandomMoney(String type) {
		int money = 0;

		switch (type) {
		// money depends of the type of personnage
		// will probably be adapted with the price of things
		case "child": {
			money = Random.range(5, 50);
			break;
		}
		case "teenager": {
			money = Random.range(20, 150);
			break;
		}
		case "adult": {
			money = Random.range(100, 1000);
			break;
		}
		default:
			break;
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

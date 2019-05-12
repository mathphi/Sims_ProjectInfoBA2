package Model;

import java.io.Serializable;

import Tools.Random;

public class PsychologicalFactors implements Serializable {
	private static final long serialVersionUID = 4526519314985078559L;
	
	public enum CharacterTraits {
		Mood, Hygiene, GeneralKnowledge, OthersImpression
	}
	
	private double mood;
	private double hygiene;
	private double generalKnowledge;
	private double othersImpression;
	
	public PsychologicalFactors(double mood, double hygiene, double generalKnowledge, double othersImpression) {
		this.mood = mood;
		this.hygiene = hygiene;
		this.generalKnowledge = generalKnowledge;
		this.othersImpression = othersImpression;
		
		parametrizeFactors();
	}
	
	public static PsychologicalFactors randomFactors() {
		return new PsychologicalFactors(
				Random.range(15, 25),
				Random.range(15, 25),
				Random.range(15, 25),
				Random.range(15, 25)
			);
	}
	
	/**
	 * We want to normalise the factors between the persons to have a balance of behaviours.
	 * So the sum of the factors must be equal to 1.0
	 */
	private void parametrizeFactors() {
		double sum = mood + hygiene + generalKnowledge + othersImpression;

		if (sum > 0) {
			mood /= sum;
			hygiene /= sum;
			generalKnowledge /= sum;
			othersImpression /= sum;
		}
	}
	
	public double getMood() {
		return mood;
	}
	
	public double getHygiene() {
		return hygiene;
	}

	public double getGeneralKnowledge() {
		return generalKnowledge;
	}

	public double getOthersImpression() {
		return othersImpression;
	}
	
	public CharacterTraits getPrincipalCharacterTrait() {
		CharacterTraits ct = CharacterTraits.GeneralKnowledge;
		
		if (mood > hygiene && mood > generalKnowledge && mood > othersImpression) {
			ct = CharacterTraits.Mood;
		}
		else if (hygiene > generalKnowledge && hygiene > othersImpression) {
			ct = CharacterTraits.Hygiene;
		}
		else if (generalKnowledge > othersImpression) {
			ct = CharacterTraits.GeneralKnowledge;
		}
		else {
			ct = CharacterTraits.OthersImpression;
		}
		
		return ct;
	}
}

package Model;

import java.io.Serializable;

import Tools.Random;

public class PsychologicalFactors implements Serializable {
	private static final long serialVersionUID = 4526519314985078559L;
	
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
	
	public static PsychologicalFactors RandomFactors() {
		//TODO: set good factors
		return new PsychologicalFactors(
				Random.range(10, 25),
				Random.range(10, 25),
				Random.range(10, 25),
				Random.range(10, 25)
			);
	}
	
	/**
	 * We want to normalise the factors between the persons to have a balance of behaviours.
	 * So the sum of the factors must be equal to 1.0
	 */
	private void parametrizeFactors() {
		double sum = mood + hygiene + generalKnowledge + othersImpression;

		if (sum > 0) {
			mood *= (1.0 / sum);
			hygiene *= (1.0 / sum);
			generalKnowledge *= (1.0 / sum);
			othersImpression *= (1.0 / sum);
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
}

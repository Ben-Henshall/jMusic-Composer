package data;

/**
 * @author Benjamin Henshall
 *
 *         Class used to store probability for the rhythm of a pitch
 */
public class RhythmObject {

	// Count of how many times this rhythm value has occurred. Used to calculate
	// probability.
	private int count;

	// Stores probability of this rhythm value occurring
	private double probability;

	// Stores rhythm value of this object
	private double value;

	public RhythmObject(double rhythmValue) {
		value = rhythmValue;
		count = 1;
	}

	/*
	 * Getter for rhythm value
	 */
	public double getValue() {
		return value;
	}

	/*
	 * Getter for the probability of this object
	 */
	public double getProbability() {
		return probability;
	}

	/*
	 * Method for updating the probability of this rhythm value occurring
	 */
	public void updateProbability(int totalRhythms) {
		probability = (double) count / (double) totalRhythms;
	}

	/*
	 * Method for incrementing the count for occurrences of this rhythm value
	 */
	public void incrementCount() {
		count++;
	}
}
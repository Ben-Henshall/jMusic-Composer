package data;

/**
 * @author Benjamin Henshall
 *
 *         Class used to store probability for the dynamic of a pitch
 */
public class DynamicObject {

	// Count of how many times this dynamic has occurred. Used to calculate
	// probability.
	private int count;

	// Stores probability of this dynamic occurring
	private double probability;

	// Stores dynamic value of this object
	private int value;

	public DynamicObject(int dynamicValue) {
		value = dynamicValue;
		count = 1;
	}

	/*
	 * Getter for dynamic value
	 */
	public int getValue() {
		return value;
	}

	/*
	 * Getter for the probability of this object
	 */
	public double getProbability() {
		return probability;
	}

	/*
	 * Method for updating the probability of this dynamic occurring
	 */
	public void updateProbability(int totalDynamics) {
		probability = (double) count / (double) totalDynamics;
	}

	/*
	 * Method for incrementing the count for occurrences of this dynamic
	 */
	public void incrementCount() {
		count++;
	}
}
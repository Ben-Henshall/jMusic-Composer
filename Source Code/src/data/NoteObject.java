package data;

import java.util.ArrayList;
import jm.JMC;
import jm.music.data.Note;

/**
 * @author Benjamin Henshall
 * 
 *         The NoteObject class is used to store the probability for a note
 *         occurring. The main function of it is to store the probability for a
 *         pitch, while also having a means of randomly generating a rhythm and
 *         dynamic for the note.
 *
 */
public class NoteObject implements JMC {

	// Variable that stores how many time this note has been found
	private int count;

	// Probability for how likely this pitch is to appear
	private double probability;

	// Pitch value of this object
	private int pitchValue;

	// ArrayLists for storing the probabilities of the dynamic and rhythm found
	// for this pitch
	private ArrayList<RhythmObject> rhythmArray;
	private ArrayList<DynamicObject> dynamicArray;

	public NoteObject(int value) {
		pitchValue = value;
		count = 0;
		probability = 0;
	}

	/*
	 * Method for increasing the value of count.
	 */
	public void incrementCount() {
		count++;
	}

	/*
	 * Method for getting the probability that this pitch will occur
	 */
	public double getProbability() {
		return probability;
	}

	/*
	 * Method for updating the probability to the correct value by doing number
	 * of times this pitch has occurred divided by total number of chains found
	 */
	public void updateProbability(int countOfNotes) {
		// Only tries to update the probability if this pitch should be more
		// than 0
		if (count != 0) {
			// Updates the probability of the pitch
			probability = (double) count / (double) countOfNotes;

			// Then calls to update the probabilities of the dynamic and rhythm
			// arrays
			updateDynamicProbability();
			updateRhythmProbability();
		}
	}

	/*
	 * Method for updating all the dynamic probabilities associated with this
	 * pitch
	 */
	public void updateDynamicProbability() {
		// Iterates over all the dynamic values we have for this pitch and
		// updates the probability of them
		for (int i = 0; i < dynamicArray.size(); i++) {
			dynamicArray.get(i).updateProbability(count);
		}
	}

	/*
	 * Method for updating all the rhythm probabilities associated with this
	 * pitch
	 */
	public void updateRhythmProbability() {
		// Iterates over all the rhythm values we have for this pitch and
		// updates the probability of them
		for (int i = 0; i < rhythmArray.size(); i++) {
			rhythmArray.get(i).updateProbability(count);
		}
	}

	/*
	 * Method for adding a new dynamic to the array of dynamic probabilities
	 */
	public void addDynamic(Note note) {
		// Boolean used to track whether or the dynamic value already exists
		// within the dynamic array
		boolean found = false;

		// Loop that iterates over the existing dynamic values found to see if
		// the new dynamic added matches any of these.
		for (int i = 0; i < dynamicArray.size(); i++) {
			// If the dynamic already exists in the array, then just increment
			// the count of the dynamic
			if (dynamicArray.get(i).getValue() == note.getDynamic()) {
				dynamicArray.get(i).incrementCount();
				found = true;

				// We've found the dynamic, so we can break the loop
				break;
			}
		}

		// If the dynamic didn't already exist in the array, then we need to add
		// a new dynamic object to it with the new dynamic value
		if (!found) {
			dynamicArray.add(new DynamicObject(note.getDynamic()));
		}
	}

	/*
	 * Method for adding a new dynamic to the array of dynamic probabilities
	 */
	public void addRhythm(Note note) {
		// Boolean used to track whether or the dynamic value already exists
		// within the dynamic array
		boolean found = false;

		// Loop that iterates over the existing dynamic values found to see if
		// the new dynamic added matches any of these.
		for (int i = 0; i < rhythmArray.size(); i++) {
			// If the dynamic already exists in the array, then just increment
			// the count of the dynamic
			if (rhythmArray.get(i).getValue() == note.getRhythmValue()) {
				rhythmArray.get(i).incrementCount();

				// We've found the dynamic, so we can break the loop
				found = true;
				break;
			}
		}

		// If the dynamic didn't already exist in the array, then we need to add
		// a new dynamic object to it with the new dynamic value
		if (!found) {
			rhythmArray.add(new RhythmObject(note.getRhythmValue()));
		}
	}

	/*
	 * Method for adding to the probability of this note (i.e. if we found
	 * another pitch like this with the same chain)
	 */
	public void addNewNote(Note note) {
		//
		if (count == 0) {
			dynamicArray = new ArrayList<DynamicObject>(1);
			rhythmArray = new ArrayList<RhythmObject>(1);
		}

		// increment count
		incrementCount();

		// if DynamicValue of note exists in dynamicObject[]
		addDynamic(note);
		addRhythm(note);
	}

	/*
	 * Method for fetching a random dynamic for this pitch
	 */
	public int getRandomDynamic() {
		// Generates a random number between 0 and 1
		double randomNumber = Math.random();

		// Cumulative total for probability
		double ranTotal = 0;

		// Loops through the array of probabilities, adding to the cumulative
		// total as we progress until the total surpasses the probability of the
		// current iteration. This is the dynamic we select.
		for (int i = 0; i < dynamicArray.size(); i++) {
			ranTotal = ranTotal + dynamicArray.get(i).getProbability();
			if (randomNumber <= ranTotal) {
				// Breaks the loop by returning the rolled value
				return dynamicArray.get(i).getValue();
			}
		}

		// If we don't have any dynamic values in our list, then return an
		// average value just in case
		return 50;
	}

	/*
	 * Method for fetching a random dynamic for this pitch
	 */
	public double getRandomRhythm() {
		// Generates a random number between 0 and 1
		double randomNumber = Math.random();

		// Cumulative total for probability
		double ranTotal = 0;

		// Loops through the array of probabilities, adding to the cumulative
		// total as we progress until the total surpasses the probability of the
		// current iteration. This is the rhythm value we select.
		for (int i = 0; i < rhythmArray.size(); i++) {
			ranTotal = ranTotal + rhythmArray.get(i).getProbability();
			if (randomNumber <= ranTotal) {
				// Breaks the loop by returning the rolled value
				return rhythmArray.get(i).getValue();
			}
		}

		// If we don't have any rhythm values in our list, then return an
		// average value just in case
		return 0.5;
	}

	/*
	 * Method for returning a completely note. Generates the dynamic and rhythm
	 * to be used with the pitch.
	 */
	public Note returnNote() {
		// Gets a random dynamic
		int noteDynamic = getRandomDynamic();
		// And a random rhythm value
		double noteRhythm = getRandomRhythm();

		// Constructs a note using our pitch and generated rhythm/dynamic and
		// returns it
		Note note = new Note(pitchValue, noteRhythm, noteDynamic);
		return note;
	}

	/*
	 * Method for setting a new probability value manually
	 */
	public void setNewProbability(double newProb) {
		probability = newProb;
		// Rounds the value up for displaying on our table
		probability = Math.round(probability * 100000.0) / 100000.0;
	}

	/*
	 * Another method for setting probability manually. This version is used to
	 * adds a dynamic and rhythm to pitches that have a count of 0, which
	 * wouldn't normally have a rhythm or dynamic but still need to generate
	 * one.
	 */
	public void setNewProbability(double newProb, Note note) {
		// If we haven't already created the array of dynamic/rhythm value
		// probabilities, instantiate them
		if (dynamicArray == null || rhythmArray == null) {
			dynamicArray = new ArrayList<DynamicObject>(1);
			rhythmArray = new ArrayList<RhythmObject>(1);
		}
		probability = newProb;
		addRhythm(note);
		addDynamic(note);
	}
}
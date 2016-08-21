package data;

import java.util.Vector;
import jm.music.data.Note;

/**
 * @author Benjamin Henshall
 * 
 *         Class used to store a row of probabilities.
 */
public class MatrixRow {

	// Variable that stores how many times this chain has appeared
	private int totalCount;

	// Stores the individual probabilities of a note appearing in this chain.
	// Position of the noteObject in the array is based on the pitch, for
	// example, the probability of pitch 60 is stored in pitchProbabilities[60].
	NoteObject[] pitchProbabilities;

	// Stores the total number of pitches (Used to define arrays)
	int numOfPitches;

	public MatrixRow(int totalPitches) {
		numOfPitches = totalPitches;

		// Starts the initial count at 0
		totalCount = 0;

		// Defines the size of the note object array, so it can store the total
		// number of unique pitches
		pitchProbabilities = new NoteObject[numOfPitches + 1];

		// Loop to instantiate all of the note object array
		for (int i = 0; i <= numOfPitches; i++) {
			NoteObject newPitch = new NoteObject(i);
			pitchProbabilities[i] = newPitch;
		}
	}

	/*
	 * Returns the total number of times this note has occurred
	 */
	public int getTotalCount() {
		return totalCount;
	}

	/*
	 * Method for generating a new note using the probabilities in
	 * pitchProbabilities[].
	 */
	public Note getNextNote() {
		Note note = new Note();

		// Generates a random number between 0 and 1
		double randomNumber = Math.random();

		// Variable to store the cumulative total of the random numbers so far
		double ranTotal = 0;

		// Loop that tries to cycle through total number of pitches unless
		// broken
		for (int i = 0; i < numOfPitches + 1; i++) {
			// Adds the probability of the note we're looking at to the
			// cumulative total
			ranTotal = ranTotal + pitchProbabilities[i].getProbability();

			// If the new total becomes more than the random number we
			// generated, then we use this note
			if (randomNumber <= ranTotal) {
				// Return a note of pitch i with rhythm and dynamic configured
				// by the note object through this method
				note = pitchProbabilities[i].returnNote();

				// We've generated our note fairly, so break the loop
				break;
			}
		}

		// Return the note we generated
		return note;
	}

	/*
	 * Method to update the probability to reflect the number of occurrences or
	 * if the user has edited the probability
	 */
	public void updateProbability(int prevNote) {
		// If we have at least one occurrence of this chain....
		if (totalCount != 0) {
			// Then cycle through all of the pitches and update the
			// probabilities
			for (int i = 0; i < numOfPitches; i++) {
				pitchProbabilities[i].updateProbability(totalCount);
			}
		} else {
			// Else there were no occurrences, but we still want to have some
			// sort of probability there to keep the program generating. We can
			// do this by creating an average note of pitch previous note+1 then
			// adding it to to the list of our probability. This ensures if we
			// ever hit an "empty" chain, where we had no occurrences, the
			// program will continually rise in pitches note by note until we
			// find a chain that DOES have occurrences.

			// Creates a new note of average dynamic and rhythm
			Note note = new Note();
			note.setDynamic(70);
			note.setRhythmValue(0.25);

			// If we're at the max pitch, then we want the new note to be
			// generated at pitch 0
			if (prevNote + 1 >= numOfPitches) {
				note.setPitch(0);
				pitchProbabilities[0].setNewProbability(1, note);
			} else {
				// Else we just create a note that is one pitch above the
				// previous to create a rising pitch effect
				note.setPitch(prevNote + 1);
				pitchProbabilities[prevNote + 1].setNewProbability(1, note);
			}
		}
	}

	/*
	 * Method used to add a note to the list of probabilities. Adds the note to
	 * pitchProbabilities[note pitch] and increases the total count of this
	 * chain
	 */
	public void addToProbabilities(Note note) {
		pitchProbabilities[note.getPitch()].addNewNote(note);
		totalCount++;
	}

	/*
	 * Method for returning a vector containing a row of probabilities.
	 */
	public Vector getProbabilityArray() {
		Vector prob = new Vector();

		// Cycles through the NoteObject array, adding the probability to the
		// vector each time.
		for (int i = 0; i < numOfPitches; i++) {
			prob.add(pitchProbabilities[i].getProbability());
		}
		// The result of this is a vector that represents the row of the
		// NoteMatrix.

		return prob;
	}

	/*
	 * Method for setting probabilities in the row. Takes an int representing
	 * the pitch of the note we're editing and a double for the new probability.
	 * 
	 * Note on how probabilities are distributed: In order to get the
	 * probability correctly distributed when the user edits I had to create a
	 * custom formula. Since the total probability over a row must equal 1, I
	 * wanted the user to be able to edit a single probability and have the
	 * program automatically alter the other probabilities to compensate for the
	 * increase/reduction by adjusted the probabilities of other notes. For
	 * example, if I had a probability and 0.75 and one at 0.25 and I increased
	 * the 0.25 probability to 0.5, then the other probability should reduce to
	 * 0.5 to keep the row value at 1. The important part of this, is keeping
	 * the ratio correct.
	 * 
	 * I developed the following formula to calculate this: With an example,
	 * where A, B, C are the only probabilities in a row, A' is the new
	 * probability:
	 * 
	 * A-K=A', so K is the difference between old and new prob (Remainder in my
	 * program)
	 * 
	 * The new probability for B would be:
	 * 
	 * (K*B)/(B+C)+B
	 * 
	 * And the new probability for C would be:
	 * 
	 * (K*C)/(B+C)+C
	 * 
	 * So, for an example with actual values, imagine we have the row: A B C
	 * 0.25 0.25 0.5
	 * 
	 * And if we edit A from 0.25 to 0.5, that gives us K=0.25
	 * 
	 * So the new prob for B would be:
	 * 
	 * (-0.25*0.25)/(0.25+0.5)+0.25 = .1666 recurring
	 * 
	 * And the new prob for C would be:
	 * 
	 * (-0.25*0.5)/(0.25+0.5)+0.5 = 0.333 recurring
	 * 
	 * These two new probabilities added together equal 0.5, which, when added
	 * back to the new probability of A, equals 1.
	 */
	public void setNewProbability(int note, double newProb) {

		// Variable that stores the current probability of the note
		double oldProb = pitchProbabilities[note].getProbability();

		// Then sets the note to the new probability that the user entered
		pitchProbabilities[note].setNewProbability(newProb);

		// Variable for storing the total probability left over all notes EXCEPT
		// for the one being edited.
		double total = 0;

		// Loop that gets the total value for all probabilities summed except
		// for the note being edited
		for (int i = 0; i < pitchProbabilities.length; i++) {
			if (i != note && pitchProbabilities[i].getProbability() != 0) {
				total += pitchProbabilities[i].getProbability();
			}
		}

		double remainder = oldProb - newProb;

		// Loop that edits all probabilities except the note that the user
		// edited by using the formula specified above.
		for (int i = 0; i < pitchProbabilities.length; i++) {
			if (i != note && pitchProbabilities[i].getProbability() != 0) {
				pitchProbabilities[i].setNewProbability((remainder * pitchProbabilities[i].getProbability()) / total
						+ pitchProbabilities[i].getProbability());
			}
		}
	}

}
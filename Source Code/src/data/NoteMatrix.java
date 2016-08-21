package data;

import java.util.Vector;
import jm.JMC;
import jm.music.data.Note;
import jm.music.data.Part;
import jm.music.data.Phrase;
import jm.music.data.Score;
import jm.music.tools.Mod;

/**
 * @author Benjamin Henshall
 * 
 *         Class used to store the matrix from reading in MIDI files. Contains
 *         methods for building, querying and organising the entire matrix.
 */
public class NoteMatrix implements JMC {

	// 2D array of MatrixRows which represent a row in the matrix. Rows are
	// stored in in the array position relative to their value. For example, if
	// you wanted the probability details of the chain 60, 63, you would look in
	// matrixRow[60][63].
	private MatrixRow[][] matrixRow;

	// Variable for how many pitches total found throughout all scores imported.
	// Used to define array sizes.
	private int numOfPitches;

	public NoteMatrix(int high) {
		numOfPitches = high;

		// Defines the size of the matrixRow 2D array. Size is +1 due to 0 also
		// being a valid pitch in jMusic.
		matrixRow = new MatrixRow[numOfPitches + 1][numOfPitches + 1];

		// Cycles through the 2D array, defining each element as a new
		// MatrixRow.
		for (int i = 0; i < numOfPitches + 1; i++) {
			for (int j = 0; j < numOfPitches + 1; j++) {
				matrixRow[i][j] = new MatrixRow(numOfPitches);
			}
		}
	}

	/*
	 * Method for adding songs to the matrix, as the constructor only creates
	 * the objects but does not fill them with data/probabilities.
	 */
	public void addToMatrix(Score s) {
		// Quick check to make sure the MIDI file isn't just an empty file
		if (s.getPartArray().length > 0) {
			// Merges all all of the scores parts into a single phrase array.
			Phrase[] phrases = mergePartsReturnPhrases(s.getPartArray());

			// Nested loop that repeats for every phrase in phrases...
			for (int i = 0; i < phrases.length; i++) {

				// Gets the note array of the phrase
				Note[] noteArray = phrases[i].getNoteArray();

				// Loop that repeats for every note from note 2 onwards and adds
				// to the probability of that chain occurring
				for (int k = 2; k < noteArray.length; k++) {
					// Quick check to make sure the note is a valid pitch
					// (Protection against possibility of corrupt MIDI files)
					if (noteArray[k].getPitch() > 0 && noteArray[k - 2].getPitch() > 0
							&& noteArray[k - 1].getPitch() > 0) {
						// Adds to the probability of the note k occurring after
						// note k-2, note k-1
						matrixRow[noteArray[k - 2].getPitch()][noteArray[k - 1].getPitch()]
								.addToProbabilities(noteArray[k]);
					}
				}
			}
		}
	}

	/*
	 * Method that merges all parts of a song together and returns an array of
	 * phrases. Most MIDI files are in separate parts, such as right hand/left
	 * hand, so this stage is necessary.
	 */
	public Phrase[] mergePartsReturnPhrases(Part[] parts) {
		// Cycles through the array of parts and merges them all into parts[0]
		// using a jMusic function
		for (int i = 1; i < parts.length; i++) {
			Mod.merge(parts[0], parts[i]);
		}

		// Returns the array of phrases from the merged parts[0]
		Phrase[] phr = parts[0].getPhraseArray();

		return phr;
	}

	/*
	 * Wrapper method for generating the next note when giving the previous two
	 * pitches
	 */
	public Note getNextNote(int pitch1, int pitch2) {
		return matrixRow[pitch1][pitch2].getNextNote();
	}

	/*
	 * Method for updating the probabilities of the matrix. Before this is
	 * called, all probabilities are 0 but counts of occurrences are correct.
	 */
	public void updateProbabilities() {

		// Loops through all objects in the matrix row 2D array
		for (int i = 0; i < numOfPitches + 1; i++) {
			for (int j = 0; j < numOfPitches + 1; j++) {
				// And updates the probability for the current iteration
				matrixRow[i][j].updateProbability(j);
			}
		}
	}

	/*
	 * Method for returning the total number of unique pitches found in the
	 * imported files
	 */
	public int getNumOfPitches() {
		return numOfPitches;
	}

	/*
	 * Method for returning a vector filled a row of probabilities for given
	 * chain (note1, note2)
	 */
	public Vector getProbabilityArray(int note1, int note2) {
		Vector prob = new Vector();

		prob = matrixRow[note1][note2].getProbabilityArray();

		return prob;
	}

	/*
	 * Method for manually editing the probability of note3 resulting from the
	 * chain note1, note2.
	 */
	public void setNewProbability(int note1, int note2, int note3, double newProb) {
		matrixRow[note1][note2].setNewProbability(note3, newProb);
	}

	/*
	 * Method that returns the total number of times the chain note1, note2 has
	 * occurred within the matrix. E.g., if the chain {63, 62} occurred 300
	 * times in the files we imported, then this method will return 300.
	 */
	public int getNumOfNotesForChain(int note1, int note2) {
		int total = matrixRow[note1][note2].getTotalCount();
		return total;
	}
}
/**
 * Author: 	Benjamin Henshall
 * 			Ben@henshall.plus.com
 * 
 * 	The MIT License (MIT)
 *	Copyright (c) 2016 Benjamin Henshall
 *
 *	Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation 
 *	files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy,
 * 	modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the 
 * 	Software is furnished to do so, subject to the following conditions:
 *
 *	The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * 	OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE 
 * 	LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR 
 * 	IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package gui;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import data.NoteMatrix;
import jm.JMC;
import jm.music.data.Note;
import jm.music.data.Part;
import jm.music.data.Phrase;
import jm.music.data.Score;
import jm.util.Read;

/**
 * Authors note for producing similar songs using only one import:
 * Starting notes:
 * Furelise = 76, 75 - 90bpm
 * bumble bee = 93, 92 - 160bpm
 * 7th symph = 69, 60
 * bach minuet = 74, 67 - 95 bpm
 * baggatelles opus 119 = 86, 88 - 100 bpm
 * chopin nocture 9 = 70, 79 - 70 bpm
 * chopin prelude 28 = 59, 71 - 100 bpm
 * gardel porpno = 66, 67 - 80bpm
 * german dance = 67, 67 - 80bpm
 * wedding march = 64, 63 - 120bpm
 * moonlight sonata - 56, 61 - 60bpm
 * mozart eine kleine - 60, 55 - 100bpm
 * o holy night - 48, 64 - 100 bpm
 * ode to joy - 64, 64 - 70bpm
 * pachelbel_canon - 64, 62 - 80bpm
 * 
 * If imported one or two songs for experimenting, Fur elise and 
 * Bach Minuet poroduce good results.
 */

/**
 * 
 * @author Benjamin Henshall
 * 
 *         Class used to store main data structure (Note matrix) while also
 *         providing the logic for creating songs as well as utility
 *         methods/wrapper methods to allow the graphical interface to view and
 *         manipulate the data structure. Similar to a "model" in the
 *         Model-View-Controller paradigm. Implementing JMC allows the use of
 *         jMusic constants, such as note values (Crochet etc) and instrument
 *         lists.
 */
public class MainFrame implements JMC {

	// Constant for changing how long a bar is. A bar of length 2 is equal to a
	// normal measure.
	private static final double BAR_LENGTH = 2;

	// String constant for storing the default phrase composition of the song.
	// Must be a string letters from the English alphabet.
	public static final String DEFAULT_COMP = "ABCDEFGHI";

	// Integer constant containing the value of the default instrument
	public static final int DEFAULT_INST = PIANO;

	// Main data structure used to store a list of all chains and the
	// probabilities associated with those notes.
	private static NoteMatrix noteMatrix;

	// Two variables used to store which two notes have previously occurred.
	// Starts at a value set by the user in the GUI, then updates the values as
	// we add more notes to the new score.
	private static int prevNote1;
	private static int prevNote2;

	// JFrame used to view whichever panel we have visible.
	private static JFrame frame;

	// JPanel of type ImportPanel which is used to display information about
	// which songs have been imported, as well as buttons to view the matrix and
	// the score generation panel.
	private static ImportPanel importPanel;

	// Variable for setting tempo of the newly generated score.
	private static int TEMPO = 60;

	// Constants for setting the initial first two notes. Default value is 60
	// (Middle C) so the first two notes are 60, 60, until the user changes the
	// values, randomises, or generates a new score.
	private static int FIRST_NOTE = 60;
	private static int SECOND_NOTE = 60;

	// Array of integers used to store the composition of phrases in the score.
	// An example of how this could look is {1, 2, 1, 1, 3}, meaning unique
	// phrase 1 is added to the score, then unique phrase 2, then phrase 1 is
	// added and repeated once, then unique phrase 3 is added. These ints are
	// translated from the letters entered by the user on the ScorePanel
	// JPanel.
	public static int[] globalPattern;

	public static void main(String[] args) {
		Score s = new Score();
		Read.midi(s, "imported/pachelbel_canon.mid");

		// Starts the GUI
		MainFrame mainFrame = new MainFrame();
	}

	public MainFrame() {
		// Sets the global pattern using our default phrase composition.
		setGlobalPattern(DEFAULT_COMP);

		// Creates a new frame for for GUI.
		frame = new JFrame("jMusic Alpha");

		// Ensures the program quits correctly when using the close button
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Try and set the look and feel of the GUI. Uses the system default.
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | IllegalAccessException | InstantiationException
				| UnsupportedLookAndFeelException ex) {
			Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
		}

		// Sets the size of the frame
		frame.setSize(1100, 550);

		// Ensures we can see the frame
		frame.setVisible(true);

		// Moves the frame to the middle of the screen
		frame.setLocation((java.awt.Toolkit.getDefaultToolkit().getScreenSize().width / 2) - 600,
				(java.awt.Toolkit.getDefaultToolkit().getScreenSize().height / 2) - 300);

		// Creates a new ImportPanel, which will be used for importing files
		// into the matrix and navigating to other parts of the program
		importPanel = new ImportPanel(frame);

		// Prevents user from resizing window
		frame.setResizable(false);

		// Adds the importPanel to the existing frame
		frame.add(importPanel);

		// Opaque property has to be set to true for panel to be visible
		importPanel.setOpaque(true);

		// Adds the importPanel as the main content pane in the JFrame
		frame.setContentPane(importPanel);
	}

	/*
	 * Method for creating a new score using the note matrix and parameters
	 * specified by the user in ScorePanel. It is accessed by the score manager
	 * and returns the new score.
	 */
	public static Score createSong() {
		// Creates a new score which we will add phrases to as we go
		Score newScore = new Score();

		// Sets the tempo of the score to that specified by the user in the
		// ScorePanel panel
		newScore.setTempo(TEMPO);

		// Creates two new variables for storing the previous chain. We need two
		// new variables as we need to update the values of the previous chain
		// as we progress, rather than having a constant for it.
		prevNote1 = FIRST_NOTE;
		prevNote2 = SECOND_NOTE;

		// Creates two new notes of a quarter length using the first two notes
		// in our song
		Note note1 = new Note(prevNote1, 0.5);
		Note note2 = new Note(prevNote2, 0.5);

		// Creates a phrase that will be the first two notes, then adds the
		// notes we defined above to it.
		Phrase firstPhrase = new Phrase(0);
		firstPhrase.add(note1);
		firstPhrase.add(note2);

		// Creates a new array of phrases uses for storing unique phrases (i.e.
		// [0] is phrase A, [1] = phrase B etc). Only need to store 26 due to
		// program only accepting letters of the English alphabet
		Phrase[] phraseArray = new Phrase[26];

		// Creates the part that we will add phrases to as we go. Starts with
		// the default instrument of PIANO at start time 0.
		Part finalPart = new Part("", DEFAULT_INST, 0);

		// This loop is used to create the phrases for the score. It creates one
		// phrase for each unique letter in the composition string. For example,
		// "AABAAC" would create 3 unique phrases
		for (int i = 0; i < globalPattern.length; i++) {

			// If we haven't let created a phrase for this unique character,
			// then create one. The generateNextBar method also updates the
			// prevNote variables accordingly.
			if (phraseArray[globalPattern[i]] == null) {
				phraseArray[globalPattern[i]] = generateNextBar();
			} else {
				// Else we just update prevNotes, so that if a new phrase is
				// created afterwards, it takes into account the correct
				// previous notes.
				Note[] noteArray = phraseArray[globalPattern[i]].getNoteArray();

				// If there's only one note in the previous phrase, then don't
				// set prevNote1 as it does not exist.
				if (noteArray.length > 1) {
					prevNote1 = noteArray[noteArray.length - 2].getPitch();
				}

				// And update the previous note for further generation.
				prevNote2 = noteArray[noteArray.length - 1].getPitch();
			}

			// Copies the phrase to be added into a new phrase so that we can
			// adjust the start time without editing the phrase we have stored
			Phrase adjustPhrase = phraseArray[globalPattern[i]].copy();

			// Adjusts the start time of the newly added phrase so it starts at
			// the correct bar
			adjustPhrase.setStartTime(firstPhrase.getEndTime() + (i * BAR_LENGTH));

			// Then adds the phrase to the part to be added to the score later.
			finalPart.addPhrase(adjustPhrase);
		}

		// Adds the first phrase to the finalPart
		finalPart.addPhrase(firstPhrase);

		// And finally adds the final part to the score...
		newScore.addPart(finalPart);

		// Which is then returned.
		return newScore;
	}

	/*
	 * This method uses logic and the note matrix in order to generate the
	 * phrases which make up the final score.
	 */
	public static Phrase generateNextBar() {
		// Creates a new phrase which we will gradually add notes to
		Phrase bar = new Phrase();

		// Double for storing the cumulative time taken up by the bar we are
		// creating
		double totalTime = 0;

		// Counter for keeping track of how many times the same note has been
		// generated in a row
		int timesGeneratedSameNote = 0;

		// Loop that repeats until we have exceeded the alloted time for the bar
		while (totalTime < BAR_LENGTH) {

			// Generates a new note using probabilities from the matrix when
			// given the two previous notes
			Note newNote = noteMatrix.getNextNote(prevNote1, prevNote2);

			// If the bar is more than three quarters complete in terms of time,
			// then...
			if (totalTime > (BAR_LENGTH / 4) * 3) {
				timesGeneratedSameNote = 0;

				// Adjust the duration of the note so that it ends on by the end
				// of the bar
				newNote.setRhythmValue(BAR_LENGTH - totalTime);

				// ...Update the previous note values
				prevNote1 = prevNote2;
				prevNote2 = newNote.getPitch();

				// ...Then add the note to the bar
				bar.addNote(newNote);

				// ...And update the cumulative time counter
				totalTime += newNote.getRhythmValue();

			} else if (newNote.getRhythmValue() + totalTime <= BAR_LENGTH) {
				// Else if the note will end before the bars allocated time, add
				// the new note to the bar

				timesGeneratedSameNote = 0;

				// ...Update the previous note values
				prevNote1 = prevNote2;
				prevNote2 = newNote.getPitch();

				// ...Then add the note to the bar
				bar.addNote(newNote);

				// ...And update the cumulative time counter
				totalTime += newNote.getRhythmValue();
			} else if (timesGeneratedSameNote > 5) {
				timesGeneratedSameNote = 0;
				newNote.setRhythmValue(BAR_LENGTH - totalTime);
				prevNote1 = prevNote2;
				prevNote2 = newNote.getPitch();
				bar.addNote(newNote);
				totalTime += newNote.getRhythmValue();
			} else {
				timesGeneratedSameNote++;
			}
		}

		// Return the completed phrase
		return bar;
	}

	/*
	 * Method for converting a string of letters into an array of ints that can
	 * be used to denote the composition of the new score. Usually accessed by
	 * ScorePanel in order to update the composition structure.
	 */
	public static void setGlobalPattern(String patternAlpha) {
		// Resizes the globalPattern array to match the length of the new string
		globalPattern = new int[patternAlpha.length()];

		// Converts to lower case for simpler managing of ASCII values
		patternAlpha = patternAlpha.toLowerCase();

		// Creates an array of chars from the given string, so we can deal with
		// ASCII values instead
		char[] patternChars = patternAlpha.toCharArray();

		// Cycles through the new array of chars and adds each phrase as an int
		// into the globalPattern array. For example,
		// a string of "AABAAC" would produce the int array {1, 1, 2, 1, 1, 3}
		for (int i = 0; i < patternChars.length; i++) {
			globalPattern[i] = patternChars[i] - 97;
		}
	}

	/*
	 * Method for getting the highest pitch from all the imported files. This is
	 * needed in order to size the Note Matrix
	 */
	public static int getHighestPitchFromImports(Score[] s) {
		int highestPitch = 0;

		// Loop that cycles through the imported scores and looks at each oft he
		// scores highest pitch. If the highest pitch is higher than the
		// previous highest, it is replaced.
		for (int i = 0; i < s.length; i++) {
			if (s[i].getHighestPitch() > highestPitch) {
				highestPitch = s[i].getHighestPitch();
			}
		}
		return highestPitch;
	}

	/*
	 * Method used to populate the matrix with data from imported files.
	 */
	public static void addToMatrix(String[] files) {
		// Creates a new array of scores for storing all the scores we imported
		Score[] scoreArray = new Score[files.length];

		// Cycles through each file we want to import
		for (int i = 0; i < files.length; i++) {
			Score score = new Score();

			// Reads the score in
			Read.midi(score, files[i]);

			// Then adds it to the array
			scoreArray[i] = score;
		}

		// Creates the note matrix
		noteMatrix = new NoteMatrix(getHighestPitchFromImports(scoreArray));

		// Cycles through the array of scores and adds them to the note matrix
		// we just created
		for (int i = 0; i < scoreArray.length; i++) {
			noteMatrix.addToMatrix(scoreArray[i]);
		}

		// Once all files are imported, we need to ensure the probabilities of
		// the matrix are up to date
		noteMatrix.updateProbabilities();
	}

	/*
	 * Wrapper method for fetching the total number of pitches. Used when
	 * creating the note matrix table in MatrixPanel.
	 */
	public static int getNumOfPitches() {
		return noteMatrix.getNumOfPitches();
	}

	/*
	 * Wrapper method for accessing the note matrix. Returns a vector that is
	 * used to create a row in the matrix table. Row fetched corresponds to
	 * chain [note1][note2].
	 */
	public static Vector getNoteRow(int note1, int note2) {
		Vector prob = new Vector();

		// Fetches row of probabilities from the note matrix for chain
		// note1, note2
		prob = noteMatrix.getProbabilityArray(note1, note2);

		return prob;
	}

	/*
	 * Wrapper method for editing the probabilities of the matrix. Takes the new
	 * value of the probability, as well as the row and column of the value
	 * edited. The row and column values are needed so that we can fetch the
	 * chain of the probability edited.
	 */
	public static void setNewProbability(double newProb, int row, int col) {
		// Gets the note sequence of the value the user attempted to edit. The
		// note sequence is an int[3] consisting of the two previous notes and
		// the event we're editing (E.g. {60, 63, 55} would be editing the
		// probability of 60, 63 being followed by 55.
		int[] noteSequence = importPanel.getNoteSequence(row, col);

		// Calls the method to update the probability of the cell selected
		noteMatrix.setNewProbability(noteSequence[0], noteSequence[1], noteSequence[2], newProb);

		// Calls a custom repaint method for the table
		importPanel.repaintRow(row);
	}

	/*
	 * Method used to set the values of the first two notes of the newly
	 * generated score. This is set by ScorePanel by users.
	 */
	public static void setFirstNotes(int first, int second) {
		FIRST_NOTE = first;
		SECOND_NOTE = second;
	}

	/*
	 * Wrapper method used to return the total number of possibilities for the
	 * given chain note1, note2 and returns it.
	 */
	public static int getNumOfNotesForChain(int note1, int note2) {
		int total = noteMatrix.getNumOfNotesForChain(note1, note2);
		return total;
	}

	/*
	 * Utility method for cleaning up Java Swing code. Creates a new
	 * GridBagConstraints object using an X and Y grid location, grid width and
	 * grid height, a constant option for filling (e.g. fill vertical or
	 * horizontal) and an insets object for declaring how much space the object
	 * should have.
	 */
	public static GridBagConstraints generateNewGridBag(int xLoc, int yLoc, int width, int height, int fill,
			Insets insets) {
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = xLoc;
		c.gridy = yLoc;
		c.gridwidth = width;
		c.gridheight = height;
		c.fill = fill;
		c.insets = insets;
		c.weightx = 1;
		return c;
	}

}
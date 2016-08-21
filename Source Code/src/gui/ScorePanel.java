package gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import data.Instrument;
import jm.music.data.Score;
import jm.util.View;

/**
 * @author Benjamin Henshall
 *
 *         Class used to display, manager and generate new scores.
 */
public class ScorePanel extends JPanel {
	// Array of instrument objects, which store a constant (int) and the name.
	// Used to populate a combo box
	Instrument[] instrumentList = new Instrument[117];

	// Label to display how many times the chain selected has been found in the
	// imported scores
	JLabel totalLabel;

	// Comboboxes for changing the value of the first two notes of the new score
	// and labels for them
	JComboBox firstNote1;
	JComboBox firstNote2;
	JLabel firstNote1Label;
	JLabel firstNote2Label;

	// Combobox for changing which instrument is used
	JComboBox instrumentCombo;

	// Buttons for generating a new score, randomising parameters, going back
	// to the import panel and viewing the newly generated score
	JButton newScoreButton;
	JButton randomButton;
	JButton backButton;
	JButton viewScoreButton;

	// Text field used to store the tempo of the new score and a label for it
	JTextField tempoField;
	JLabel tempoLabel;

	// Text field used to store the composition of the new score and a label for
	// it
	JTextField compositionField;
	JLabel compLabel;

	// Frame which contains the score manager
	JFrame frame;

	// ImportPanel used in order to revert back tot he import panel
	ImportPanel importPanel;

	// Score used to store the generated score
	Score score;

	public ScorePanel(JFrame j, ImportPanel importPan) {
		importPanel = importPan;

		// Calls to fill the array with the list of instruments
		fillInstrumentList();

		frame = j;
		setLayout(new GridBagLayout());

		// Creates a new song using the default settings
		score = MainFrame.createSong();

		// Gets a new GridBagConstraints for the total label, centres the label
		// then creates and adds the label to the JPanel. It is programmatically
		// given a string later.
		GridBagConstraints c = MainFrame.generateNewGridBag(0, 2, 4, 1, GridBagConstraints.HORIZONTAL,
				new Insets(30, 40, 0, 120));
		totalLabel = new JLabel("", SwingConstants.CENTER);
		add(totalLabel, c);

		// Gets a new GridBagConstraints for the first note1 label, then creates
		// and adds the label to the JPanel.
		c = MainFrame.generateNewGridBag(0, 1, 1, 1, GridBagConstraints.HORIZONTAL, new Insets(30, 40, 0, 0));
		firstNote1Label = new JLabel("First Note: ");
		add(firstNote1Label, c);

		// Gets a new GridBagConstraints for the first note1 combo box, then
		// creates and adds it to the JPanel.
		c = MainFrame.generateNewGridBag(1, 1, 1, 1, GridBagConstraints.HORIZONTAL, new Insets(30, 40, 0, 120));
		firstNote1 = new JComboBox();
		// Loops through the number of pitches, adding each pitch to the combo
		// box
		for (int i = 0; i < MainFrame.getNumOfPitches(); i++) {
			firstNote1.addItem(i);
		}
		add(firstNote1, c);

		// Gets a new GridBagConstraints for the second note1 label, then
		// creates and adds the label to the JPanel.
		c = MainFrame.generateNewGridBag(2, 1, 1, 1, GridBagConstraints.HORIZONTAL, new Insets(30, 40, 0, 0));
		firstNote2Label = new JLabel("Second Note: ");
		add(firstNote2Label, c);

		// Gets a new GridBagConstraints for the first note2 combo box, then
		// creates and adds it to the JPanel.
		c = MainFrame.generateNewGridBag(3, 1, 1, 1, GridBagConstraints.HORIZONTAL, new Insets(30, 40, 0, 120));
		firstNote2 = new JComboBox();
		// Loops through the number of pitches, adding each pitch to the combo
		// box
		for (int i = 0; i < MainFrame.getNumOfPitches(); i++) {
			firstNote2.addItem(i);
		}
		add(firstNote2, c);

		// Gets a new GridBagConstraints for the instrument combo box, then
		// creates and adds it to the JPanel.
		c = MainFrame.generateNewGridBag(4, 2, 2, 1, GridBagConstraints.HORIZONTAL, new Insets(30, 40, 0, 120));
		instrumentCombo = new JComboBox();
		// Loops through the instrument list array, adding each instruments name
		// to the combo box
		for (int i = 0; i < instrumentList.length; i++) {
			instrumentCombo.addItem(instrumentList[i].getName());
		}
		// Sets the instrument combo selection to the default instrument
		instrumentCombo.setSelectedIndex(MainFrame.DEFAULT_INST);
		add(instrumentCombo, c);

		// Gets a new GridBagConstraints for the new score button, then creates
		// and adds the button to the JPanel.
		c = MainFrame.generateNewGridBag(0, 0, 2, 1, GridBagConstraints.HORIZONTAL, new Insets(30, 40, 0, 120));
		newScoreButton = new JButton("Generate New Score");
		add(newScoreButton, c);

		// Gets a new GridBagConstraints for the random button, then creates
		// and adds the button to the JPanel.
		c = MainFrame.generateNewGridBag(2, 0, 2, 1, GridBagConstraints.HORIZONTAL, new Insets(30, 40, 0, 120));
		randomButton = new JButton("Randomize Score");
		add(randomButton, c);

		// Gets a new GridBagConstraints for the back button, then creates
		// and adds the button to the JPanel.
		c = MainFrame.generateNewGridBag(4, 3, 2, 1, GridBagConstraints.HORIZONTAL, new Insets(30, 40, 0, 120));
		backButton = new JButton("Back");
		add(backButton, c);

		// Gets a new GridBagConstraints for the tempo field, then creates
		// and adds it to the JPanel.
		c = MainFrame.generateNewGridBag(5, 1, 1, 1, GridBagConstraints.HORIZONTAL, new Insets(30, 0, 0, 120));
		tempoField = new JTextField();
		// Sets the tempo field to the default value of 60
		tempoField.setText("60");
		add(tempoField, c);

		// Gets a new GridBagConstraints for the tempo label, then creates
		// and adds the label to the JPanel.
		c = MainFrame.generateNewGridBag(4, 1, 1, 1, GridBagConstraints.HORIZONTAL, new Insets(30, 40, 0, 0));
		tempoLabel = new JLabel("Tempo: ");
		add(tempoLabel, c);

		// Gets a new GridBagConstraints for the composition field, then creates
		// and adds the field to the JPanel.
		c = MainFrame.generateNewGridBag(1, 3, 3, 1, GridBagConstraints.HORIZONTAL, new Insets(30, 10, 0, 120));
		compositionField = new JTextField();
		// Sets the composition field to the default value
		compositionField.setText(MainFrame.DEFAULT_COMP);
		add(compositionField, c);

		// Gets a new GridBagConstraints for the composition label, then creates
		// and adds the label to the JPanel.
		c = MainFrame.generateNewGridBag(0, 3, 1, 1, GridBagConstraints.HORIZONTAL, new Insets(30, 40, 0, 10));
		compLabel = new JLabel("Composition: ");
		add(compLabel, c);

		// Gets a new GridBagConstraints for the view score button, then creates
		// and adds the button to the JPanel.
		c = MainFrame.generateNewGridBag(4, 0, 2, 1, GridBagConstraints.HORIZONTAL, new Insets(30, 40, 0, 120));
		viewScoreButton = new JButton("View/Play Score");
		add(viewScoreButton, c);

		// Sets up the action listeners for combo boxes/buttons
		setUpActionListeners();

		// Sets the first note combo boxes to the default, equal to notes in the
		// score generated
		firstNote1.setSelectedIndex(score.getPart(0).getPhrase(0).getNote(0).getPitch());
		firstNote2.setSelectedIndex(score.getPart(0).getPhrase(0).getNote(1).getPitch());

		// Then update the MainFrames first notes variables to reflect the
		// change
		MainFrame.setFirstNotes(firstNote1.getSelectedIndex(), firstNote2.getSelectedIndex());
	}

	/*
	 * Method for setting up action listeners for UI components
	 */
	public void setUpActionListeners() {
		// Action listener for back button
		backButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Hides this panel, sets the old import panel as the main
				// content pane and makes it visible.
				setVisible(false);
				frame.setContentPane(importPanel);
				importPanel.setVisible(true);
			}
		});

		// Action listener for view score button
		viewScoreButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Check to make sure the text inside the tempo field is an
				// integer
				if (!isNumeric(tempoField.getText())) {
					// If it's not, open an error dialog
					JOptionPane.showMessageDialog(ScorePanel.this, "Tempo must be a whole number between 50 and 200.",
							"Input Error", JOptionPane.ERROR_MESSAGE);
					// And reset the value to default
					tempoField.setText("60");
					score.setTempo(Integer.parseInt(tempoField.getText()));
				} else if (!(Integer.parseInt(tempoField.getText()) > 49
						&& Integer.parseInt(tempoField.getText()) < 201)) {
					// Else if score is too low (<50) or too high (>200) then
					// open an error dialog
					JOptionPane.showMessageDialog(ScorePanel.this, "Tempo must be a whole number between 50 and 200",
							"Input Error", JOptionPane.ERROR_MESSAGE);
					// And reset the value to default
					tempoField.setText("60");
					score.setTempo(Integer.parseInt(tempoField.getText()));
				} else {
					// Else set the score to the new tempo
					score.setTempo(Integer.parseInt(tempoField.getText()));
					// And show the score using the jMusic command. Opens
					// another jMusic window that displays the score and allows
					// users to play/save the score.
					View.show(score);
				}
			}
		});

		// Action listener for generating a new score
		newScoreButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Check to make sure the composition field is in the English
				// alphabet with no symbols
				if (!isAlphabetical(compositionField.getText())) {
					// If not, throw an error dialog
					JOptionPane.showMessageDialog(ScorePanel.this, "Composition can only contain letters.",
							"Input Error", JOptionPane.ERROR_MESSAGE);
					// And reset the composition field to default
					compositionField.setText(MainFrame.DEFAULT_COMP);
				} else {
					// Else the composition field is valid

					// Update the first notes for generating a score from the
					// note matrix
					MainFrame.setFirstNotes(firstNote1.getSelectedIndex(), firstNote2.getSelectedIndex());
					// And update the pattern being used to generate the score
					MainFrame.setGlobalPattern(compositionField.getText());
					// Then create a new score
					score = MainFrame.createSong();
					// And finally, update the instrument of the score to match
					// the combo box
					changeInstrument();
				}
			}
		});

		// ActionListener for updating the instrument
		instrumentCombo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// When clicked, simply changes the instrument to the instrument
				// selected
				changeInstrument();
			}
		});

		// Action listener for the first note combo boxes
		ActionListener firstNotesListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// If a value is changed, updated the label to reflect the
				// correct amount of notes found for the chain
				totalLabel.setText("Total count for chain = " + MainFrame
						.getNumOfNotesForChain(firstNote1.getSelectedIndex(), firstNote2.getSelectedIndex()));
			}
		};
		// Adds the listener to both first note combo boxes
		firstNote1.addActionListener(firstNotesListener);
		firstNote2.addActionListener(firstNotesListener);

		// Action listener for the randomise button
		randomButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Generates a new random value between 0 and the number of
				// instruments in the list
				int rand = (int) (Math.random() * instrumentList.length);
				// Then sets the combo box to the instrument we randomised
				instrumentCombo.setSelectedIndex(rand);

				// Generates a new random number between 50 and 200, for the
				// tempo
				rand = (int) (50 + Math.random() * (200 - 50));
				// Then updates the tempo text box with the randomised value
				tempoField.setText(Integer.toString(rand));

				// Boolean used to exit loop when we have found a chain of notes
				// that have at least one event outcome
				boolean found = false;

				// Loop that repeats until we have found a suitable chain
				while (!found) {
					// Generates a new random number within the range of 0 to
					// maximum pitch in imports
					rand = (int) (Math.random() * MainFrame.getNumOfPitches());
					// Creates a new arraylist which is used to store all the
					// pitches that we haven't checked in this iteration of the
					// loop.
					ArrayList possibleNotes = new ArrayList(MainFrame.getNumOfPitches());
					// Fills the loop with the pitches
					for (int i = 0; i < MainFrame.getNumOfPitches(); i++) {
						possibleNotes.add(i);
					}

					// Loop that repeats until we have found a suitable match or
					// until we have processed the arraylist of all pitches. If
					// we do not find a suitable pairing, then we repeat the
					// while (!found) loop, where a new first number is
					// generated and the cycle is repeated.
					for (int i = 0; i < possibleNotes.size(); i++) {
						// Generates a new random number between 0 and the
						// current size of the possible notes array. This size
						// decreases over every iteration of the for loop
						int secondRand = (int) (Math.random() * possibleNotes.size());
						// If the sequence of notes has at least one note it may
						// generate then we have found a valid chain
						if (MainFrame.getNumOfNotesForChain(rand, secondRand) > 0) {
							// So update the combo boxes to show the new numbers
							firstNote1.setSelectedIndex(rand);
							firstNote2.setSelectedIndex(secondRand);
							// And mark found as true so we can exit the outer
							// loop
							found = true;
							// and break out of the current for loop
							break;
						} else {
							// Else, remove the note from the array list and try
							// generating a new one
							possibleNotes.remove(secondRand);
						}
					}
				}
				// Set the composition field to a new random string
				compositionField.setText(generateNewPattern());
				// Ensures a new score is generated with the new randomised
				// parameters
				newScoreButton.doClick();
				// And updates the instrument to the one in the combo box
				changeInstrument();
			}
		});
	}

	// Taken from
	// https://rosettacode.org/wiki/Determine_if_a_string_is_numeric#Java
	public boolean isNumeric(String input) {
		// Tries to parse the string as an int. If the string is not a valid
		// value for an integer, then it throws an exception which returns
		// false. Else, it is a valid integer and returns true.
		try {
			Integer.parseInt(input);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	/*
	 * Method for determining if a string is composed of only letters from the
	 * English alphabet (Uppercase or Lowercase)
	 */
	public boolean isAlphabetical(String input) {
		// Converts the string into a char array sow e can compare ASCII values
		char[] chars = input.toCharArray();

		// Loops over each of the chars found in the string and compares ASCII
		// values to see if they are valid
		for (int i = 0; i < chars.length; i++) {
			// If char is outside of a lowercase or uppercase English alphabet
			// letter then return false
			if (chars[i] < 65 || (chars[i] > 90 && chars[i] < 97) || chars[i] > 122) {
				return false;
			}
		}
		// Else return true
		return true;
	}

	/*
	 * Method for changing the instrument of the score
	 */
	public void changeInstrument() {
		// Sets the instrument of the first part in score to the value in the
		// combo box. The score only has one part, so this affects the whole
		// score.
		score.getPart(0).setInstrument(instrumentList[instrumentCombo.getSelectedIndex()].getValue());
	}

	/*
	 * Method for generating a new random string for use in the composition
	 * field
	 */
	public String generateNewPattern() {
		// String used to store the new pattern we have generated
		String newPattern = "";
		// Generates a new random number between 5 and 13, which will be used as
		// the length of our new pattern
		int randLength = (int) (5 + Math.random() * (13 - 5));

		// Char array used to store the individual characters of our new
		// composition
		char[] patternChars = new char[randLength];

		// Loops through, adding a new random English alphabet letter for each
		// loop
		for (int i = 0; i < randLength; i++) {
			// Generates a random number between 65 and 90 (Between a and z in
			// ASCII values)
			char randPhrase = (char) (65 + Math.random() * (90 - 65));
			// Then assigns it to the ith element in our char array
			patternChars[i] = randPhrase;
		}
		// Translates the char array into a string
		newPattern = String.valueOf(patternChars);
		// Then returns the string
		return newPattern;
	}

	/*
	 * Method for filling the instrumentList array with instruments. Due to
	 * there being gaps in the values of the jMusic constants, I couldn't do
	 * this so the selected index represented the value of the instrument. There
	 * is no jMusic instrument constant for int 2, so if the user had selected
	 * index 2 in the combo box, it would cause a run time error in jMusic. So
	 * instead, we have to have a large amount of lines that mnaully add to the
	 * array.
	 */
	public void fillInstrumentList() {
		instrumentList[0] = new Instrument(0, "PIANO");
		instrumentList[1] = new Instrument(3, "HONKYTONK");
		instrumentList[2] = new Instrument(4, "EPIANO");
		instrumentList[3] = new Instrument(5, "EPIANO2");
		instrumentList[4] = new Instrument(6, "HARPSICHORD");
		instrumentList[5] = new Instrument(7, "CLAV");
		instrumentList[6] = new Instrument(8, "CELESTE");
		instrumentList[7] = new Instrument(9, "GLOCKENSPIEL");
		instrumentList[8] = new Instrument(10, "MUSIC_BOX");
		instrumentList[9] = new Instrument(11, "VIBRAPHONE");
		instrumentList[10] = new Instrument(12, "MARIMBA");
		instrumentList[11] = new Instrument(13, "XYLOPHONE");
		instrumentList[12] = new Instrument(14, "TUBULAR_BELL");
		instrumentList[13] = new Instrument(16, "ORGAN");
		instrumentList[14] = new Instrument(17, "ORGAN2");
		instrumentList[15] = new Instrument(18, "ORGAN3");
		instrumentList[16] = new Instrument(19, "CHURCH_ORGAN");
		instrumentList[17] = new Instrument(20, "REED_ORGAN");
		instrumentList[18] = new Instrument(21, "ACCORDION");
		instrumentList[19] = new Instrument(22, "HARMONICA");
		instrumentList[20] = new Instrument(23, "BANDEON");
		instrumentList[21] = new Instrument(24, "NYLON_GUITAR");
		instrumentList[22] = new Instrument(25, "STEEL_GUITAR");
		instrumentList[23] = new Instrument(26, "JAZZ_GUITAR");
		instrumentList[24] = new Instrument(27, "CLEAN_GUITAR");
		instrumentList[25] = new Instrument(28, "MUTED_GUITAR");
		instrumentList[26] = new Instrument(29, "OVERDRIVE_GUITAR");
		instrumentList[27] = new Instrument(30, "DISTORTED_GUITAR");
		instrumentList[28] = new Instrument(31, "GUITAR_HARMONICS");
		instrumentList[29] = new Instrument(32, "ACOUSTIC_BASS");
		instrumentList[30] = new Instrument(33, "FINGERED_BASS");
		instrumentList[31] = new Instrument(34, "PICKED_BASS");
		instrumentList[32] = new Instrument(35, "FRETLESS_BASS");
		instrumentList[33] = new Instrument(36, "SLAP_BASS");
		instrumentList[34] = new Instrument(38, "SYNTH_BASS");
		instrumentList[35] = new Instrument(40, "VIOLIN");
		instrumentList[36] = new Instrument(41, "VIOLA");
		instrumentList[37] = new Instrument(42, "CELLO");
		instrumentList[38] = new Instrument(43, "CONTRABASS");
		instrumentList[39] = new Instrument(44, "TREMOLO_STRINGS");
		instrumentList[40] = new Instrument(45, "PIZZICATO_STRINGS");
		instrumentList[41] = new Instrument(46, "HARP");
		instrumentList[42] = new Instrument(47, "TIMPANI");
		instrumentList[43] = new Instrument(48, "STRINGS");
		instrumentList[44] = new Instrument(51, "SLOW_STRINGS");
		instrumentList[45] = new Instrument(50, "SYNTH_STRINGS");
		instrumentList[46] = new Instrument(52, "AHH");
		instrumentList[47] = new Instrument(53, "OOH");
		instrumentList[48] = new Instrument(54, "SYNVOX");
		instrumentList[49] = new Instrument(55, "ORCHESTRA_HIT");
		instrumentList[50] = new Instrument(56, "TRUMPET");
		instrumentList[51] = new Instrument(57, "TROMBONE");
		instrumentList[52] = new Instrument(58, "TUBA");
		instrumentList[53] = new Instrument(59, "MUTED_TRUMPET");
		instrumentList[54] = new Instrument(60, "FRENCH_HORN");
		instrumentList[55] = new Instrument(61, "BRASS");
		instrumentList[56] = new Instrument(62, "SYNTH_BRASS");
		instrumentList[57] = new Instrument(64, "SOPRANO_SAX");
		instrumentList[58] = new Instrument(65, "ALTO_SAX");
		instrumentList[59] = new Instrument(66, "TENOR_SAX");
		instrumentList[60] = new Instrument(67, "BARITONE_SAX");
		instrumentList[61] = new Instrument(68, "OBOE");
		instrumentList[62] = new Instrument(69, "ENGLISH_HORN");
		instrumentList[63] = new Instrument(70, "BASSOON");
		instrumentList[64] = new Instrument(71, "CLARINET");
		instrumentList[65] = new Instrument(72, "PICCOLO");
		instrumentList[66] = new Instrument(73, "FLUTE");
		instrumentList[67] = new Instrument(74, "RECORDER");
		instrumentList[68] = new Instrument(75, "PAN_FLUTE");
		instrumentList[69] = new Instrument(76, "BOTTLE_BLOW");
		instrumentList[70] = new Instrument(77, "SHAKUHACHI");
		instrumentList[71] = new Instrument(78, "WHISTLE");
		instrumentList[72] = new Instrument(79, "OCARINA");
		instrumentList[73] = new Instrument(80, "SQUARE_WAVE");
		instrumentList[74] = new Instrument(81, "SAW_WAVE");
		instrumentList[75] = new Instrument(81, "SYNTH_CALLIOPE");
		instrumentList[76] = new Instrument(83, "CHIFFER_LEAD");
		instrumentList[77] = new Instrument(84, "CHARANG");
		instrumentList[78] = new Instrument(85, "SOLO_VOX");
		instrumentList[79] = new Instrument(88, "FANTASIA");
		instrumentList[80] = new Instrument(89, "WARM_PAD");
		instrumentList[81] = new Instrument(90, "POLYSYNTH");
		instrumentList[82] = new Instrument(91, "SPACE_VOICE");
		instrumentList[83] = new Instrument(92, "BOWED_GLASS");
		instrumentList[84] = new Instrument(93, "METAL_PAD");
		instrumentList[85] = new Instrument(94, "HALO_PAD");
		instrumentList[86] = new Instrument(95, "SWEEP_PAD");
		instrumentList[87] = new Instrument(96, "ICE_RAIN");
		instrumentList[88] = new Instrument(97, "SOUNDTRACK");
		instrumentList[89] = new Instrument(98, "CRYSTAL");
		instrumentList[90] = new Instrument(99, "ATMOSPHERE");
		instrumentList[91] = new Instrument(100, "BRIGHTNESS");
		instrumentList[92] = new Instrument(101, "GOBLIN");
		instrumentList[93] = new Instrument(102, "ECHO_DROPS");
		instrumentList[94] = new Instrument(103, "STAR_THEME");
		instrumentList[95] = new Instrument(104, "SITAR");
		instrumentList[96] = new Instrument(105, "BANJO");
		instrumentList[97] = new Instrument(106, "SHAMISEN");
		instrumentList[98] = new Instrument(107, "KOTO");
		instrumentList[99] = new Instrument(108, "KALIMBA");
		instrumentList[100] = new Instrument(109, "BAGPIPES");
		instrumentList[101] = new Instrument(110, "FIDDLE");
		instrumentList[102] = new Instrument(111, "SHANNAI");
		instrumentList[103] = new Instrument(112, "TINKLE_BELL");
		instrumentList[104] = new Instrument(113, "AGOGO");
		instrumentList[105] = new Instrument(114, "STEEL_DRUMS");
		instrumentList[106] = new Instrument(115, "WOODBLOCK");
		instrumentList[107] = new Instrument(116, "TAIKO");
		instrumentList[108] = new Instrument(118, "SYNTH_DRUM");
		instrumentList[109] = new Instrument(119, "TOMS");
		instrumentList[110] = new Instrument(120, "FRETNOISE");
		instrumentList[111] = new Instrument(121, "BREATHNOISE");
		instrumentList[112] = new Instrument(122, "SEAHORSE");
		instrumentList[113] = new Instrument(123, "BIRD");
		instrumentList[114] = new Instrument(124, "TELEPHONE");
		instrumentList[115] = new Instrument(125, "HELICOPTER");
		instrumentList[116] = new Instrument(126, "APPLAUSE");
	}

}

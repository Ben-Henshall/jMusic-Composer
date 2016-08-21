package gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/**
 * @author Benjamin Henshall
 * 
 *         This class is used to display the panel where users can import MIDI
 *         files and also acts as a navigation centre to switch to the matrix
 *         view and score manager view.
 *
 */
public class ImportPanel extends JPanel {

	// Column names used when created the import table
	private final String[] columnNames = { "File Name", "Path" };

	// JFrame used to display the GUI
	private JFrame frame;

	// JTable used to display which files have been imported
	private JTable table;

	// ScrollPane which table is stored in
	private JScrollPane scrollPane;

	// Buttons used in interface
	private JButton removeButton;
	private JButton importButton;
	private JButton generateButton;
	private JButton infoButton;
	private JButton viewMatrixButton;

	// MatrixPanel panel used when the user wants to view the note matrix
	private MatrixPanel matrixPanel;

	// ScorePanel panel used when the user wants to view generate/edit a score
	private ScorePanel scorePanel;

	// FileChooser for importing files into the program
	private JFileChooser fc;

	// String to store the last location the user has visited using the
	// FileChooser. Defaults as the base directory of the .jar file.
	private String previousImportLoc = System.getProperty("user.dir");

	public ImportPanel(JFrame j) {
		frame = j;

		// ImportPanel uses a GridBagLayout in order to display the GUI
		// components
		setLayout(new GridBagLayout());

		// Instantiates the FileChooser
		fc = new JFileChooser();

		// Gets a new GridBagConstraints for the import button, then creates and
		// adds the button to the JPanel.
		GridBagConstraints c = MainFrame.generateNewGridBag(0, 3, 1, 1, GridBagConstraints.HORIZONTAL,
				new Insets(30, 40, 0, 40));
		importButton = new JButton("Import");
		add(importButton, c);

		// Gets a new GridBagConstraints for the remove button, then creates and
		// adds the button to the JPanel.
		c = MainFrame.generateNewGridBag(1, 3, 1, 1, GridBagConstraints.HORIZONTAL, new Insets(30, 0, 0, 40));
		removeButton = new JButton("Remove Import");
		add(removeButton, c);
		// Sets the remove button to disabled as default (Due to no files being
		// imported, therefore you cannot remove any currently.
		removeButton.setEnabled(false);

		// Gets a new GridBagConstraints for the generate button, then creates
		// and adds the button to the JPanel.
		c = MainFrame.generateNewGridBag(3, 3, 2, 1, GridBagConstraints.HORIZONTAL, new Insets(30, 40, 0, 20));
		generateButton = new JButton("Generate");
		add(generateButton, c);

		// Gets a new GridBagConstraints for the info button, then creates and
		// adds the button to the JPanel.
		c = MainFrame.generateNewGridBag(3, 0, 1, 1, GridBagConstraints.HORIZONTAL, new Insets(0, 40, 0, 40));
		infoButton = new JButton("Info");
		add(infoButton, c);

		// Gets a new GridBagConstraints for the view matrix button, then
		// creates and adds the button to the JPanel.
		c = MainFrame.generateNewGridBag(4, 0, 1, 1, GridBagConstraints.HORIZONTAL, new Insets(0, 40, 0, 40));
		viewMatrixButton = new JButton("View Matrix");
		add(viewMatrixButton, c);

		// Gets a new GridBagConstraints for the table
		c = MainFrame.generateNewGridBag(0, 0, 2, 2, GridBagConstraints.HORIZONTAL, new Insets(0, 40, 0, 0));
		// Creates a new JTable using a custom DefaultTableModel with null data
		// and specified column names
		table = new JTable(new UneditableDTM(null, columnNames));
		// Sets the table to use a custom selection model which prevents users
		// from being able to select more than one entry
		table.removeColumn(table.getColumnModel().getColumn(1));

		// Create the scroll pane and add the table to it.
		scrollPane = new JScrollPane(table);

		// Add the scroll pane to this panel.
		add(scrollPane, c);

		// Sets up all the action listeners for the buttons
		setUpListeners();
	}

	public void setUpListeners() {
		// Action listener for import button
		importButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Sets the current directory to the location last visited (Or
				// the default)
				fc.setCurrentDirectory(new File(previousImportLoc));

				// Allows multiple files to be imported at once
				fc.setMultiSelectionEnabled(true);

				// Opens the fileChooser
				int returnVal = fc.showOpenDialog(ImportPanel.this);

				// If user successfully selects a file
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					// Gets the collection of files selected by the user
					File[] selectedFiles = fc.getSelectedFiles();
					// Adds the files to the list to import
					addToImports(selectedFiles);
					// Resets the table selection to the first item in the list
					table.getSelectionModel().setSelectionInterval(0, 0);
					// Then updates the matrix so that probabilities are up to
					// date
					updateMatrix();
				}
				// Updates the directory variable to the directory which the
				// user last visited
				previousImportLoc = fc.getCurrentDirectory().getAbsolutePath();
			}
		});

		// Action listener for generate score button
		generateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Check if user has imported any files yet
				if (table.getRowCount() != 0) {
					// Instantiates the ScorePanel panel
					scorePanel = new ScorePanel(frame, ImportPanel.this);
					// Sets it to visible
					scorePanel.setVisible(true);
					// Hides the current panel
					setVisible(false);
					// Then sets the ScorePanel panel to the main content pane
					// of the frame
					frame.setContentPane(scorePanel);
				} else {
					// If they haven't, then throw an error dialog telling them
					// they need to import.
					JOptionPane.showMessageDialog(ImportPanel.this, "Please import at least one file.",
							"No imports found.", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		// Action listener for the info button
		infoButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Creates a new AboutDialog and sets it to visible
				AboutDialog aboutDialog = new AboutDialog(frame);
				aboutDialog.setVisible(true);
			}
		});

		// Action listener for view matrix button
		viewMatrixButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Check if the user has imported any files yet
				if (table.getRowCount() != 0) {
					// Instantiates the matrix panel
					matrixPanel = new MatrixPanel(frame, ImportPanel.this);
					// Sets location of the new panel to the middle of the
					// screen
					matrixPanel.setLocation((java.awt.Toolkit.getDefaultToolkit().getScreenSize().width / 2) - 600,
							(java.awt.Toolkit.getDefaultToolkit().getScreenSize().height / 2) - 300);

					// Sets the new panel to visible and hides the import panel
					matrixPanel.setVisible(true);
					setVisible(false);
					// Sets the matrix panel as the main content pane of the
					// frame
					frame.setContentPane(matrixPanel);

					// Maximises the window for easier viewing
					frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
				} else {
					// If not, show an error dialog
					JOptionPane.showMessageDialog(ImportPanel.this, "Please import at least one file.",
							"No imports found.", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		// Action listener for remove button
		removeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Gets the model of the import table
				UneditableDTM tempModel = (UneditableDTM) table.getModel();

				// Gets the rows the user has selected
				int[] rowsSelected = table.getSelectedRows();

				// Loops through the rows selected and removes them. Removes
				// row[i]-i because remaining rows are shifted down each time a
				// row is removed.
				for (int i = 0; i < rowsSelected.length; i++) {
					tempModel.removeRow(rowsSelected[i] - i);
				}

				// If there's no imports left in the table, then disable the
				// remove button.
				if (tempModel.getRowCount() == 0) {
					removeButton.setEnabled(false);
				} else {
					// Else set the selection to the first element in the table
					table.getSelectionModel().setSelectionInterval(0, 0);
				}
			}
		});
	}

	/*
	 * Method for adding imported files to the matrix
	 */
	public void addToImports(File[] importFiles) {
		// Loops over all files given
		for (int i = 0; i < importFiles.length; i++) {
			// Check if file is MIDI. Needs two ways of checking due to
			// differences in file systems.
			if (fc.getTypeDescription(importFiles[i]).contains(".mid")
					|| fc.getTypeDescription(importFiles[i]).contains("MIDI")) {

				// Gets model of table
				UneditableDTM model = (UneditableDTM) table.getModel();

				// Gets name of file being imported
				String fileName = importFiles[i].getName();

				// Boolean used to see if the file already exists in the table
				boolean foundDuplicate = false;

				// Loops through all files in the import table checking for
				// duplicates
				for (int j = 0; j < model.getRowCount(); j++) {
					if (importFiles[i].getAbsolutePath().equals(model.getValueAt(j, 1))) {
						foundDuplicate = true;
					}
				}
				// If no duplicate is found, then add the new entry to the model
				if (!foundDuplicate) {
					model.addRow(new Object[] { fileName, importFiles[i].getAbsolutePath() });
				}

				// Then update the table with the modified model and
				// repaint/revalidate
				table.setModel(model);
				repaint();
				revalidate();
			} else {
				// If file is not a MIDI file, open a new error dialog and break
				// the loop.
				JOptionPane.showMessageDialog(ImportPanel.this,
						"Non-MIDI file detected. Please select a valid MIDI (.mid) file.", "Invalid File.",
						JOptionPane.ERROR_MESSAGE);
				break;
			}
		}
		// If something is successfully imported, enable the remove button.
		if (table.getRowCount() > 0) {
			removeButton.setEnabled(true);
		}
	}

	/*
	 * Method for updating the matrix using the files in the import table
	 */
	public void updateMatrix() {
		// Creates a new String array to store the file paths in
		String[] files = new String[table.getRowCount()];

		// Gets the model of the import table
		UneditableDTM model = (UneditableDTM) table.getModel();

		// Loops through the table, filling the string array with the paths of
		// the files
		for (int i = 0; i < table.getRowCount(); i++) {
			files[i] = (String) model.getValueAt(i, 1);
		}

		// Then adds them to the matrix
		MainFrame.addToMatrix(files);
	}

	/*
	 * Wrapper method for getting a sequence of three notes
	 */
	public int[] getNoteSequence(int row, int col) {
		int[] noteSequence = matrixPanel.getNoteSequence(row, col);
		return noteSequence;
	}

	/*
	 * Wrapper method for custom repainting of the matrix table
	 */
	public void repaintRow(int row) {
		matrixPanel.repaintRow(row);
	}
}

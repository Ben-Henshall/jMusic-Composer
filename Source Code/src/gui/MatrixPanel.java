package gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 * @author Benjamin Henshall
 *
 *         The MatrixPanel class is used to display the matrix in a table.
 */
public class MatrixPanel extends JPanel {

	// Variable used to store how far we're currently zoomed in
	double zoomFactor = 1;

	// Array storing all the possible values for zooming in
	double[] zoomArray = { 1, 1.5, 2, 2.5, 3, 3.5 };

	// Current state in zooming (Value represent which value in zoomArray we're
	// in)
	int zoomState = 0;

	// JFrame for window
	JFrame frame;

	// Buttons used in interface
	JButton backButton;
	JButton zoomInButton;
	JButton zoomOutButton;

	// Table storing matrix
	JTable table;

	// Table for storing note chain on the left, row header
	JTable headerTable;

	// Scroll pane containing table
	JScrollPane pane;

	// ImportPanel passed through constructor as we need to make it visible if
	// the user clicks back
	ImportPanel importPanel;

	public MatrixPanel(JFrame j, ImportPanel importPan) {
		importPanel = importPan;
		frame = j;
		setLayout(new GridBagLayout());

		// Gets a new GridBagConstraints for the back button, then creates and
		// adds the button to the JPanel.
		GridBagConstraints c = MainFrame.generateNewGridBag(4, 1, 1, 1, GridBagConstraints.HORIZONTAL,
				new Insets(20, 40, 20, 120));
		backButton = new JButton("Back");
		add(backButton, c);

		// Adds a hidden label for spacing reasons
		c = MainFrame.generateNewGridBag(3, 1, 1, 1, GridBagConstraints.HORIZONTAL, new Insets(20, 40, 20, 120));
		JLabel hiddenLabel = new JLabel("");
		add(hiddenLabel, c);

		// Gets a new GridBagConstraints for the zoom in button, then creates
		// and adds the button to the JPanel.
		c = MainFrame.generateNewGridBag(1, 1, 1, 1, GridBagConstraints.HORIZONTAL, new Insets(20, 10, 20, 10));
		zoomInButton = new JButton("Zoom In");
		add(zoomInButton, c);

		// Gets a new GridBagConstraints for the zoom out button, then creates
		// and adds the button to the JPanel.
		c = MainFrame.generateNewGridBag(2, 1, 1, 1, GridBagConstraints.HORIZONTAL, new Insets(20, 10, 20, 10));
		zoomOutButton = new JButton("Zoom Out");
		add(zoomOutButton, c);
		// Zoom out button disabled by default as we start zoomed out
		zoomOutButton.setEnabled(false);

		// Gets a new GridBagConstraints for the table
		c = MainFrame.generateNewGridBag(0, 0, 5, 1, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0));
		// Sets GridBagConstraints so the table has Y priority, allowing it to
		// take up all vertical space that is free.
		c.weighty = 1;

		// Calls to fill table and headerTable with data
		fillTable();

		// Ensures the table fills up all free space in the scroll pane
		table.setFillsViewportHeight(true);
		// Ensures the table rows/columns are sized how I specified
		table.setAutoResizeMode(0);

		// Create the scroll pane and add the table to it.
		JScrollPane scrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		// Creates a new dimension that has just enough width required to
		// display properly
		Dimension d = headerTable.getPreferredScrollableViewportSize();
		d.width = headerTable.getPreferredSize().width;
		// Sets headerTable as the row header for table
		scrollPane.setRowHeaderView(headerTable);
		// Add the scroll pane to this panel.
		add(scrollPane, c);

		setUpActionListeners();
	}

	public void setUpActionListeners() {
		// Action listener for Back button
		backButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Hides current panel, changes the content pane of the window
				// to import panel
				setVisible(false);
				frame.setContentPane(importPanel);
				// Makes the import panel visible, then sets the window back to
				// its normal size (Not maximised)
				importPanel.setVisible(true);
				frame.setExtendedState(JFrame.NORMAL);
			}
		});

		// Action listener for zoom in button
		zoomInButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Increases the zoom factor to the next largest amount
				zoomFactor = zoomArray[zoomState + 1];
				// Increase zoom state as we have increased zoom
				zoomState++;
				// Calls the resizeTable method in order to resize the row
				// height and column widths
				resizeTable((ValidatedTableModel) table.getModel());

				// Quick check to see if we should enable or disable any of the
				// buttons due to not being allowed to zoom in more
				if (zoomState >= 5) {
					zoomInButton.setEnabled(false);
				} else if (zoomState >= 1) {
					zoomOutButton.setEnabled(true);
				}
			}
		});

		// Action listener for zoom out button
		zoomOutButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Increases the zoom factor to the next largest amount
				zoomFactor = zoomArray[zoomState - 1];
				// Increase zoom state as we have increased zoom
				zoomState--;
				// Calls the resizeTable method in order to resize the row
				// height and column widths
				resizeTable((ValidatedTableModel) table.getModel());
				// Quick check to see if we should enable or disable any of the
				// buttons due to not being allowed to zoom in more
				if (zoomState <= 0) {
					zoomOutButton.setEnabled(false);
				} else if (zoomState <= 5) {
					zoomInButton.setEnabled(true);
				}
			}
		});
	}

	/*
	 * Method for filling the table with data. Includes configuring of the
	 * header table.
	 */
	public void fillTable() {
		// Creates a new model which will be filled with data from our matrix
		ValidatedTableModel model;
		// Creates a new model for the row header table with no data and 2
		// columns (note1, note2)
		DefaultTableModel headerModel = new DefaultTableModel(0, 2);

		// If we already have data in our tables model, then we should start
		// from this model in order to keep settings the same. Else, we just
		// instantiate it anew.
		if (table != null) {
			model = (ValidatedTableModel) table.getModel();
		} else {
			model = new ValidatedTableModel();
		}

		// Get number of pitches so we know how many columns and rows there
		// should be
		int numOfPitches = MainFrame.getNumOfPitches();

		// Loop that adds a new column for each pitch found
		for (int i = 0; i < numOfPitches; i++) {
			model.addColumn(i);
		}

		// Nested loops that create n^2 loops where n is the number of pitches.
		// This is a row for each possible combination/chain of notes.
		for (int i = 0; i < numOfPitches; i++) {
			for (int j = 0; j < numOfPitches; j++) {
				// Retrieves the probabilities for the row i, j
				Vector probs = MainFrame.getNoteRow(i, j);
				// Then adds to the model
				model.addRow(probs);
				// Adds a new row to the headerTable model, that simply has the
				// note sequence (i, j) over two cells
				headerModel.addRow(new Object[] { i, j });
			}
		}

		// Updates the table to use the new model
		table = new JTable(model);

		// Updates the header table to use the new model. Uses a custom model
		// that does not allow editing of the cell
		headerTable = new JTable(headerModel) {
			public boolean isCellEditable(int row, int column) {
				return false;
			};
		};

		// Sets the column sizes of the headerTable to a smaller amount, so
		// there's more space for the data
		headerTable.getColumnModel().getColumn(0).setPreferredWidth(30);
		headerTable.getColumnModel().getColumn(1).setPreferredWidth(30);

		// Resizes the table using the new model
		resizeTable(model);
	}

	/*
	 * Method for resizing the table. Uses zoom factor to determine how
	 * large/small the row height/column width should be
	 */
	public void resizeTable(ValidatedTableModel model) {
		// Sets the height of the table and header table
		table.setRowHeight(
				(int) ((java.awt.Toolkit.getDefaultToolkit().getScreenSize().height / MainFrame.getNumOfPitches() / 2)
						* zoomFactor));
		headerTable.setRowHeight(
				(int) ((java.awt.Toolkit.getDefaultToolkit().getScreenSize().height / MainFrame.getNumOfPitches() / 2)
						* zoomFactor));

		// Loops through every column in order to resize them
		for (int i = 0; i < model.getColumnCount(); i++) {
			// Sets each column to use a custom cell renderer, allowing cells to
			// be conditionally coloured dependent on their value
			table.getColumnModel().getColumn(i).setCellRenderer(new StatusCellRenderer());
			// Sets the width of the column in the main matrix table
			table.getColumnModel().getColumn(i).setPreferredWidth(
					(int) ((java.awt.Toolkit.getDefaultToolkit().getScreenSize().width / MainFrame.getNumOfPitches())
							* zoomFactor));
		}
	}

	/*
	 * Retrieves the note sequence when given a cell. E.G. note sequence (60,
	 * 53, 55) would be retrieved if the cell was in row (60, 53) under column
	 * 55.
	 */
	public int[] getNoteSequence(int row, int col) {
		int[] noteSequence = new int[3];
		// Parses the value of the two cells in headerTable so we know what the
		// chain is
		noteSequence[0] = Integer.parseInt(headerTable.getValueAt(row, 0).toString());
		noteSequence[1] = Integer.parseInt(headerTable.getValueAt(row, 1).toString());
		// And gets the pitch value we edited the probability for
		noteSequence[2] = col;
		return noteSequence;
	}

	/*
	 * Custom method for repainting a single row, rather than having to refill
	 * the whole table. We have to refresh a row whenever the user edits a cell,
	 * as the other probabilities change depending on the users edit
	 */
	public void repaintRow(int row) {
		// Gets the table model
		ValidatedTableModel model = (ValidatedTableModel) table.getModel();
		// Removes the row so we can add the newer one
		model.removeRow(row);

		// Gets the value for the chain
		int note1 = Integer.parseInt(headerTable.getValueAt(row, 0).toString());
		int note2 = Integer.parseInt(headerTable.getValueAt(row, 1).toString());
		// ...So we can get the row for that chain
		Vector probs = MainFrame.getNoteRow(note1, note2);

		// Then insert the updated row back where it was removed
		model.insertRow(row, probs);

	}
}

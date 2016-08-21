package gui;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 * @author Benjamin Henshall
 *
 *         Custom table model used for the matrix table. Prevents users from
 *         entering an incorrect entry into the table
 */
public class ValidatedTableModel extends DefaultTableModel {

	// Overides the setValueAt method in DefaultTableModel, which is used
	// whenever a change is attempted in the table
	@Override
	public void setValueAt(Object aValue, int row, int column) {
		// Try catch statement to check if the value the user entered is a
		// number.
		try {
			// Error is thrown here (But caught) if the user does not enter a
			// number.
			double prob = Double.parseDouble(aValue.toString());
			// If the probability is too large (More than 1, due to the sum
			// probability of a row having to equal 1) then we do nothing
			if (prob <= 1) {
				// But if it is equal to or below 1, then call the setValueAt
				// method in the DefaultTableModel
				super.setValueAt(prob, row, column);
				// Then updated the probability of the matrix to the new value
				// the user entered. This method also edits other probabilities
				// to keep the row sum at 1.
				MainFrame.setNewProbability(prob, row, column);
			}
		} catch (NumberFormatException E) {
			// Else user entered something that is not a number, so throw an
			// error dialog.
			JOptionPane.showMessageDialog(null, "Entry must be a number.", "Error.", JOptionPane.ERROR_MESSAGE);
		}
	}
}

package gui;

import javax.swing.table.DefaultTableModel;

/**
 * @author Benjamin Henshall
 *
 *         Custom table model class to prevent the user from being able to edit
 *         the data in the table
 */
public class UneditableDTM extends DefaultTableModel {

	public UneditableDTM(Object[][] data, Object[] columns) {
		// Calls the constructor of DefaultTableModel
		super(data, columns);
	}

	// Overrides the isCellEditable method in DefaultTableModel to return false,
	// so the user can't edit the cell
	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}
}

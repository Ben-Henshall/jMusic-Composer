package gui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * @author Benjamin Henshall
 *
 *         Custom cell renderer for colouring in a cell conditionally based on
 *         its value
 */
public class StatusCellRenderer extends DefaultTableCellRenderer {
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int col) {

		// Creates a new JLabel which will be the text in our cell
		JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);

		// Get the status for the current row
		ValidatedTableModel tableModel = (ValidatedTableModel) table.getModel();

		// Get the probability of the cell currently being rendered
		double prob = Double.parseDouble(tableModel.getValueAt(row, col).toString());
		// Color for storing which color we are using
		Color color;

		// If the probability is large
		if (prob > 0.75) {
			// Use color green
			color = new Color(0, 255, 0);
		} else if (prob > 0.5) {
			// Else if probability is medium, set to darker green
			color = new Color(64, 191, 0);
		} else if (prob > 0.25) {
			// Else if probability is low, set to green-reddish
			color = new Color(122, 122, 0);
		} else if (prob > 0) {
			// Else if probability is red, set to dark red
			color = new Color(191, 64, 0);
		} else {
			color = new Color(255, 0, 0);
		}

		// Set background of the label to the color we just decided on
		l.setBackground(color);

		// Return the JLabel which renders the cell.
		return l;
	}
}

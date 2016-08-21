package gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTextArea;

/**
 * @author Benjamin Henshall
 *
 *         The AboutDialog class is a custom JDialog used to display contact and
 *         licensing information
 */
public class AboutDialog extends JDialog {

	// String storing contact and licensing information
	String USER_GUIDE_TEXT = "Contact:\nBen@henshall.plus.com\n\n"

			+ "MIT License\n\n"

			+ "Copyright (c) [2016] [Benjamin Henshall]\n\n"

			+ "Permission is hereby granted, free of charge, to any person obtaining a copy "
			+ "of this software and associated documentation files (the \"Software\"), to deal "
			+ "in the Software without restriction, including without limitation the rights "
			+ "to use, copy, modify, merge, publish, distribute, sublicense, and/or sell "
			+ "copies of the Software, and to permit persons to whom the Software is "
			+ "furnished to do so, subject to the following conditions:\n\n"

			+ "The above copyright notice and this permission notice shall be included in all "
			+ "copies or substantial portions of the Software.\n\n"

			+ "THE SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR "
			+ "IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, "
			+ "FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE "
			+ "AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER "
			+ "LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, "
			+ "OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE " + "SOFTWARE.";

	JButton backButton;

	JFrame frame;

	public AboutDialog(JFrame j) {
		// Calls the JDialog constructor using JFrame J as the parent frame
		super(j, true);

		frame = j;
		setLayout(new GridBagLayout());

		// Sets the title of the Dialog
		setTitle("About");

		setSize(500, 650);
		// Ensures the dialog appears in the centre of the previous frame
		setLocationRelativeTo(frame);
		setResizable(false);

		// Gets a new GridBagConstraints for the license text area, then creates
		// and
		// adds it to the JPanel. I use a JTextArea as it is easier to fit large
		// amounts of text in compared to JLabel
		GridBagConstraints c = MainFrame.generateNewGridBag(0, 0, 1, 1, GridBagConstraints.HORIZONTAL,
				new Insets(0, 10, 0, 10));
		JTextArea licenseText = new JTextArea(USER_GUIDE_TEXT);
		// Sets the text area to fit the text in the JLabel using line wrapping
		licenseText.setLineWrap(true);
		// Removes the white background from the text area, to look more like a
		// JLabel
		licenseText.setOpaque(false);
		// Sets the text in the licensing text area to wrap by words, not by
		// letters. Prevents the words from becoming unreadable
		licenseText.setWrapStyleWord(true);
		// Prevents the user from editing the text area
		licenseText.setEditable(false);
		// And prevents the user from highlighting any text in the text area
		licenseText.setHighlighter(null);
		add(licenseText, c);

		// Gets a new GridBagConstraints for the back button, then creates and
		// adds the button to the JPanel.
		c = MainFrame.generateNewGridBag(0, 1, 1, 1, GridBagConstraints.HORIZONTAL, new Insets(20, 10, 0, 10));
		backButton = new JButton("Back");
		add(backButton, c);

		// Sets up the action listeners
		setUpActionListeners();
	}

	public void setUpActionListeners() {
		// Action listener for the back button
		backButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Simply closes the dialog
				dispose();
			}
		});
	}

}

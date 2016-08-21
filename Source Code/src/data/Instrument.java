package data;

/*
 * Class used to store details of an instrument. Includes the name and the constant given to it by jMusic
 */
public class Instrument {

	// Integer value equal to the jMusic constant for the instrument
	int value;

	// String containing the name of the instrument jMusic constant
	String name;

	public Instrument(int val, String nam) {
		value = val;
		name = nam;
	}

	/*
	 * Getter for the integer value of the jMusic constant
	 */
	public int getValue() {
		return value;
	}

	/*
	 * Getter for the string name of the jMusic constant
	 */
	public String getName() {
		return name;
	}

}

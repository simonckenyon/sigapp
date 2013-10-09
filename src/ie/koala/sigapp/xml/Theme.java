/**
 * 
 */
package ie.koala.sigapp.xml;

import android.graphics.Color;

/**
 * @author simon
 *
 */
public class Theme {
	String name;
	String colour;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the colour
	 */
	public String getColour() {
		return colour;
	}

	/**
	 * @return the colour as an integer
	 */
	public int getIntColour() {
		return Color.parseColor(colour);
	}

	/**
	 * @param colour the colour to set
	 */
	public void setColour(String colour) {
		this.colour = colour;
	}
}

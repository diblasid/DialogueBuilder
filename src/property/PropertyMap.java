package property;

import java.awt.Color;
import java.util.HashMap;

public class PropertyMap extends HashMap<Integer, Object> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PropertyMap() {
		super();
	}

	public int getIntValue(Integer key) {
		return (Integer) this.get(key);
	}

	public String getStringValue(Integer key) {
		return (String) this.get(key);
	}

	public double getDoubleValue(Integer key) {
		return (Double) this.get(key);
	}

	public Color getColorValue(Integer key) {
		return (Color) this.get(key);
	}

	public float getFloatValue(Integer key) {
		return (Float) this.get(key);
	}
}

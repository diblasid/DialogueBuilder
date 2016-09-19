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

	public int getIntValue(int key) {
		return (Integer) this.get(key);
	}

	public String getStringValue(int key) {
		return (String) this.get(key);
	}

	public double getDoubleValue(int key) {
		return (Double) this.get(key);
	}

	public Color getColorValue(int key) {
		return (Color) this.get(key);
	}

	public float getFloatValue(int key) {
		return (Float) this.get(key);
	}
}

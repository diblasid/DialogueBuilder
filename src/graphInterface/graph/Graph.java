package graphInterface.graph;

import graphInterface.graph.node.Node;
import graphInterface.property.EditType;
import graphInterface.property.PropertyKey;
import graphInterface.property.Selectable;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Graph implements Selectable {

	public enum GraphEnum implements PropertyKey {

		GRID_LINE_WIDTH("Grid Line Width", EditType.INTEGER), GRID_LINE_COLOR(
				"Grid Line Color", EditType.COLOR), ZOOM_X("Zoom X",
				EditType.DOUBLE), ZOOM_Y("Zoom Y", EditType.DOUBLE), ZOOM_SCALE(
				"Zoom Scale", EditType.DOUBLE), DEFAULT_NODE_COLOR(
				"Default Node Color", EditType.COLOR), DEFAULT_NODE_SELECTED_COLOR(
				"Default Node Color", EditType.COLOR), NODE_WIDTH("Node Width",
				EditType.INTEGER), NODE_HEIGHT("Node Height", EditType.INTEGER);

		private String propertyName;
		private EditType type;

		private GraphEnum(String propertyName, EditType type) {
			this.propertyName = propertyName;
			this.type = type;
		}

		public EditType getType() {
			return this.type;
		}

		public String getPropertyName() {
			return this.propertyName;
		}

	}

	/*
	 * private static final String GRID_LINE_WIDTH = "Grid Line Width",
	 * GRID_LINE_COLOR = "Grid Line Color", ZOOM_X = "Zoom X", ZOOM_Y =
	 * "Zoom Y", ZOOM_SCALE = "Zoom Scale";
	 */
	private int dimPixels;
	public static final int BASE_UNIT_SIZE = 32;
	private List<Node> nodes;
	protected Properties properties;

	public Graph(int dimPixels) {

		this.dimPixels = dimPixels;
		this.nodes = new ArrayList<Node>();
		this.properties = new Properties();
		properties.put(GraphEnum.GRID_LINE_WIDTH, 1);
		properties.put(GraphEnum.GRID_LINE_COLOR, Color.LIGHT_GRAY);
		properties.put(GraphEnum.ZOOM_X, 0.0);
		properties.put(GraphEnum.ZOOM_Y, 0.0);
		properties.put(GraphEnum.ZOOM_SCALE, 1.0);
		properties.put(GraphEnum.DEFAULT_NODE_COLOR, Color.GRAY);
		properties.put(GraphEnum.DEFAULT_NODE_SELECTED_COLOR, Color.RED);
		properties.put(GraphEnum.NODE_WIDTH, 10);
		properties.put(GraphEnum.NODE_HEIGHT, 6);

	}

	public int getNodeWidth() {
		return (Integer) properties.get(GraphEnum.NODE_WIDTH);
	}

	public int getNodeHeight() {
		return (Integer) properties.get(GraphEnum.NODE_HEIGHT);
	}

	public Color getDefaultNodeColor() {
		return (Color) properties.get(GraphEnum.DEFAULT_NODE_COLOR);
	}

	public Color getDefaultNodeSelectedColor() {
		return (Color) properties.get(GraphEnum.DEFAULT_NODE_SELECTED_COLOR);
	}

	public List<Node> getNodes() {
		return nodes;
	}

	public void addNode(Node node) {
		nodes.add(node);
	}

	public double getZoomPointX() {
		return (Double) properties.get(GraphEnum.ZOOM_X);
	}

	public double getZoomPointY() {
		return (Double) properties.get(GraphEnum.ZOOM_Y);
	}

	public double getZoomScale() {
		return (Double) properties.get(GraphEnum.ZOOM_SCALE);
	}

	public void setZoom(double x, double y, double scale) {
		properties.replace(GraphEnum.ZOOM_X, x);
		properties.replace(GraphEnum.ZOOM_Y, y);
		properties.replace(GraphEnum.ZOOM_SCALE, scale);
	}

	public int getGridLineWidth() {
		return (Integer) properties.get(GraphEnum.GRID_LINE_WIDTH);
	}

	public void setGridLineWidth(int gridLinesWidth) {
		properties.replace(GraphEnum.GRID_LINE_WIDTH, gridLinesWidth);
	}

	public Color getGridLinesColor() {
		return (Color) properties.get(GraphEnum.GRID_LINE_COLOR);
	}

	public void setGridLinesColor(Color gridLinesColor) {
		properties.replace(GraphEnum.GRID_LINE_COLOR, gridLinesColor);
	}

	public int getDimPixels() {
		return dimPixels;
	}

	public int getUnitSize() {
		return BASE_UNIT_SIZE;
	}

	public Properties getProperties() {
		return this.properties;
	}

}
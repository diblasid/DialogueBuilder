package graph;

import graph.node.Node;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Graph {

	private enum GraphEnum {
		GRID_LINE_WIDTH, GRID_LINE_COLOR, ZOOM_X, ZOOM_Y, ZOOM_SCALE
	}

	private Properties properties;
	private int dimPixels;
	public static final int BASE_UNIT_SIZE = 32;
	private List<Node> nodes;

	public Graph(int dimPixels) {

		this.dimPixels = dimPixels;
		this.nodes = new ArrayList<Node>();
		this.properties = new Properties();
		properties.put(GraphEnum.GRID_LINE_WIDTH, 1);
		properties.put(GraphEnum.GRID_LINE_COLOR, Color.LIGHT_GRAY);
		properties.put(GraphEnum.ZOOM_X, 0.0);
		properties.put(GraphEnum.ZOOM_Y, 0.0);
		properties.put(GraphEnum.ZOOM_SCALE, 1.0);

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

}
